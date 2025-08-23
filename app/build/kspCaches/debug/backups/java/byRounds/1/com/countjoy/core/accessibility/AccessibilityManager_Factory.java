package com.countjoy.core.accessibility;

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
public final class AccessibilityManager_Factory implements Factory<AccessibilityManager> {
  private final Provider<Context> contextProvider;

  private final Provider<SharedPreferencesManager> preferencesManagerProvider;

  public AccessibilityManager_Factory(Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    this.contextProvider = contextProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public AccessibilityManager get() {
    return newInstance(contextProvider.get(), preferencesManagerProvider.get());
  }

  public static AccessibilityManager_Factory create(Provider<Context> contextProvider,
      Provider<SharedPreferencesManager> preferencesManagerProvider) {
    return new AccessibilityManager_Factory(contextProvider, preferencesManagerProvider);
  }

  public static AccessibilityManager newInstance(Context context,
      SharedPreferencesManager preferencesManager) {
    return new AccessibilityManager(context, preferencesManager);
  }
}
