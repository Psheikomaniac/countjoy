package com.countjoy.data.repository;

import com.countjoy.data.local.dao.RecurrenceRuleDao;
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
public final class RecurrenceRuleRepositoryImpl_Factory implements Factory<RecurrenceRuleRepositoryImpl> {
  private final Provider<RecurrenceRuleDao> recurrenceRuleDaoProvider;

  public RecurrenceRuleRepositoryImpl_Factory(
      Provider<RecurrenceRuleDao> recurrenceRuleDaoProvider) {
    this.recurrenceRuleDaoProvider = recurrenceRuleDaoProvider;
  }

  @Override
  public RecurrenceRuleRepositoryImpl get() {
    return newInstance(recurrenceRuleDaoProvider.get());
  }

  public static RecurrenceRuleRepositoryImpl_Factory create(
      Provider<RecurrenceRuleDao> recurrenceRuleDaoProvider) {
    return new RecurrenceRuleRepositoryImpl_Factory(recurrenceRuleDaoProvider);
  }

  public static RecurrenceRuleRepositoryImpl newInstance(RecurrenceRuleDao recurrenceRuleDao) {
    return new RecurrenceRuleRepositoryImpl(recurrenceRuleDao);
  }
}
