package com.countjoy.di;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.GetEventUseCase;
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
public final class UseCaseModule_ProvideGetEventUseCaseFactory implements Factory<GetEventUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UseCaseModule_ProvideGetEventUseCaseFactory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetEventUseCase get() {
    return provideGetEventUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetEventUseCaseFactory create(
      Provider<EventRepository> repositoryProvider) {
    return new UseCaseModule_ProvideGetEventUseCaseFactory(repositoryProvider);
  }

  public static GetEventUseCase provideGetEventUseCase(EventRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetEventUseCase(repository));
  }
}
