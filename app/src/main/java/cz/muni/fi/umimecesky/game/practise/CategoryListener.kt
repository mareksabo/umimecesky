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

import android.util.Log
import android.view.View
import android.widget.CheckBox

import cz.muni.fi.umimecesky.R

internal class CategoryListener(private val categoryAdapter: CategoryAdapter, private val checkBox: CheckBox) : View.OnClickListener {

    override fun onClick(v: View) {
        val position: Int

        when (v.id) {
            R.id.categoryCheckLayout -> {
                val holder = v.tag as CategoryAdapter.ViewHolder
                position = holder.checkBox.tag as Int
            }
            R.id.checkBox -> position = v.tag as Int
            else -> throw IllegalArgumentException("Wrong class: " + v.javaClass.name)
        }
        val changeChecked = !categoryAdapter.isChecked(position)
        categoryAdapter.setCategoryChecked(position, changeChecked)
        checkBox.isChecked = changeChecked

        Log.v("checkbox" + position, changeChecked.toString())
    }
}
