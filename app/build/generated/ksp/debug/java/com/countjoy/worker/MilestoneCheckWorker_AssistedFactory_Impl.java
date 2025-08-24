package com.countjoy.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MilestoneCheckWorker_AssistedFactory_Impl implements MilestoneCheckWorker_AssistedFactory {
  private final MilestoneCheckWorker_Factory delegateFactory;

  MilestoneCheckWorker_AssistedFactory_Impl(MilestoneCheckWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MilestoneCheckWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<MilestoneCheckWorker_AssistedFactory> create(
      MilestoneCheckWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MilestoneCheckWorker_AssistedFactory_Impl(delegateFactory));
  }
}
