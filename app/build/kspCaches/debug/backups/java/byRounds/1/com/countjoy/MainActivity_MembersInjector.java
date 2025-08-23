package com.countjoy;

import com.countjoy.core.locale.LocaleManager;
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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  private final Provider<LocaleManager> localeManagerProvider;

  public MainActivity_MembersInjector(Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.localeManagerProvider = localeManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider) {
    return new MainActivity_MembersInjector(preferencesManagerProvider, localeManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
    injectLocaleManager(instance, localeManagerProvider.get());
  }

  @InjectedFieldSignature("com.countjoy.MainActivity.preferencesManager")
  public static void injectPreferencesManager(MainActivity instance,
      SharedPreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }

  @InjectedFieldSignature("com.countjoy.MainActivity.localeManager")
  public static void injectLocaleManager(MainActivity instance, LocaleManager localeManager) {
    instance.localeManager = localeManager;
  }
}
