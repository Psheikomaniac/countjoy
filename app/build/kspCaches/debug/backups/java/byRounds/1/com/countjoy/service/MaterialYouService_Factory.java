package com.countjoy.service;

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
public final class MaterialYouService_Factory implements Factory<MaterialYouService> {
  private final Provider<Context> contextProvider;

  public MaterialYouService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MaterialYouService get() {
    return newInstance(contextProvider.get());
  }

  public static MaterialYouService_Factory create(Provider<Context> contextProvider) {
    return new MaterialYouService_Factory(contextProvider);
  }

  public static MaterialYouService newInstance(Context context) {
    return new MaterialYouService(context);
  }
}
