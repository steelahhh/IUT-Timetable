package com.alefimenko.iuttimetable.feature

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupActivity

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startActivity(PickGroupActivity.createIntent(this))
            finish()
        }, 500)
    }
}
