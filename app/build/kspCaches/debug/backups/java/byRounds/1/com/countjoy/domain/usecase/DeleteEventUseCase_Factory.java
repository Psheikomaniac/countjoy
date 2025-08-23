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
public final class DeleteEventUseCase_Factory implements Factory<DeleteEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public DeleteEventUseCase_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteEventUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteEventUseCase_Factory create(Provider<EventRepository> repositoryProvider) {
    return new DeleteEventUseCase_Factory(repositoryProvider);
  }

  public static DeleteEventUseCase newInstance(EventRepository repository) {
    return new DeleteEventUseCase(repository);
  }
}
