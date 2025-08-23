package com.countjoy.receiver;

import com.countjoy.data.local.preferences.SharedPreferencesManager;
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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  public BootReceiver_MembersInjector(
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    return new BootReceiver_MembersInjector(preferencesManagerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
  }

  @InjectedFieldSignature("com.countjoy.receiver.BootReceiver.preferencesManager")
  public static void injectPreferencesManager(BootReceiver instance,
      SharedPreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }
}
