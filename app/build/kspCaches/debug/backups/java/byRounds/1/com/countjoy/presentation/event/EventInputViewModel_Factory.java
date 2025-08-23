package com.countjoy.presentation.event;

import androidx.lifecycle.SavedStateHandle;
import com.countjoy.domain.usecase.CreateEventUseCase;
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
public final class EventInputViewModel_Factory implements Factory<EventInputViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetEventUseCase> getEventUseCaseProvider;

  private final Provider<CreateEventUseCase> createEventUseCaseProvider;

  private final Provider<UpdateEventUseCase> updateEventUseCaseProvider;

  public EventInputViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<CreateEventUseCase> createEventUseCaseProvider,
      Provider<UpdateEventUseCase> updateEventUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getEventUseCaseProvider = getEventUseCaseProvider;
    this.createEventUseCaseProvider = createEventUseCaseProvider;
    this.updateEventUseCaseProvider = updateEventUseCaseProvider;
  }

  @Override
  public EventInputViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getEventUseCaseProvider.get(), createEventUseCaseProvider.get(), updateEventUseCaseProvider.get());
  }

  public static EventInputViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<CreateEventUseCase> createEventUseCaseProvider,
      Provider<UpdateEventUseCase> updateEventUseCaseProvider) {
    return new EventInputViewModel_Factory(savedStateHandleProvider, getEventUseCaseProvider, createEventUseCaseProvider, updateEventUseCaseProvider);
  }

  public static EventInputViewModel newInstance(SavedStateHandle savedStateHandle,
      GetEventUseCase getEventUseCase, CreateEventUseCase createEventUseCase,
      UpdateEventUseCase updateEventUseCase) {
    return new EventInputViewModel(savedStateHandle, getEventUseCase, createEventUseCase, updateEventUseCase);
  }
}
