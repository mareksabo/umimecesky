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

package cz.muni.fi.umimecesky.game.robots

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.model.RaceConcept
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.inflate

class LevelAdapter(private val items: List<RaceConcept>,
                   private val listener: (RaceConcept) -> Unit) :
        RecyclerView.Adapter<LevelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.column_category_race))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val sectionName = itemView.findViewById<TextView>(R.id.sectionName)
        private val currentLevel = itemView.findViewById<TextView>(R.id.currentLevel)
        private val progressBar = itemView.findViewById<RoundCornerProgressBar>(R.id.levelProgress)

        fun bind(item: RaceConcept, listener: (RaceConcept) -> Unit) = with(itemView) {
            sectionName.text = item.name
            val currentLevelNumber = item.currentLevel
            currentLevel.text = currentLevelNumber.toString()
            progressBar.max = (item.numberOfLevels - 1).toFloat()
            progressBar.progress = (currentLevelNumber - 1).toFloat()
            progressBar.secondaryProgress = currentLevelNumber.toFloat()
            setOnClickListener { listener(item) }
        }
    }

}

