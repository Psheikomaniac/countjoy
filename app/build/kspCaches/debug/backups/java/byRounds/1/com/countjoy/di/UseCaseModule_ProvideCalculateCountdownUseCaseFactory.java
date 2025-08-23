package com.countjoy.di;

import com.countjoy.domain.usecase.CalculateCountdownUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("dagger.hilt.android.scopes.ViewModelScoped")
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
public final class UseCaseModule_ProvideCalculateCountdownUseCaseFactory implements Factory<CalculateCountdownUseCase> {
  @Override
  public CalculateCountdownUseCase get() {
    return provideCalculateCountdownUseCase();
  }

  public static UseCaseModule_ProvideCalculateCountdownUseCaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateCountdownUseCase provideCalculateCountdownUseCase() {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideCalculateCountdownUseCase());
  }

  private static final class InstanceHolder {
    private static final UseCaseModule_ProvideCalculateCountdownUseCaseFactory INSTANCE = new UseCaseModule_ProvideCalculateCountdownUseCaseFactory();
  }
}
