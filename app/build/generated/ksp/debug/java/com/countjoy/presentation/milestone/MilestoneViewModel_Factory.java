package com.countjoy.presentation.milestone;

import com.countjoy.domain.repository.MilestoneRepository;
import com.countjoy.domain.usecase.milestone.CheckMilestonesUseCase;
import com.countjoy.domain.usecase.milestone.CreateMilestonesUseCase;
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
public final class MilestoneViewModel_Factory implements Factory<MilestoneViewModel> {
  private final Provider<MilestoneRepository> milestoneRepositoryProvider;

  private final Provider<CreateMilestonesUseCase> createMilestonesUseCaseProvider;

  private final Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider;

  public MilestoneViewModel_Factory(Provider<MilestoneRepository> milestoneRepositoryProvider,
      Provider<CreateMilestonesUseCase> createMilestonesUseCaseProvider,
      Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider) {
    this.milestoneRepositoryProvider = milestoneRepositoryProvider;
    this.createMilestonesUseCaseProvider = createMilestonesUseCaseProvider;
    this.checkMilestonesUseCaseProvider = checkMilestonesUseCaseProvider;
  }

  @Override
  public MilestoneViewModel get() {
    return newInstance(milestoneRepositoryProvider.get(), createMilestonesUseCaseProvider.get(), checkMilestonesUseCaseProvider.get());
  }

  public static MilestoneViewModel_Factory create(
      Provider<MilestoneRepository> milestoneRepositoryProvider,
      Provider<CreateMilestonesUseCase> createMilestonesUseCaseProvider,
      Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider) {
    return new MilestoneViewModel_Factory(milestoneRepositoryProvider, createMilestonesUseCaseProvider, checkMilestonesUseCaseProvider);
  }

  public static MilestoneViewModel newInstance(MilestoneRepository milestoneRepository,
      CreateMilestonesUseCase createMilestonesUseCase,
      CheckMilestonesUseCase checkMilestonesUseCase) {
    return new MilestoneViewModel(milestoneRepository, createMilestonesUseCase, checkMilestonesUseCase);
  }
}
