package com.example.hopshop.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [FirebaseModule::class])
@ComponentScan("com.example.hopshop.data.repository")
class DataKoinModule