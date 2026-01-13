package com.caririfest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationManagerCompat
import com.caririfest.app.navigation.AppNavigation
import com.caririfest.app.ui.theme.CaririFestTheme
import com.caririfest.app.workmanager.NOTIFICATION_ID
import com.caririfest.app.workmanager.requestNotificationPermission
import com.caririfest.app.workmanager.scheduleCuriosidadeWorker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission(this, this)
        scheduleCuriosidadeWorker(this)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
        setContent { CaririFestTheme { CaririFestApp() } }
    }
}

@Composable
fun CaririFestApp() {
    AppNavigation()
}