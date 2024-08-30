package com.accesso.accessocommercesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.accesso.AccessoCore
import com.accessosdk.accessocommerce.AccessoCommerce

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AccessoCore
            .setApplicationContext(applicationContext)
            .setActivity(this)
            .build()
        AccessoCore.configureThemeCustomization()

        val accessoCommerce = AccessoCommerce()
        accessoCommerce.showStoreProducts(this)

    }
}
