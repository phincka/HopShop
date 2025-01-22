package pl.hincka.hopshop.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [FirebaseModule::class])
@ComponentScan("pl.hincka.hopshop.data.repository")
class DataKoinModule