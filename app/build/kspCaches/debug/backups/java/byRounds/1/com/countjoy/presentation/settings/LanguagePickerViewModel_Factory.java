package com.countjoy.presentation.settings;

import com.countjoy.core.locale.LocaleManager;
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
public final class LanguagePickerViewModel_Factory implements Factory<LanguagePickerViewModel> {
  private final Provider<LocaleManager> localeManagerProvider;

  public LanguagePickerViewModel_Factory(Provider<LocaleManager> localeManagerProvider) {
    this.localeManagerProvider = localeManagerProvider;
  }

  @Override
  public LanguagePickerViewModel get() {
    return newInstance(localeManagerProvider.get());
  }

  public static LanguagePickerViewModel_Factory create(
      Provider<LocaleManager> localeManagerProvider) {
    return new LanguagePickerViewModel_Factory(localeManagerProvider);
  }

  public static LanguagePickerViewModel newInstance(LocaleManager localeManager) {
    return new LanguagePickerViewModel(localeManager);
  }
}
