package com.example.playlistmaker.domain.api

interface ValueManagerInteractor<T> {
    fun getValue(): T
    fun saveValue(consumer: ValueConsumer<T>)

    interface ValueConsumer<T> {
        fun consume() : T
    }
}