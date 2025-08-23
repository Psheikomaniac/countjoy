package com.countjoy;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.countjoy.core.locale.LocaleManager;
import com.countjoy.data.local.CountJoyDatabase;
import com.countjoy.data.local.dao.CountdownEventDao;
import com.countjoy.data.local.preferences.SharedPreferencesManager;
import com.countjoy.data.repository.EventRepositoryImpl;
import com.countjoy.di.AppModule;
import com.countjoy.di.AppModule_ProvideApplicationContextFactory;
import com.countjoy.di.CoroutineModule;
import com.countjoy.di.DatabaseModule;
import com.countjoy.di.DatabaseModule_ProvideCountJoyDatabaseFactory;
import com.countjoy.di.DatabaseModule_ProvideCountdownEventDaoFactory;
import com.countjoy.di.UseCaseModule_ProvideCalculateCountdownUseCaseFactory;
import com.countjoy.di.UseCaseModule_ProvideCreateEventUseCaseFactory;
import com.countjoy.di.UseCaseModule_ProvideDeleteEventUseCaseFactory;
import com.countjoy.di.UseCaseModule_ProvideGetEventUseCaseFactory;
import com.countjoy.di.UseCaseModule_ProvideUpdateEventUseCaseFactory;
import com.countjoy.domain.usecase.CalculateCountdownUseCase;
import com.countjoy.domain.usecase.CreateEventUseCase;
import com.countjoy.domain.usecase.DeleteEventUseCase;
import com.countjoy.domain.usecase.GetEventUseCase;
import com.countjoy.domain.usecase.UpdateEventUseCase;
import com.countjoy.presentation.countdown.CountdownViewModel;
import com.countjoy.presentation.countdown.CountdownViewModel_HiltModules_KeyModule_ProvideFactory;
import com.countjoy.presentation.event.EventInputViewModel;
import com.countjoy.presentation.event.EventInputViewModel_HiltModules_KeyModule_ProvideFactory;
import com.countjoy.presentation.settings.LanguagePickerViewModel;
import com.countjoy.presentation.settings.LanguagePickerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.countjoy.presentation.settings.SettingsViewModel;
import com.countjoy.presentation.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.countjoy.service.CountdownService;
import com.countjoy.service.CountdownService_MembersInjector;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
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
public final class DaggerCountJoyApplication_HiltComponents_SingletonC {
  private DaggerCountJoyApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder appModule(AppModule appModule) {
      Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder coroutineModule(CoroutineModule coroutineModule) {
      Preconditions.checkNotNull(coroutineModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder databaseModule(DatabaseModule databaseModule) {
      Preconditions.checkNotNull(databaseModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule(
        HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule) {
      Preconditions.checkNotNull(hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule);
      return this;
    }

    public CountJoyApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements CountJoyApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public CountJoyApplication_HiltComponents.ActivityRetainedC build() {
      return new ActivityRetainedCImpl(singletonCImpl);
    }
  }

  private static final class ActivityCBuilder implements CountJoyApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements CountJoyApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements CountJoyApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements CountJoyApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements CountJoyApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements CountJoyApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public CountJoyApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends CountJoyApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends CountJoyApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends CountJoyApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends CountJoyApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(4).add(CountdownViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(EventInputViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(LanguagePickerViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesManager(instance, singletonCImpl.sharedPreferencesManagerProvider.get());
      MainActivity_MembersInjector.injectLocaleManager(instance, singletonCImpl.localeManagerProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends CountJoyApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<GetEventUseCase> provideGetEventUseCaseProvider;

    private Provider<DeleteEventUseCase> provideDeleteEventUseCaseProvider;

    private Provider<UpdateEventUseCase> provideUpdateEventUseCaseProvider;

    private Provider<CalculateCountdownUseCase> provideCalculateCountdownUseCaseProvider;

    private Provider<CountdownViewModel> countdownViewModelProvider;

    private Provider<CreateEventUseCase> provideCreateEventUseCaseProvider;

    private Provider<EventInputViewModel> eventInputViewModelProvider;

    private Provider<LanguagePickerViewModel> languagePickerViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.provideGetEventUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<GetEventUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1));
      this.provideDeleteEventUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<DeleteEventUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2));
      this.provideUpdateEventUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<UpdateEventUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3));
      this.provideCalculateCountdownUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<CalculateCountdownUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4));
      this.countdownViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.provideCreateEventUseCaseProvider = DoubleCheck.provider(new SwitchingProvider<CreateEventUseCase>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6));
      this.eventInputViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.languagePickerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
    }

    @Override
    public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, Provider<ViewModel>>newMapBuilder(4).put("com.countjoy.presentation.countdown.CountdownViewModel", ((Provider) countdownViewModelProvider)).put("com.countjoy.presentation.event.EventInputViewModel", ((Provider) eventInputViewModelProvider)).put("com.countjoy.presentation.settings.LanguagePickerViewModel", ((Provider) languagePickerViewModelProvider)).put("com.countjoy.presentation.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).build();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.countjoy.presentation.countdown.CountdownViewModel 
          return (T) new CountdownViewModel(viewModelCImpl.provideGetEventUseCaseProvider.get(), viewModelCImpl.provideDeleteEventUseCaseProvider.get(), viewModelCImpl.provideUpdateEventUseCaseProvider.get(), viewModelCImpl.provideCalculateCountdownUseCaseProvider.get());

          case 1: // com.countjoy.domain.usecase.GetEventUseCase 
          return (T) UseCaseModule_ProvideGetEventUseCaseFactory.provideGetEventUseCase(singletonCImpl.eventRepositoryImplProvider.get());

          case 2: // com.countjoy.domain.usecase.DeleteEventUseCase 
          return (T) UseCaseModule_ProvideDeleteEventUseCaseFactory.provideDeleteEventUseCase(singletonCImpl.eventRepositoryImplProvider.get());

          case 3: // com.countjoy.domain.usecase.UpdateEventUseCase 
          return (T) UseCaseModule_ProvideUpdateEventUseCaseFactory.provideUpdateEventUseCase(singletonCImpl.eventRepositoryImplProvider.get());

          case 4: // com.countjoy.domain.usecase.CalculateCountdownUseCase 
          return (T) UseCaseModule_ProvideCalculateCountdownUseCaseFactory.provideCalculateCountdownUseCase();

          case 5: // com.countjoy.presentation.event.EventInputViewModel 
          return (T) new EventInputViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.provideGetEventUseCaseProvider.get(), viewModelCImpl.provideCreateEventUseCaseProvider.get(), viewModelCImpl.provideUpdateEventUseCaseProvider.get());

          case 6: // com.countjoy.domain.usecase.CreateEventUseCase 
          return (T) UseCaseModule_ProvideCreateEventUseCaseFactory.provideCreateEventUseCase(singletonCImpl.eventRepositoryImplProvider.get());

          case 7: // com.countjoy.presentation.settings.LanguagePickerViewModel 
          return (T) new LanguagePickerViewModel(singletonCImpl.localeManagerProvider.get());

          case 8: // com.countjoy.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.sharedPreferencesManagerProvider.get(), singletonCImpl.localeManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends CountJoyApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends CountJoyApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    private GetEventUseCase getEventUseCase() {
      return new GetEventUseCase(singletonCImpl.eventRepositoryImplProvider.get());
    }

    @Override
    public void injectCountdownService(CountdownService countdownService) {
      injectCountdownService2(countdownService);
    }

    private CountdownService injectCountdownService2(CountdownService instance) {
      CountdownService_MembersInjector.injectGetEventUseCase(instance, getEventUseCase());
      CountdownService_MembersInjector.injectCalculateCountdownUseCase(instance, new CalculateCountdownUseCase());
      return instance;
    }
  }

  private static final class SingletonCImpl extends CountJoyApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SharedPreferencesManager> sharedPreferencesManagerProvider;

    private Provider<Context> provideApplicationContextProvider;

    private Provider<LocaleManager> localeManagerProvider;

    private Provider<CountJoyDatabase> provideCountJoyDatabaseProvider;

    private Provider<CountdownEventDao> provideCountdownEventDaoProvider;

    private Provider<EventRepositoryImpl> eventRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.sharedPreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<SharedPreferencesManager>(singletonCImpl, 0));
      this.provideApplicationContextProvider = DoubleCheck.provider(new SwitchingProvider<Context>(singletonCImpl, 2));
      this.localeManagerProvider = DoubleCheck.provider(new SwitchingProvider<LocaleManager>(singletonCImpl, 1));
      this.provideCountJoyDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<CountJoyDatabase>(singletonCImpl, 5));
      this.provideCountdownEventDaoProvider = DoubleCheck.provider(new SwitchingProvider<CountdownEventDao>(singletonCImpl, 4));
      this.eventRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<EventRepositoryImpl>(singletonCImpl, 3));
    }

    @Override
    public void injectCountJoyApplication(CountJoyApplication countJoyApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.countjoy.data.local.preferences.SharedPreferencesManager 
          return (T) new SharedPreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.countjoy.core.locale.LocaleManager 
          return (T) new LocaleManager(singletonCImpl.provideApplicationContextProvider.get(), singletonCImpl.sharedPreferencesManagerProvider.get());

          case 2: // android.content.Context 
          return (T) AppModule_ProvideApplicationContextFactory.provideApplicationContext(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.countjoy.data.repository.EventRepositoryImpl 
          return (T) new EventRepositoryImpl(singletonCImpl.provideCountdownEventDaoProvider.get());

          case 4: // com.countjoy.data.local.dao.CountdownEventDao 
          return (T) DatabaseModule_ProvideCountdownEventDaoFactory.provideCountdownEventDao(singletonCImpl.provideCountJoyDatabaseProvider.get());

          case 5: // com.countjoy.data.local.CountJoyDatabase 
          return (T) DatabaseModule_ProvideCountJoyDatabaseFactory.provideCountJoyDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
