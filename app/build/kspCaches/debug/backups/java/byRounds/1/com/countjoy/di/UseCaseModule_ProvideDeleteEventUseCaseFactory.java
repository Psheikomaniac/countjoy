package com.countjoy.di;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.DeleteEventUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("dagger.hilt.android.scopes.ViewModelScoped")
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
public final class UseCaseModule_ProvideDeleteEventUseCaseFactory implements Factory<DeleteEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UseCaseModule_ProvideDeleteEventUseCaseFactory(
      Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteEventUseCase get() {
    return provideDeleteEventUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideDeleteEventUseCaseFactory create(
      Provider<EventRepository> repositoryProvider) {
    return new UseCaseModule_ProvideDeleteEventUseCaseFactory(repositoryProvider);
  }

  public static DeleteEventUseCase provideDeleteEventUseCase(EventRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDeleteEventUseCase(repository));
  }
}
