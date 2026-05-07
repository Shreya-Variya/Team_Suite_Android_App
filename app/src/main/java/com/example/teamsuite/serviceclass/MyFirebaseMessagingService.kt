package com.example.teamsuite.serviceclass

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.teamsuite.R
import com.example.teamsuite.data.model.notification.SaveFcmTokenRequest
import com.example.teamsuite.repository.notification.SaveFcmTokenRepository
import com.example.teamsuite.utils.Constant
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data["title"] ?: ""
        val body = remoteMessage.data["body"] ?: ""
        val leaveId = remoteMessage.data["leaveId"] ?: ""
        val employeeId = remoteMessage.data["employeeId"] ?: ""
        val type = remoteMessage.data["type"] ?: ""

        when(type) {
            "leave_request" -> {
                showLeaveNotification(title, body, leaveId, employeeId)
            }
            "leave_status" -> {
                showSimpleNotification(title, body)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val sharedPref = applicationContext.getSharedPreferences(
            Constant.PREF_NAME,
            MODE_PRIVATE
        )

        val isLoggedIn = sharedPref.getBoolean(Constant.KEY_LOGGED_IN, false)
        val userId = sharedPref.getString(Constant.KEY_USER_ID, null)
        val category = sharedPref.getString(Constant.KEY_CATEGORY, null)

        if (isLoggedIn && userId != null && category != null) {

            sharedPref.edit().putString("FCM_TOKEN", token).apply()
            val request = SaveFcmTokenRequest(userId, category, token)

//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val response = SaveFcmTokenRepository().saveFcmToken(request)
//                    Log.i("FCM", "Token saved: ${response.isSuccessful}")
//                } catch (e: Exception) {
//                    Log.e("FCM", "Error: ${e.message}")
//                }
//            }
            // Direct API call (no ViewModel in service)
//            SaveFcmTokenRepository().saveFcmToken(request)
        }
    }
    private fun showLeaveNotification(
        title: String,
        body: String,
        leaveId: String,
        employeeId: String
    ) {
        val notificationId = System.currentTimeMillis().toInt()

        val acceptIntent = Intent(this, NotificationActionReceiver::class.java)
        acceptIntent.action = "ACTION_ACCEPT"
        acceptIntent.putExtra("leaveId", leaveId)
        acceptIntent.putExtra("employeeId", employeeId)
        acceptIntent.putExtra("notificationId", notificationId)

        val rejectIntent = Intent(this, NotificationActionReceiver::class.java)
        rejectIntent.action = "ACTION_REJECT"
        rejectIntent.putExtra("leaveId", leaveId)
        rejectIntent.putExtra("employeeId", employeeId)
        rejectIntent.putExtra("notificationId", notificationId)

        val acceptPendingIntent = PendingIntent.getBroadcast(
            this,
            100,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectPendingIntent = PendingIntent.getBroadcast(
            this,
            101,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "leave_channel")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(body)
            .addAction(R.drawable.ic_accept, "Accept", acceptPendingIntent)
            .addAction(R.drawable.ic_reject, "Reject", rejectPendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }

    private fun showSimpleNotification(title: String, body: String,){
        val notification = NotificationCompat.Builder(this, "leave_channel")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, notification)
    }
}