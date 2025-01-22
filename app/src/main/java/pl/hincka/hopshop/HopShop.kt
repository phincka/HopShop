package pl.hincka.hopshop

import android.app.Application
import pl.hincka.hopshop.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class HopShop : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext( this@HopShop)
            modules(
                AppModule().module,
            )
        }
    }
}