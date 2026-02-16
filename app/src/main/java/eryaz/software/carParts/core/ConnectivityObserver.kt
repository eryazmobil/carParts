package eryaz.software.carParts.core

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>
    fun currentStatus(): Status

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}