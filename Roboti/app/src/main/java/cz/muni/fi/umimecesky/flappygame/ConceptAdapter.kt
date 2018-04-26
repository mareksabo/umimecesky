package cz.muni.fi.umimecesky.flappygame

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.pojo.RaceConcept
import cz.muni.fi.umimecesky.prefs
import cz.muni.fi.umimecesky.utils.Constant.FLAPPY_CHOSEN_CATEGORY
import cz.muni.fi.umimecesky.utils.GuiUtil.inflate
import kotlinx.android.synthetic.main.column_category_flappy.view.categoryName
import kotlinx.android.synthetic.main.column_category_flappy.view.highestScore
import org.jetbrains.anko.startActivity

/**
 * @author Marek Sabo
 */
class ConceptAdapter(private val concepts: List<RaceConcept>) :
        RecyclerView.Adapter<ConceptAdapter.ConceptHolder>() {

    override fun getItemCount() = concepts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConceptAdapter.ConceptHolder =
            ConceptHolder(parent.inflate(R.layout.column_category_flappy, false))

    override fun onBindViewHolder(holder: ConceptAdapter.ConceptHolder, position: Int) =
            holder.bindConcept(concepts[position])

    class ConceptHolder(private var v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var raceConcept: RaceConcept? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) =
                itemView.context.startActivity<JumpGameActivity>(
                        FLAPPY_CHOSEN_CATEGORY to raceConcept)


        fun bindConcept(raceConcept: RaceConcept) {
            this.raceConcept = raceConcept

            v.categoryName.text = raceConcept.name
            v.highestScore.text = "${prefs.getBestScore(raceConcept)}"
        }
    }

}