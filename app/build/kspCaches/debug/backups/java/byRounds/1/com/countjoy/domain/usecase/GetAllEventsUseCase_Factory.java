package com.countjoy.domain.usecase;

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
public final class GetAllEventsUseCase_Factory implements Factory<GetAllEventsUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public GetAllEventsUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllEventsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllEventsUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new GetAllEventsUseCase_Factory(repositoryProvider);
  }

  public static GetAllEventsUseCase newInstance(EventRepository repository) {
    return new GetAllEventsUseCase(repository);
  }
}
