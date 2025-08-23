package com.countjoy.di;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.UpdateEventUseCase;
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
public final class UseCaseModule_ProvideUpdateEventUseCaseFactory implements Factory<UpdateEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UseCaseModule_ProvideUpdateEventUseCaseFactory(
      Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateEventUseCase get() {
    return provideUpdateEventUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideUpdateEventUseCaseFactory create(
      Provider<EventRepository> repositoryProvider) {
    return new UseCaseModule_ProvideUpdateEventUseCaseFactory(repositoryProvider);
  }

  public static UpdateEventUseCase provideUpdateEventUseCase(EventRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideUpdateEventUseCase(repository));
  }
}
