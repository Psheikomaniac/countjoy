package com.countjoy.presentation.analytics;

import android.content.Context;
import com.countjoy.service.AnalyticsService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<AnalyticsService> analyticsServiceProvider;

  private final Provider<Context> contextProvider;

  public AnalyticsViewModel_Factory(Provider<AnalyticsService> analyticsServiceProvider,
      Provider<Context> contextProvider) {
    this.analyticsServiceProvider = analyticsServiceProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(analyticsServiceProvider.get(), contextProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<AnalyticsService> analyticsServiceProvider, Provider<Context> contextProvider) {
    return new AnalyticsViewModel_Factory(analyticsServiceProvider, contextProvider);
  }

  public static AnalyticsViewModel newInstance(AnalyticsService analyticsService, Context context) {
    return new AnalyticsViewModel(analyticsService, context);
  }
}
