package com.example.knightsadventuremobile.ui.theme.Models

data class GameResponse<T1, T2>(
    val value: T1,
    val extra: T2
)