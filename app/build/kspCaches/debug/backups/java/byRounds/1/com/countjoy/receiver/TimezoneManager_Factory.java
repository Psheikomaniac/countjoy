package com.countjoy.receiver;

import android.content.Context;
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
public final class TimezoneManager_Factory implements Factory<TimezoneManager> {
  private final Provider<Context> contextProvider;

  public TimezoneManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TimezoneManager get() {
    return newInstance(contextProvider.get());
  }

  public static TimezoneManager_Factory create(Provider<Context> contextProvider) {
    return new TimezoneManager_Factory(contextProvider);
  }

  public static TimezoneManager newInstance(Context context) {
    return new TimezoneManager(context);
  }
}
