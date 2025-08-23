package com.countjoy.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateCountdownUseCase_Factory implements Factory<CalculateCountdownUseCase> {
  @Override
  public CalculateCountdownUseCase get() {
    return newInstance();
  }

  public static CalculateCountdownUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateCountdownUseCase newInstance() {
    return new CalculateCountdownUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateCountdownUseCase_Factory INSTANCE = new CalculateCountdownUseCase_Factory();
  }
}
