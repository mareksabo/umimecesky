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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jaredrummler.materialspinner.MaterialSpinner
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.db.helper.categoryOpenHelper
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.game.shared.util.Constant
import cz.muni.fi.umimecesky.game.shared.util.Constant.TICKED_CATEGORIES
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.hideNavigationBar
import kotlinx.android.synthetic.main.activity_list_categories.backButton
import kotlinx.android.synthetic.main.activity_list_categories.listView
import kotlinx.android.synthetic.main.activity_list_categories.nextButton
import kotlinx.android.synthetic.main.activity_list_categories.roundsSpinner
import kotlinx.android.synthetic.main.activity_list_categories.tickAll
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity


class ListCategoriesActivity : AppCompatActivity() {

    private var dataAdapter: CategoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_categories)


        setSpinner()
        displayListView()
        setButtonClick()
    }

    private fun setSpinner() {
        val spinnerValues = Constant.ROUND_POSSIBILITIES

        roundsSpinner.setItems(spinnerValues)
        roundsSpinner.selectedIndex = spinnerValues.indexOf(prefs.seriesAmount)
        roundsSpinner.setOnItemSelectedListener(createSpinnerListener())
    }

    private fun createSpinnerListener(): MaterialSpinner.OnItemSelectedListener<String> =
            MaterialSpinner.OnItemSelectedListener { _, _, _, item -> prefs.seriesAmount = item }

    private fun displayListView() {
        val allCategories = categoryOpenHelper.allCategories()

        dataAdapter = CategoryAdapter(this, this, R.layout.category_info, allCategories, tickAll)
        listView.adapter = dataAdapter
    }


    private fun setButtonClick() {

        nextButton.setOnClickListener(View.OnClickListener {
            val selectedCategories = dataAdapter!!.selectedCategories
            if (selectedCategories.isEmpty()) {
                longToast(R.string.choose_at_least_one_category)
                return@OnClickListener
            }

            startActivity<TrainingActivity>(TICKED_CATEGORIES to selectedCategories)
        })


        tickAll.setOnClickListener {
            val allChecked = dataAdapter!!.areAllChecked()
            Log.v("allChecked", allChecked.toString())
            dataAdapter!!.setAll(!allChecked)
        }

        backButton.setOnClickListener { finish() }

    }

    override fun onPause() {
        super.onPause()
        prefs.checkedStates = dataAdapter!!.getCheckedStates()
    }

    override fun onResume() {
        super.onResume()
        val statesArray = prefs.checkedStates
        if (statesArray.size > 1) dataAdapter!!.setCheckedStates(statesArray)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideNavigationBar()
    }
}
