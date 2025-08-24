package com.countjoy.di;

import com.countjoy.data.local.CountJoyDatabase;
import com.countjoy.data.local.dao.MilestoneDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideMilestoneDaoFactory implements Factory<MilestoneDao> {
  private final Provider<CountJoyDatabase> databaseProvider;

  public DatabaseModule_ProvideMilestoneDaoFactory(Provider<CountJoyDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MilestoneDao get() {
    return provideMilestoneDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMilestoneDaoFactory create(
      Provider<CountJoyDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMilestoneDaoFactory(databaseProvider);
  }

  public static MilestoneDao provideMilestoneDao(CountJoyDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMilestoneDao(database));
  }
}
