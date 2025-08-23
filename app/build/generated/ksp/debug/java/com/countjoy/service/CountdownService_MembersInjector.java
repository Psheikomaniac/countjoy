package com.countjoy.service;

import com.countjoy.domain.usecase.CalculateCountdownUseCase;
import com.countjoy.domain.usecase.GetEventUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CountdownService_MembersInjector implements MembersInjector<CountdownService> {
  private final Provider<GetEventUseCase> getEventUseCaseProvider;

  private final Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider;

  public CountdownService_MembersInjector(Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider) {
    this.getEventUseCaseProvider = getEventUseCaseProvider;
    this.calculateCountdownUseCaseProvider = calculateCountdownUseCaseProvider;
  }

  public static MembersInjector<CountdownService> create(
      Provider<GetEventUseCase> getEventUseCaseProvider,
      Provider<CalculateCountdownUseCase> calculateCountdownUseCaseProvider) {
    return new CountdownService_MembersInjector(getEventUseCaseProvider, calculateCountdownUseCaseProvider);
  }

  @Override
  public void injectMembers(CountdownService instance) {
    injectGetEventUseCase(instance, getEventUseCaseProvider.get());
    injectCalculateCountdownUseCase(instance, calculateCountdownUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.countjoy.service.CountdownService.getEventUseCase")
  public static void injectGetEventUseCase(CountdownService instance,
      GetEventUseCase getEventUseCase) {
    instance.getEventUseCase = getEventUseCase;
  }

  @InjectedFieldSignature("com.countjoy.service.CountdownService.calculateCountdownUseCase")
  public static void injectCalculateCountdownUseCase(CountdownService instance,
      CalculateCountdownUseCase calculateCountdownUseCase) {
    instance.calculateCountdownUseCase = calculateCountdownUseCase;
  }
}
