package com.countjoy.domain.usecase.milestone;

import com.countjoy.domain.repository.EventRepository;
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
public final class CheckMilestonesUseCase_Factory implements Factory<CheckMilestonesUseCase> {
  private final Provider<MilestoneRepository> milestoneRepositoryProvider;

  private final Provider<EventRepository> eventRepositoryProvider;

  public CheckMilestonesUseCase_Factory(Provider<MilestoneRepository> milestoneRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider) {
    this.milestoneRepositoryProvider = milestoneRepositoryProvider;
    this.eventRepositoryProvider = eventRepositoryProvider;
  }

  @Override
  public CheckMilestonesUseCase get() {
    return newInstance(milestoneRepositoryProvider.get(), eventRepositoryProvider.get());
  }

  public static CheckMilestonesUseCase_Factory create(
      Provider<MilestoneRepository> milestoneRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider) {
    return new CheckMilestonesUseCase_Factory(milestoneRepositoryProvider, eventRepositoryProvider);
  }

  public static CheckMilestonesUseCase newInstance(MilestoneRepository milestoneRepository,
      EventRepository eventRepository) {
    return new CheckMilestonesUseCase(milestoneRepository, eventRepository);
  }
}
