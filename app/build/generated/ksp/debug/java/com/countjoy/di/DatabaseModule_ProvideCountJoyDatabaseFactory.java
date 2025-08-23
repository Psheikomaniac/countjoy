package com.countjoy.di;

import android.content.Context;
import com.countjoy.data.local.CountJoyDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCountJoyDatabaseFactory implements Factory<CountJoyDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideCountJoyDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CountJoyDatabase get() {
    return provideCountJoyDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideCountJoyDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideCountJoyDatabaseFactory(contextProvider);
  }

  public static CountJoyDatabase provideCountJoyDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCountJoyDatabase(context));
  }
}
