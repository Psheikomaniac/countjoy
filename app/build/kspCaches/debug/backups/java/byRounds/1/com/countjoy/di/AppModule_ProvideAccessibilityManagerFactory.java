package com.countjoy.di;

import android.content.Context;
import com.countjoy.core.accessibility.AccessibilityManager;
import com.countjoy.data.local.preferences.SharedPreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideAccessibilityManagerFactory implements Factory<AccessibilityManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  public AppModule_ProvideAccessibilityManagerFactory(Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    this.contextProvider = contextProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public AccessibilityManager get() {
    return provideAccessibilityManager(contextProvider.get(), preferencesManagerProvider.get());
  }

  public static AppModule_ProvideAccessibilityManagerFactory create(
      Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    return new AppModule_ProvideAccessibilityManagerFactory(contextProvider, preferencesManagerProvider);
  }

  public static AccessibilityManager provideAccessibilityManager(Context context,
      SharedPreferencesManager preferencesManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAccessibilityManager(context, preferencesManager));
  }
}
