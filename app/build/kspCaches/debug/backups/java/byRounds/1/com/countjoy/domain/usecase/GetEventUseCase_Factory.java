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
public final class GetEventUseCase_Factory implements Factory<GetEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public GetEventUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetEventUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetEventUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new GetEventUseCase_Factory(repositoryProvider);
  }

  public static GetEventUseCase newInstance(EventRepository repository) {
    return new GetEventUseCase(repository);
  }
}
