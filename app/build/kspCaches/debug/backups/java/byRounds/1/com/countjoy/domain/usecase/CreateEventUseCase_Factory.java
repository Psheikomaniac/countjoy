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
public final class CreateEventUseCase_Factory implements Factory<CreateEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public CreateEventUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateEventUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateEventUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new CreateEventUseCase_Factory(repositoryProvider);
  }

  public static CreateEventUseCase newInstance(EventRepository repository) {
    return new CreateEventUseCase(repository);
  }
}
