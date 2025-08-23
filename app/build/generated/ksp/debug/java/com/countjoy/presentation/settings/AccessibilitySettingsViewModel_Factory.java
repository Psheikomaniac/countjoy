package com.countjoy.presentation.settings;

import com.countjoy.core.accessibility.AccessibilityManager;
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
public final class AccessibilitySettingsViewModel_Factory implements Factory<AccessibilitySettingsViewModel> {
  private final Provider<AccessibilityManager> accessibilityManagerProvider;

  public AccessibilitySettingsViewModel_Factory(
      Provider<AccessibilityManager> accessibilityManagerProvider) {
    this.accessibilityManagerProvider = accessibilityManagerProvider;
  }

  @Override
  public AccessibilitySettingsViewModel get() {
    return newInstance(accessibilityManagerProvider.get());
  }

  public static AccessibilitySettingsViewModel_Factory create(
      Provider<AccessibilityManager> accessibilityManagerProvider) {
    return new AccessibilitySettingsViewModel_Factory(accessibilityManagerProvider);
  }

  public static AccessibilitySettingsViewModel newInstance(
      AccessibilityManager accessibilityManager) {
    return new AccessibilitySettingsViewModel(accessibilityManager);
  }
}
