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


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.shared.util.GuiUtil.hideNavigationBar
import cz.muni.fi.umimecesky.prefs
import kotlinx.android.synthetic.main.activity_race_levels.levelListView
import org.jetbrains.anko.startActivity

class LevelRaceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race_levels)

    }

    override fun onStart() {
        // TODO: replace with data update - notify?
        super.onStart()

        levelListView.layoutManager = LinearLayoutManager(this)
        levelListView.itemAnimator = DefaultItemAnimator()

        levelListView.adapter =
                LevelAdapter(prefs.maxRobotsCategories, { raceConcept ->
                    prefs.currentRobotConcept = raceConcept
                    startActivity<RaceActivity>()
                })

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideNavigationBar()
    }

}
