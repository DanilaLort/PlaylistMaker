package com.example.playlistmaker.domain.repository

interface ValueManagerRepository<T> {
    fun saveValue(value: T)
    fun getValue(): T
}