package com.countjoy.di;

import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.GetAllEventsUseCase;
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
public final class UseCaseModule_ProvideGetAllEventsUseCaseFactory implements Factory<GetAllEventsUseCase> {
  private final Provider<EventRepository> repositoryProvider;

  public UseCaseModule_ProvideGetAllEventsUseCaseFactory(
      Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllEventsUseCase get() {
    return provideGetAllEventsUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetAllEventsUseCaseFactory create(
      Provider<EventRepository> repositoryProvider) {
    return new UseCaseModule_ProvideGetAllEventsUseCaseFactory(repositoryProvider);
  }

  public static GetAllEventsUseCase provideGetAllEventsUseCase(EventRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetAllEventsUseCase(repository));
  }
}
