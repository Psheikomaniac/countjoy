package com.countjoy.di;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.CreateEventUseCase;
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
public final class UseCaseModule_ProvideCreateEventUseCaseFactory implements Factory<CreateEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UseCaseModule_ProvideCreateEventUseCaseFactory(
      Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateEventUseCase get() {
    return provideCreateEventUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideCreateEventUseCaseFactory create(
      Provider<EventRepository> repositoryProvider) {
    return new UseCaseModule_ProvideCreateEventUseCaseFactory(repositoryProvider);
  }

  public static CreateEventUseCase provideCreateEventUseCase(EventRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideCreateEventUseCase(repository));
  }
}
