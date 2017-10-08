package cz.muni.fi.umimecesky.adapterlistener

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar

import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.pojo.RaceConcept

class LevelAdapter(private val activity: Activity, private val list: List<RaceConcept>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var returnView = convertView
        val holder: ViewHolder

        if (returnView == null) {
            returnView = LayoutInflater.from(activity).inflate(R.layout.column_category_race, parent, false)

            holder = ViewHolder()
            holder.sectionName = returnView!!.findViewById<TextView>(R.id.sectionName)
            holder.currentLevel = returnView.findViewById<TextView>(R.id.currentLevel)
            holder.progressBar = returnView.findViewById<RoundCornerProgressBar>(R.id.levelProgress)
            holder.progressBar!!.max = (list[position].numberOfLevels - 1).toFloat()
            returnView.tag = holder
        } else {
            holder = returnView.tag as ViewHolder
        }

        val raceConcept = list[position]
        holder.sectionName!!.text = raceConcept.name
        setProgressBar(holder, raceConcept)
        return returnView
    }

    private fun setProgressBar(holder: ViewHolder, raceConcept: RaceConcept) {
        val progressBar = holder.progressBar
        val textCurrentLevel = holder.currentLevel
        var currentLevel = raceConcept.getCurrentLevel()
        textCurrentLevel!!.text = currentLevel.toString()
        currentLevel-- //progress bar starts at 0, level at 1
        progressBar!!.progress = currentLevel.toFloat()
        progressBar.secondaryProgress = (currentLevel + 1).toFloat()
    }

    private inner class ViewHolder {
        internal var sectionName: TextView? = null
        internal var currentLevel: TextView? = null
        internal var progressBar: RoundCornerProgressBar? = null
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}

