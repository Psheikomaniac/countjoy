package com.countjoy;

import com.countjoy.core.locale.LocaleManager;
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
public final class CountJoyApplication_MembersInjector implements MembersInjector<CountJoyApplication> {
  private final Provider<LocaleManager> localeManagerProvider;

  public CountJoyApplication_MembersInjector(Provider<LocaleManager> localeManagerProvider) {
    this.localeManagerProvider = localeManagerProvider;
  }

  public static MembersInjector<CountJoyApplication> create(
      Provider<LocaleManager> localeManagerProvider) {
    return new CountJoyApplication_MembersInjector(localeManagerProvider);
  }

  @Override
  public void injectMembers(CountJoyApplication instance) {
    injectLocaleManager(instance, localeManagerProvider.get());
  }

  @InjectedFieldSignature("com.countjoy.CountJoyApplication.localeManager")
  public static void injectLocaleManager(CountJoyApplication instance,
      LocaleManager localeManager) {
    instance.localeManager = localeManager;
  }
}
