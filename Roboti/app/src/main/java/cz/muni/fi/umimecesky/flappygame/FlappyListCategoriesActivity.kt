package cz.muni.fi.umimecesky.flappygame

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.pojo.RaceConcept
import kotlinx.android.synthetic.main.activity_flappy_category_list.categories



/**
 * @author Marek Sabo
 */
class FlappyListCategoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flappy_category_list)

        setDecorator()

        categories.layoutManager = LinearLayoutManager(this)
        categories.itemAnimator = DefaultItemAnimator()
        categories.adapter = ConceptAdapter(RaceConcept.initConcepts)
    }

    private fun setDecorator() {
        val itemDecorator = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.accent_divider)!!)
        categories.addItemDecoration(itemDecorator)
    }

    override fun onPause() {
        categories.adapter.notifyDataSetChanged()
        super.onPause()
    }
}