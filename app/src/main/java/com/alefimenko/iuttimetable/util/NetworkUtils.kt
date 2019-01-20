package com.alefimenko.iuttimetable.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import com.alefimenko.iuttimetable.model.ScheduleRoomModel

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

object NetworkUtils {

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun sendFeedback(context: Context, throwable: Throwable?, schedule: ScheduleRoomModel) {
        sendFeedback(context, throwable,
            schedule.formId, schedule.groupId,
            schedule.groupName, schedule.instituteId,
            schedule.instituteName
        )
    }

    @JvmStatic
    fun sendFeedback(context: Context, throwable: Throwable?,
                     formId: Int, groupId: Int, groupName: String,
                     instituteId: Int, instituteName: String) {
        val sb = StringBuilder()
        var subject = "Обратная связь Расписание ТИУ"
        try {
            val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            sb.append("Место для сообщения:\n\n\n-----------------------------")
                .append("\nPlease don't remove this information")
                .append("\nDevice OS version: ${Build.VERSION.RELEASE}")
                .append("\nDevice brand: ${Build.BRAND}")
                .append("\nDevice model: ${Build.MODEL}")
                .append("\nDevice manufacturer: ${Build.MANUFACTURER}")
                .append("\nApp version: $version")
                .append("\nForm id: $formId")
                .append("\nGroup id: $groupId")
                .append("\nGroup name: $groupName")
                .append("\nInstitute id: $instituteId")
                .append("\nInstitute name: $instituteName")
            if (throwable != null) {
                sb.append("\nError message: ${throwable.message}")
                subject += "| Error report"
            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("iuttimetablefeedback@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, sb.toString())
                }, "")) // context.getString(R.string.str_choose_email_client)
    }
}
