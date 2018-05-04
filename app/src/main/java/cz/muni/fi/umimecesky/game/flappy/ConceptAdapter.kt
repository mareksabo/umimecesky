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

package cz.muni.fi.umimecesky.game.flappy

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.inflate
import cz.muni.fi.umimecesky.prefs
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