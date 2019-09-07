/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.practise

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.R.id.checkBox
import java.util.*

class CategoryAdapter(private val activity: Activity, context: Context, textViewResourceId: Int, categories: List<Category>, private val tickAll: Button) : ArrayAdapter<Category>(context, textViewResourceId, categories) {
    private var checkedStates: BooleanArray
    private val categoryList: List<Category>

    init {
        checkedStates = BooleanArray(categories.size)
        categoryList = ArrayList(categories)
    }


    internal inner class ViewHolder(val categoryName: TextView, val checkBox: CheckBox)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var returnView = convertView
        val holder: ViewHolder

        if (returnView == null) {
            val vi = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            returnView = vi.inflate(R.layout.category_info, parent, false)

            holder = ViewHolder(returnView.findViewById(R.id.categoryName),
                                returnView.findViewById(checkBox))
            holder.checkBox.tag = position
            returnView.tag = holder

            val listener = CategoryListener(this, holder.checkBox)
            returnView.setOnClickListener(listener)
            holder.checkBox.setOnClickListener(listener)
        } else {
            holder = returnView.tag as ViewHolder
        }

        val category = categoryList[position]
        holder.categoryName.text = category.name
        holder.checkBox.isChecked = checkedStates[position]
        holder.checkBox.tag = position

        return returnView!! // TODO fix in the future, create better code

    }


    internal fun isChecked(position: Int): Boolean = checkedStates[position]

    internal fun setCategoryChecked(position: Int, isChecked: Boolean) {
        checkedStates[position] = isChecked
        notifyDataSetChanged()
        checkTickAllText()
    }

    fun setAll(isChecked: Boolean) {
        for (i in 0 until this.count) {
            setCategoryChecked(i, isChecked)
        }
    }

    fun areAllChecked(): Boolean = (0 until count).any { isChecked(it) }

    val selectedCategories: List<Category>
        get() {
            return categoryList.indices
                    .filter { checkedStates[it] }
                    .map { categoryList[it] }
        }

    fun getCheckedStates(): BooleanArray = checkedStates

    fun setCheckedStates(checkedStates: BooleanArray) {
        this.checkedStates = checkedStates
        notifyDataSetChanged()
        checkTickAllText()
    }

    private fun checkTickAllText() {
        if (areAllChecked()) {
            tickAll.setText(R.string.none)
        } else {
            tickAll.setText(R.string.all)
        }
    }
}
