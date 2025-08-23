package com.countjoy.presentation.countdown;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CountdownUpdateManager_Factory implements Factory<CountdownUpdateManager> {
  @Override
  public CountdownUpdateManager get() {
    return newInstance();
  }

  public static CountdownUpdateManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CountdownUpdateManager newInstance() {
    return new CountdownUpdateManager();
  }

  private static final class InstanceHolder {
    private static final CountdownUpdateManager_Factory INSTANCE = new CountdownUpdateManager_Factory();
  }
}
