package com.countjoy.presentation.settings;

import com.countjoy.core.locale.LocaleManager;
import com.countjoy.data.local.preferences.SharedPreferencesManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  private final Provider<LocaleManager> localeManagerProvider;

  public SettingsViewModel_Factory(Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.localeManagerProvider = localeManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get(), localeManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<LocaleManager> localeManagerProvider) {
    return new SettingsViewModel_Factory(preferencesManagerProvider, localeManagerProvider);
  }

  public static SettingsViewModel newInstance(SharedPreferencesManager preferencesManager,
      LocaleManager localeManager) {
    return new SettingsViewModel(preferencesManager, localeManager);
  }
}
