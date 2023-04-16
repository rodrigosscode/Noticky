package br.com.sscode.ui.extension

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

fun LifecycleCoroutineScope.launchDelayed(
    seconds: Long,
    actionBefore: () -> Unit = {},
    actionAfter: () -> Unit
) {
    TimeUnit.SECONDS.toMillis(seconds).let { secondsInMillis ->
        launch {
            actionBefore()
            delay(secondsInMillis)
            actionAfter()
        }
    }
}