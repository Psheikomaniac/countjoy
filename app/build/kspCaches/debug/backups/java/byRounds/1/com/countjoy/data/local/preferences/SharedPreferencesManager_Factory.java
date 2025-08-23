package com.countjoy.data.local.preferences;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SharedPreferencesManager_Factory implements Factory<SharedPreferencesManager> {
  private final Provider<Context> contextProvider;

  public SharedPreferencesManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SharedPreferencesManager get() {
    return newInstance(contextProvider.get());
  }

  public static SharedPreferencesManager_Factory create(Provider<Context> contextProvider) {
    return new SharedPreferencesManager_Factory(contextProvider);
  }

  public static SharedPreferencesManager newInstance(Context context) {
    return new SharedPreferencesManager(context);
  }
}
