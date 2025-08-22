package com.countjoy.di

import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    
    @Provides
    @ViewModelScoped
    fun provideGetEventUseCase(
        repository: EventRepository
    ): GetEventUseCase {
        return GetEventUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideGetAllEventsUseCase(
        repository: EventRepository
    ): GetAllEventsUseCase {
        return GetAllEventsUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideCreateEventUseCase(
        repository: EventRepository
    ): CreateEventUseCase {
        return CreateEventUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideUpdateEventUseCase(
        repository: EventRepository
    ): UpdateEventUseCase {
        return UpdateEventUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideDeleteEventUseCase(
        repository: EventRepository
    ): DeleteEventUseCase {
        return DeleteEventUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideCalculateCountdownUseCase(): CalculateCountdownUseCase {
        return CalculateCountdownUseCase()
    }
}