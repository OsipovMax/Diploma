package com.example.maapp.presentation.ui.base.view_model


interface Event

data class OpenLinkEvent(val link: String) : Event

data class ErrorEvent(val error: Throwable) : Event

data class ErrorMessageEvent(val errorMessage: String) : Event

data class MessageEvent(val message: String) : Event