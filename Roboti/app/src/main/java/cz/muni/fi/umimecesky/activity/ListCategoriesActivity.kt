package cz.muni.fi.umimecesky.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jaredrummler.materialspinner.MaterialSpinner
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.adapterlistener.CategoryAdapter
import cz.muni.fi.umimecesky.db.helper.categoryOpenHelper
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.Constant
import cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES
import cz.muni.fi.umimecesky.utils.GuiUtil
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

            prefs.lastShownWord = null
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
        if (hasFocus) GuiUtil.hideNavigationBar(this)
    }
}
