package com.example.playlistmaker.domain.api

interface ValueManagerRepository<T> {
    fun saveValue(value: T)
    fun getValue(): T
}