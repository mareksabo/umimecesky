package cz.muni.fi.umimecesky.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jaredrummler.materialspinner.MaterialSpinner
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.adapterlistener.CategoryAdapter
import cz.muni.fi.umimecesky.db.CategoryDbHelper
import cz.muni.fi.umimecesky.utils.Constant
import cz.muni.fi.umimecesky.utils.Constant.CHECKED_STATES
import cz.muni.fi.umimecesky.utils.Constant.LAST_FILLED_WORD
import cz.muni.fi.umimecesky.utils.Constant.LAST_SPINNER_VALUE
import cz.muni.fi.umimecesky.utils.Constant.TICKED_CATEGORIES_EXTRA
import cz.muni.fi.umimecesky.utils.GuiUtil
import cz.muni.fi.umimecesky.utils.Util
import kotlinx.android.synthetic.main.activity_list_categories.backButton
import kotlinx.android.synthetic.main.activity_list_categories.listView
import kotlinx.android.synthetic.main.activity_list_categories.nextButton
import kotlinx.android.synthetic.main.activity_list_categories.roundsSpinner
import kotlinx.android.synthetic.main.activity_list_categories.tickAll
import java.io.Serializable


class ListCategoriesActivity : AppCompatActivity() {

    private var categoryHelper: CategoryDbHelper? = null
    private var dataAdapter: CategoryAdapter? = null

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_categories)

        categoryHelper = CategoryDbHelper(this)

        setSpinner()
        displayListView()
        setButtonClick()
    }

    private fun setSpinner() {
        val spinnerValues = Constant.ROUND_POSSIBILITIES
        val lastValue = Util.getSharedPreferences(this)
                .getString(LAST_SPINNER_VALUE, Constant.INFINITY)

        roundsSpinner.setItems(spinnerValues)
        roundsSpinner.selectedIndex = spinnerValues.indexOf(lastValue)
        roundsSpinner.setOnItemSelectedListener(createSpinnerListener())
    }

    private fun createSpinnerListener(): MaterialSpinner.OnItemSelectedListener<String> {
        val activity = this@ListCategoriesActivity
        return MaterialSpinner.OnItemSelectedListener<String> { _, _, _, item -> Util.getSharedPreferences(activity).edit().putString(LAST_SPINNER_VALUE, item).apply() }
    }

    private fun displayListView() {
        val categories = categoryHelper!!.allCategories

        dataAdapter = CategoryAdapter(this, this, R.layout.category_info, categories, tickAll)
        listView.adapter = dataAdapter
    }


    private fun setButtonClick() {

        nextButton.setOnClickListener(View.OnClickListener {
            val selectedCategories = dataAdapter!!.selectedCategories
            if (selectedCategories.isEmpty()) {
                Toast.makeText(this@ListCategoriesActivity, R.string.choose_at_least_one_category,
                        Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            val intent = Intent(baseContext, TrainingActivity::class.java)
            intent.putExtra(TICKED_CATEGORIES_EXTRA, selectedCategories as Serializable)

            val sharedPref = Util.getSharedPreferences(baseContext)
            sharedPref.edit().putString(LAST_FILLED_WORD, null).apply()
            startActivity(intent)
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
        Util.saveArray(this, dataAdapter!!.getCheckedStates(), CHECKED_STATES)
    }

    override fun onResume() {
        super.onResume()
        val statesArray = Util.loadArray(this, CHECKED_STATES)
        if (statesArray.isNotEmpty()) dataAdapter!!.setCheckedStates(statesArray)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) GuiUtil.hideNavigationBar(this)
    }
}
