package com.countjoy.data.repository;

import com.countjoy.data.local.dao.CountdownEventDao;
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
public final class EventRepositoryImpl_Factory implements Factory<EventRepositoryImpl> {
  private final Provider<CountdownEventDao> eventDaoProvider;

  public EventRepositoryImpl_Factory(Provider<CountdownEventDao> eventDaoProvider) {
    this.eventDaoProvider = eventDaoProvider;
  }

  @Override
  public EventRepositoryImpl get() {
    return newInstance(eventDaoProvider.get());
  }

  public static EventRepositoryImpl_Factory create(Provider<CountdownEventDao> eventDaoProvider) {
    return new EventRepositoryImpl_Factory(eventDaoProvider);
  }

  public static EventRepositoryImpl newInstance(CountdownEventDao eventDao) {
    return new EventRepositoryImpl(eventDao);
  }
}
