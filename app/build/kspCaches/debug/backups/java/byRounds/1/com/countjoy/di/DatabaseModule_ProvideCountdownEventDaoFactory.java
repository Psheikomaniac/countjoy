package com.countjoy.di;

import com.countjoy.data.local.CountJoyDatabase;
import com.countjoy.data.local.dao.CountdownEventDao;
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
public final class DatabaseModule_ProvideCountdownEventDaoFactory implements Factory<CountdownEventDao> {
  private final Provider<CountJoyDatabase> databaseProvider;

  public DatabaseModule_ProvideCountdownEventDaoFactory(
      Provider<CountJoyDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CountdownEventDao get() {
    return provideCountdownEventDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCountdownEventDaoFactory create(
      Provider<CountJoyDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCountdownEventDaoFactory(databaseProvider);
  }

  public static CountdownEventDao provideCountdownEventDao(CountJoyDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCountdownEventDao(database));
  }
}
