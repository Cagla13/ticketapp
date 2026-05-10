package com.turkcell.ticketapp.di

import com.turkcell.ticketapp.login.LoginViewModel
import com.turkcell.ticketapp.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LoginViewModel(get()) }

    viewModel { RegisterViewModel(get()) }
}