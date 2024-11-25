package pl.hincka.hopshop.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DataKoinModule::class])
@ComponentScan("pl.hincka.hopshop")
class AppModule