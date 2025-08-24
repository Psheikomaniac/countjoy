package com.countjoy.presentation.eventlist;

import com.countjoy.domain.repository.EventRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class EventListViewModel_Factory implements Factory<EventListViewModel> {
  private final Provider<EventRepository> eventRepositoryProvider;

  public EventListViewModel_Factory(Provider<EventRepository> eventRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
  }

  @Override
  public EventListViewModel get() {
    return newInstance(eventRepositoryProvider.get());
  }

  public static EventListViewModel_Factory create(
      Provider<EventRepository> eventRepositoryProvider) {
    return new EventListViewModel_Factory(eventRepositoryProvider);
  }

  public static EventListViewModel newInstance(EventRepository eventRepository) {
    return new EventListViewModel(eventRepository);
  }
}
