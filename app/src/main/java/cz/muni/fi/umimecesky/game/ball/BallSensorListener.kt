/*
 * Copyright (c) 2018 Marek Sabo
 * Modifications copyright (C) 2010 The Android Open Source Project
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

package cz.muni.fi.umimecesky.game.ball

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager

/**
 * @author Marek Sabo
 */

class BallSensorListener(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val defaultDisplay by lazy {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }

    var sensor = Point2Df(0f, 0f)

    /*
     * It is not necessary to get accelerometer events at a very high
     * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
     * automatic low-pass filter, which "extracts" the gravity component
     * of the acceleration. As an added benefit, we use less power and
     * CPU resources.
     */
    fun startSimulation() = sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
    )

    fun stopSimulation() = sensorManager.unregisterListener(this)

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        when (defaultDisplay.rotation) {
            Surface.ROTATION_0 -> {
                sensor.x = event.values[0]
                sensor.y = event.values[1]
            }
            Surface.ROTATION_90 -> {
                sensor.x = -event.values[1]
                sensor.y = event.values[0]
            }
            Surface.ROTATION_180 -> {
                sensor.x = -event.values[0]
                sensor.y = -event.values[1]
            }
            Surface.ROTATION_270 -> {
                sensor.x = event.values[1]
                sensor.y = -event.values[0]
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

}
