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
public final class UpdateEventUseCase_Factory implements Factory<UpdateEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UpdateEventUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateEventUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateEventUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new UpdateEventUseCase_Factory(repositoryProvider);
  }

  public static UpdateEventUseCase newInstance(EventRepository repository) {
    return new UpdateEventUseCase(repository);
  }
}
