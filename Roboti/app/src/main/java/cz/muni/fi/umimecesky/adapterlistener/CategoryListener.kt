package cz.muni.fi.umimecesky.adapterlistener

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
