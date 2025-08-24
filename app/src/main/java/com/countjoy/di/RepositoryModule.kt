package com.countjoy.di

import com.countjoy.data.repository.EventRepositoryImpl
import com.countjoy.data.repository.MilestoneRepositoryImpl
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.repository.MilestoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository
    
    @Binds
    @Singleton
    abstract fun bindMilestoneRepository(
        milestoneRepositoryImpl: MilestoneRepositoryImpl
    ): MilestoneRepository
}