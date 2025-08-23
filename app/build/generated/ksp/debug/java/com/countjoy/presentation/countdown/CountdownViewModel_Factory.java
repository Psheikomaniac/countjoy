package com.countjoy.presentation.countdown;

import com.countjoy.domain.usecase.CalculateCountdownUseCase;
import com.countjoy.domain.usecase.DeleteEventUseCase;
import com.countjoy.domain.usecase.GetEventUseCase;
import com.countjoy.domain.usecase.UpdateEventUseCase;
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
public final class CountdownViewModel_Factory implements Factory<CountdownViewModel> {
  private final Provider<GetEventUseCase> getEventUseCaseProvider;

  private final Provider<DeleteEventUseCase> deleteEventUseCaseProvider;

  private final Provider<UpdateEventUseCase> updateEventUseCaseProvider;

  private final Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider;

  public CountdownViewModel_Factory(Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<DeleteEventUseCase> deleteEventUseCaseProvider,
      Provider<UpdateEventUseCase> updateEventUseCaseProvider,
      Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider) {
    this.getEventUseCaseProvider = getEventUseCaseProvider;
    this.deleteEventUseCaseProvider = deleteEventUseCaseProvider;
    this.updateEventUseCaseProvider = updateEventUseCaseProvider;
    this.calculateCountdownUseCaseProvider = calculateCountdownUseCaseProvider;
  }

  @Override
  public CountdownViewModel get() {
    return newInstance(getEventUseCaseProvider.get(), deleteEventUseCaseProvider.get(), updateEventUseCaseProvider.get(), calculateCountdownUseCaseProvider.get());
  }

  public static CountdownViewModel_Factory create(Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<DeleteEventUseCase> deleteEventUseCaseProvider,
      Provider<UpdateEventUseCase> updateEventUseCaseProvider,
      Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider) {
    return new CountdownViewModel_Factory(getEventUseCaseProvider, deleteEventUseCaseProvider, updateEventUseCaseProvider, calculateCountdownUseCaseProvider);
  }

  public static CountdownViewModel newInstance(GetEventUseCase getEventUseCase,
      DeleteEventUseCase deleteEventUseCase, UpdateEventUseCase updateEventUseCase,
      CalculateCountdownUseCase calculateCountdownUseCase) {
    return new CountdownViewModel(getEventUseCase, deleteEventUseCase, updateEventUseCase, calculateCountdownUseCase);
  }
}
