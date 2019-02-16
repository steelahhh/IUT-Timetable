package com.alefimenko.iuttimetable.core.data.remote

import android.content.Context
import android.content.Intent
import android.os.Build
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.model.ScheduleEntity

/*
 * Created by Alexander Efimenko on 2019-01-20.
 */

class FeedbackService(private val context: Context) {

    fun sendFeedback(throwable: Throwable?, schedule: ScheduleEntity) {
        sendFeedback(
            throwable,
            schedule.formId, schedule.groupId,
            schedule.groupName, schedule.instituteId,
            schedule.instituteName
        )
    }

    fun sendFeedback(
        throwable: Throwable?,
        formId: Int, groupId: Int, groupName: String,
        instituteId: Int, instituteName: String
    ) {
        val subject = "Обратная связь Расписание ТИУ ${if (throwable != null) "| Error report" else ""}"
        val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "2.2.0"
        val sb = StringBuilder().apply {
            append("Место для сообщения:\n\n\n-----------------------------")
            append("\nPlease don't remove this information")
            append("\nDevice OS version: ${Build.VERSION.RELEASE}")
            append("\nDevice brand: ${Build.BRAND}")
            append("\nDevice model: ${Build.MODEL}")
            append("\nDevice manufacturer: ${Build.MANUFACTURER}")
            append("\nApp version: $version")
            append("\nForm id: $formId")
            append("\nGroup id: $groupId; name: $groupName")
            append("\nInstitute id: $instituteId; name: $instituteName")
            append(if (throwable != null) "\nError message: ${throwable.message}" else "")
        }
        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("iuttimetablefeedback@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, sb.toString())
                }, context.getString(R.string.str_choose_email_client)
            )
        )
    }
}