package com.countjoy.service;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.repository.MilestoneRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AnalyticsService_Factory implements Factory<AnalyticsService> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<MilestoneRepository> milestoneRepositoryProvider;

  public AnalyticsService_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<MilestoneRepository> milestoneRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.milestoneRepositoryProvider = milestoneRepositoryProvider;
  }

  @Override
  public AnalyticsService get() {
    return newInstance(eventRepositoryProvider.get(), milestoneRepositoryProvider.get());
  }

  public static AnalyticsService_Factory create(Provider<EventRepository> eventRepositoryProvider,
      Provider<MilestoneRepository> milestoneRepositoryProvider) {
    return new AnalyticsService_Factory(eventRepositoryProvider, milestoneRepositoryProvider);
  }

  public static AnalyticsService newInstance(EventRepository eventRepository,
      MilestoneRepository milestoneRepository) {
    return new AnalyticsService(eventRepository, milestoneRepository);
  }
}
