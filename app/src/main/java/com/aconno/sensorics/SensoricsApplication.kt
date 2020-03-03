package com.aconno.sensorics

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.aconno.sensorics.dagger.application.DaggerAppComponent
import com.aconno.sensorics.dagger.worker.GeneralWorkerFactory
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

class SensoricsApplication : Application(), HasAndroidInjector {

    companion object {
        private const val DEV_BUILD_FLAVOR = "dev"
    }

    @Inject
    lateinit var workerFactory: GeneralWorkerFactory

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().create(this).inject(this)

        @Suppress("ConstantConditionIf")
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Fabric.with(this, Crashlytics())

        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )

        AndroidThreeTen.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
