package com.alefimenko.iuttimetable.core.data.remote

import android.content.Context
import android.content.Intent
import android.os.Build
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.data.local.model.ScheduleEntity

/*
 * Created by Alexander Efimenko on 2019-01-20.
 */

class FeedbackService(private val context: Context) {

    fun sendFeedback(schedule: ScheduleEntity, throwable: Throwable? = null) {
        sendFeedback(
            schedule.formId,
            schedule.groupId,
            schedule.groupName,
            schedule.instituteId,
            schedule.instituteName,
            throwable
        )
    }

    fun sendFeedback(
        formId: Int,
        groupId: Int,
        groupName: String,
        instituteId: Int,
        instituteName: String,
        throwable: Throwable? = null
    ) {
        val subject =
            "Обратная связь Расписание ТИУ ${if (throwable != null) "| Error report" else ""}"
        val version = context.packageManager
            .getPackageInfo(context.packageName, 0).versionName ?: "2.2.0"
        val text = """
            Место для сообщения:



            -----------------------------

            Please don't remove this information
            Device OS version: ${Build.VERSION.RELEASE}
            Device brand: ${Build.BRAND}
            Device model: ${Build.MODEL}
            Device manufacturer: ${Build.MANUFACTURER}
            App version: $version
            Form (id=$formId)
            Group (id=$groupId, name=$groupName)
            Institute (id=$instituteId, name=$instituteName)
            ${if (throwable != null) "\nError message: ${throwable.message}" else ""}
        """.trimIndent()

        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("iuttimetablefeedback@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, text)
                }, context.getString(R.string.str_choose_email_client)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
