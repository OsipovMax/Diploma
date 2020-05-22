package com.example.maapp.presentation.ui.base.state

sealed class ViewState<out T>
data class Data<T>(val data: T) : ViewState<T>()
object Loading : ViewState<Nothing>()
data class Error(val error: Throwable) : ViewState<Nothing>()