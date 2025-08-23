package com.countjoy.core.locale;

import android.content.Context;
import com.countjoy.data.local.preferences.SharedPreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LocaleManager_Factory implements Factory<LocaleManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  public LocaleManager_Factory(Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    this.contextProvider = contextProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public LocaleManager get() {
    return newInstance(contextProvider.get(), preferencesManagerProvider.get());
  }

  public static LocaleManager_Factory create(Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    return new LocaleManager_Factory(contextProvider, preferencesManagerProvider);
  }

  public static LocaleManager newInstance(Context context,
      SharedPreferencesManager preferencesManager) {
    return new LocaleManager(context, preferencesManager);
  }
}
