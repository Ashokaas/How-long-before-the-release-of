package fr.ashokas.nintendogamesreleases


import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.annotation.RequiresApi

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class totk : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        scheduleWidgetUpdate(context)

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysUntilDate(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(dateString, formatter)
    val today = LocalDate.now()
    return ChronoUnit.DAYS.between(today, date)
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.totk)
    val days_left = getDaysUntilDate("12/05/2023").toInt()

    val widgetText = when {
        days_left <= -2 -> "Le jeu est sorti il y a ${kotlin.math.abs(days_left)} jours"
        days_left == -1 -> "Le jeu est sorti hier"
        days_left == 0 -> "Le jeu sort aujourd'hui"
        days_left == 1 -> "Le jeu sort demain"
        days_left >= 2 -> "$days_left Jours Restants"
        else -> "Erreur, veuillez la signaler au développeur"
    }

    views.setTextViewText(R.id.appwidget_text, widgetText)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}


@RequiresApi(Build.VERSION_CODES.O)
fun getNextMidnight(): LocalDateTime {
    return LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0)
}


@RequiresApi(Build.VERSION_CODES.O)
private fun scheduleWidgetUpdate(context: Context) {
    val intent = Intent(context, totk::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, totk::class.java)))
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        getNextMidnight().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        pendingIntent
    )
}

