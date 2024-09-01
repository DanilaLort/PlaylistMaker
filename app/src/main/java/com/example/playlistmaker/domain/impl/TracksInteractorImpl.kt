package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.TracksInteractor
import com.example.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(text))
        }
    }

}