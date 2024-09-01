package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.ValueManagerInteractor
import com.example.playlistmaker.domain.repository.ValueManagerRepository

class ValueManagerInteractorImpl<T>(private val repository: ValueManagerRepository<T>) {
    fun getValue(): T {
        return repository.getValue()
    }
    fun saveValue(consumer: ValueManagerInteractor.ValueConsumer<T>) {
        repository.saveValue(consumer.consume())
    }
}