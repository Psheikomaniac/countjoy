package com.countjoy.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.countjoy.domain.repository.EventRepository;
import com.countjoy.domain.usecase.milestone.CheckMilestonesUseCase;
import com.countjoy.service.MilestoneNotificationService;
import dagger.internal.DaggerGenerated;
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
public final class MilestoneCheckWorker_Factory {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider;

  private final Provider<MilestoneNotificationService> notificationServiceProvider;

  public MilestoneCheckWorker_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider,
      Provider<MilestoneNotificationService> notificationServiceProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.checkMilestonesUseCaseProvider = checkMilestonesUseCaseProvider;
    this.notificationServiceProvider = notificationServiceProvider;
  }

  public MilestoneCheckWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, eventRepositoryProvider.get(), checkMilestonesUseCaseProvider.get(), notificationServiceProvider.get());
  }

  public static MilestoneCheckWorker_Factory create(
      Provider<EventRepository> eventRepositoryProvider,
      Provider<CheckMilestonesUseCase> checkMilestonesUseCaseProvider,
      Provider<MilestoneNotificationService> notificationServiceProvider) {
    return new MilestoneCheckWorker_Factory(eventRepositoryProvider, checkMilestonesUseCaseProvider, notificationServiceProvider);
  }

  public static MilestoneCheckWorker newInstance(Context context, WorkerParameters params,
      EventRepository eventRepository, CheckMilestonesUseCase checkMilestonesUseCase,
      MilestoneNotificationService notificationService) {
    return new MilestoneCheckWorker(context, params, eventRepository, checkMilestonesUseCase, notificationService);
  }
}
