package com.countjoy.receiver;

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
public final class TimezoneChangeReceiver_MembersInjector implements MembersInjector<TimezoneChangeReceiver> {
  private final Provider<GetEventUseCase> getEventUseCaseProvider;

  public TimezoneChangeReceiver_MembersInjector(Provider<GetEventUseCase> getEventUseCaseProvider) {
    this.getEventUseCaseProvider = getEventUseCaseProvider;
  }

  public static MembersInjector<TimezoneChangeReceiver> create(
      Provider<GetEventUseCase> getEventUseCaseProvider) {
    return new TimezoneChangeReceiver_MembersInjector(getEventUseCaseProvider);
  }

  @Override
  public void injectMembers(TimezoneChangeReceiver instance) {
    injectGetEventUseCase(instance, getEventUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.countjoy.receiver.TimezoneChangeReceiver.getEventUseCase")
  public static void injectGetEventUseCase(TimezoneChangeReceiver instance,
      GetEventUseCase getEventUseCase) {
    instance.getEventUseCase = getEventUseCase;
  }
}
