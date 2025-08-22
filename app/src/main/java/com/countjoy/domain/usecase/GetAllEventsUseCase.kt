package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<List<CountdownEvent>> {
        return repository.getAllEvents()
    }
    
    fun getActiveEvents(): Flow<List<CountdownEvent>> {
        return repository.getActiveEvents()
    }
}