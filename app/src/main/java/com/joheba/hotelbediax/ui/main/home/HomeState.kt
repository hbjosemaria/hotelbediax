package com.joheba.hotelbediax.ui.main.home

data class HomeState(
    val result: HomeStateResult = HomeStateResult.Loading,
)

//Change Error and Success subclasses to fit your need at HomeScreen
sealed class HomeStateResult {
    data object Loading : HomeStateResult()
    data object Error : HomeStateResult()
    data object Success : HomeStateResult()
}