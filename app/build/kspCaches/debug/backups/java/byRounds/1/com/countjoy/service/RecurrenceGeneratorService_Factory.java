package com.countjoy.service;

import com.countjoy.domain.model.RecurrenceCalculator;
import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.repository.RecurrenceRuleRepository;
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
public final class RecurrenceGeneratorService_Factory implements Factory<RecurrenceGeneratorService> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<RecurrenceRuleRepository> recurrenceRuleRepositoryProvider;

  private final Provider<RecurrenceCalculator> recurrenceCalculatorProvider;

  public RecurrenceGeneratorService_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<RecurrenceRuleRepository> recurrenceRuleRepositoryProvider,
      Provider<RecurrenceCalculator> recurrenceCalculatorProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.recurrenceRuleRepositoryProvider = recurrenceRuleRepositoryProvider;
    this.recurrenceCalculatorProvider = recurrenceCalculatorProvider;
  }

  @Override
  public RecurrenceGeneratorService get() {
    return newInstance(eventRepositoryProvider.get(), recurrenceRuleRepositoryProvider.get(), recurrenceCalculatorProvider.get());
  }

  public static RecurrenceGeneratorService_Factory create(
      Provider<EventRepository> eventRepositoryProvider,
      Provider<RecurrenceRuleRepository> recurrenceRuleRepositoryProvider,
      Provider<RecurrenceCalculator> recurrenceCalculatorProvider) {
    return new RecurrenceGeneratorService_Factory(eventRepositoryProvider, recurrenceRuleRepositoryProvider, recurrenceCalculatorProvider);
  }

  public static RecurrenceGeneratorService newInstance(EventRepository eventRepository,
      RecurrenceRuleRepository recurrenceRuleRepository,
      RecurrenceCalculator recurrenceCalculator) {
    return new RecurrenceGeneratorService(eventRepository, recurrenceRuleRepository, recurrenceCalculator);
  }
}
