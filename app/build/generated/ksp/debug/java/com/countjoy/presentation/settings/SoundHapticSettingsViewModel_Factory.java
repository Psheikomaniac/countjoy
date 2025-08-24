package com.countjoy.presentation.settings;

import com.countjoy.data.local.preferences.SharedPreferencesManager;
import com.countjoy.service.SoundHapticService;
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
public final class SoundHapticSettingsViewModel_Factory implements Factory<SoundHapticSettingsViewModel> {
  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  private final Provider<SoundHapticService> soundHapticServiceProvider;

  public SoundHapticSettingsViewModel_Factory(
      Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<SoundHapticService> soundHapticServiceProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.soundHapticServiceProvider = soundHapticServiceProvider;
  }

  @Override
  public SoundHapticSettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get(), soundHapticServiceProvider.get());
  }

  public static SoundHapticSettingsViewModel_Factory create(
      Provider<SharedPreferencesManager> preferencesManagerProvider,
      Provider<SoundHapticService> soundHapticServiceProvider) {
    return new SoundHapticSettingsViewModel_Factory(preferencesManagerProvider, soundHapticServiceProvider);
  }

  public static SoundHapticSettingsViewModel newInstance(
      SharedPreferencesManager preferencesManager, SoundHapticService soundHapticService) {
    return new SoundHapticSettingsViewModel(preferencesManager, soundHapticService);
  }
}
