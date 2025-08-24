package com.countjoy.domain.usecase.milestone;

import com.countjoy.domain.repository.MilestoneRepository;
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
public final class CreateMilestonesUseCase_Factory implements Factory<CreateMilestonesUseCase> {
  private final Provider<MilestoneRepository> repositoryProvider;

  public CreateMilestonesUseCase_Factory(Provider<MilestoneRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateMilestonesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateMilestonesUseCase_Factory create(
      Provider<MilestoneRepository> repositoryProvider) {
    return new CreateMilestonesUseCase_Factory(repositoryProvider);
  }

  public static CreateMilestonesUseCase newInstance(MilestoneRepository repository) {
    return new CreateMilestonesUseCase(repository);
  }
}
