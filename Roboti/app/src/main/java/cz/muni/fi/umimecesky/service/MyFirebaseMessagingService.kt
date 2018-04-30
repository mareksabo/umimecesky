package cz.muni.fi.umimecesky.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import cz.muni.fi.umimecesky.R
import cz.muni.fi.umimecesky.game.mainmenu.MainActivity



/**
 * Service for pushing notifications when
 *
 * @author Marek Sabo
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "admin_channel_id"
        private const val REQUEST_CODE = 1
        private const val NOTIFICATION_ID = 3456
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage == null) return

        var title = remoteMessage.data["title"]
        if (title.isNullOrEmpty()) title = "Umíme česky"

        var message = remoteMessage.data["message"]
        if (message.isNullOrEmpty()) message = "Mrkněte na novou verzi aplikace!"

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

}
