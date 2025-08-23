package com.countjoy;

import com.countjoy.core.accessibility.AccessibilityManager;
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

  private final Provider<AccessibilityManager> accessibilityManagerProvider;

  public MainActivity_MembersInjector(Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider,
      Provider<AccessibilityManager> accessibilityManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.localeManagerProvider = localeManagerProvider;
    this.accessibilityManagerProvider = accessibilityManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider,
      Provider<AccessibilityManager> accessibilityManagerProvider) {
    return new MainActivity_MembersInjector(preferencesManagerProvider, localeManagerProvider, accessibilityManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
    injectLocaleManager(instance, localeManagerProvider.get());
    injectAccessibilityManager(instance, accessibilityManagerProvider.get());
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

  @InjectedFieldSignature("com.countjoy.MainActivity.accessibilityManager")
  public static void injectAccessibilityManager(MainActivity instance,
      AccessibilityManager accessibilityManager) {
    instance.accessibilityManager = accessibilityManager;
  }
}
