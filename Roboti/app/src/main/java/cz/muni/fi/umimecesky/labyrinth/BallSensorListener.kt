package cz.muni.fi.umimecesky.labyrinth

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
