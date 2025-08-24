package com.countjoy.data.repository;

import com.countjoy.data.local.dao.MilestoneDao;
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
public final class MilestoneRepositoryImpl_Factory implements Factory<MilestoneRepositoryImpl> {
  private final Provider<MilestoneDao> milestoneDaoProvider;

  public MilestoneRepositoryImpl_Factory(Provider<MilestoneDao> milestoneDaoProvider) {
    this.milestoneDaoProvider = milestoneDaoProvider;
  }

  @Override
  public MilestoneRepositoryImpl get() {
    return newInstance(milestoneDaoProvider.get());
  }

  public static MilestoneRepositoryImpl_Factory create(
      Provider<MilestoneDao> milestoneDaoProvider) {
    return new MilestoneRepositoryImpl_Factory(milestoneDaoProvider);
  }

  public static MilestoneRepositoryImpl newInstance(MilestoneDao milestoneDao) {
    return new MilestoneRepositoryImpl(milestoneDao);
  }
}
