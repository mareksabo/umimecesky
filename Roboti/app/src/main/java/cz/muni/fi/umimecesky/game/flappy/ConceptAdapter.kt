package cz.muni.fi.umimecesky.game.flappy

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.inflate
import kotlinx.android.synthetic.main.column_category_flappy.view.categoryName
import kotlinx.android.synthetic.main.column_category_flappy.view.highestScore


/**
 * @author Marek Sabo
 */
class ConceptAdapter(concepts: List<RaceConcept>) :
        RecyclerView.Adapter<ConceptAdapter.ConceptHolder>() {

    private val concepts = concepts.toMutableList()

    override fun getItemCount() = concepts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConceptAdapter.ConceptHolder =
            ConceptHolder(parent.inflate(R.layout.column_category_flappy, false))

    override fun onBindViewHolder(holder: ConceptAdapter.ConceptHolder, position: Int) =
            holder.bindConcept(concepts[position])

    // workaround fixing not refreshing score bug
    fun refreshAllItems() {
        val list: List<RaceConcept> = concepts.toList()
        concepts.clear()
        concepts.addAll(list)
        notifyDataSetChanged()
    }

    class ConceptHolder(private var v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var raceConcept: RaceConcept? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            try {
                FlappySettingsDialog(itemView.context, raceConcept)
            } catch (ignored: IllegalArgumentException) {
            }
        }


        fun bindConcept(raceConcept: RaceConcept) {
            this.raceConcept = raceConcept

            v.categoryName.text = raceConcept.name
            v.highestScore.text = "${prefs.getBestScore(raceConcept)}"
        }
    }

}