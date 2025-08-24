package com.countjoy.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.countjoy.data.local.dao.CountdownEventDao;
import com.countjoy.data.local.dao.CountdownEventDao_Impl;
import com.countjoy.data.local.dao.MilestoneDao;
import com.countjoy.data.local.dao.MilestoneDao_Impl;
import com.countjoy.data.local.dao.RecurrenceRuleDao;
import com.countjoy.data.local.dao.RecurrenceRuleDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CountJoyDatabase_Impl extends CountJoyDatabase {
  private volatile CountdownEventDao _countdownEventDao;

  private volatile MilestoneDao _milestoneDao;

  private volatile RecurrenceRuleDao _recurrenceRuleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `countdown_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `category` TEXT NOT NULL, `target_date_time` INTEGER NOT NULL, `reminder_enabled` INTEGER NOT NULL, `reminder_time` INTEGER, `color` INTEGER, `icon` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `is_active` INTEGER NOT NULL, `priority` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_category` ON `countdown_events` (`category`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_target_date_time` ON `countdown_events` (`target_date_time`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_is_active` ON `countdown_events` (`is_active`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `milestones` (`id` TEXT NOT NULL, `eventId` TEXT NOT NULL, `type` TEXT NOT NULL, `value` REAL NOT NULL, `title` TEXT NOT NULL, `message` TEXT NOT NULL, `isNotificationEnabled` INTEGER NOT NULL, `isAchieved` INTEGER NOT NULL, `achievedAt` INTEGER, `celebrationEffect` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`eventId`) REFERENCES `countdown_events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_eventId` ON `milestones` (`eventId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recurrence_rules` (`id` TEXT NOT NULL, `eventId` TEXT NOT NULL, `pattern` TEXT NOT NULL, `intervalValue` INTEGER NOT NULL, `daysOfWeek` TEXT, `dayOfMonth` INTEGER, `weekOfMonth` INTEGER, `monthOfYear` INTEGER, `endType` TEXT NOT NULL, `endDate` INTEGER, `occurrenceCount` INTEGER, `exceptions` TEXT, `skipWeekends` INTEGER NOT NULL, `skipHolidays` INTEGER NOT NULL, `lastOccurrenceDate` INTEGER, `nextOccurrenceDate` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`eventId`) REFERENCES `countdown_events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurrence_rules_eventId` ON `recurrence_rules` (`eventId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '36a311e77b72ba281dc1ad88244bd385')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `countdown_events`");
        db.execSQL("DROP TABLE IF EXISTS `milestones`");
        db.execSQL("DROP TABLE IF EXISTS `recurrence_rules`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCountdownEvents = new HashMap<String, TableInfo.Column>(13);
        _columnsCountdownEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("target_date_time", new TableInfo.Column("target_date_time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("reminder_enabled", new TableInfo.Column("reminder_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("reminder_time", new TableInfo.Column("reminder_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("color", new TableInfo.Column("color", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("icon", new TableInfo.Column("icon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountdownEvents.put("priority", new TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCountdownEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCountdownEvents = new HashSet<TableInfo.Index>(3);
        _indicesCountdownEvents.add(new TableInfo.Index("index_countdown_events_category", false, Arrays.asList("category"), Arrays.asList("ASC")));
        _indicesCountdownEvents.add(new TableInfo.Index("index_countdown_events_target_date_time", false, Arrays.asList("target_date_time"), Arrays.asList("ASC")));
        _indicesCountdownEvents.add(new TableInfo.Index("index_countdown_events_is_active", false, Arrays.asList("is_active"), Arrays.asList("ASC")));
        final TableInfo _infoCountdownEvents = new TableInfo("countdown_events", _columnsCountdownEvents, _foreignKeysCountdownEvents, _indicesCountdownEvents);
        final TableInfo _existingCountdownEvents = TableInfo.read(db, "countdown_events");
        if (!_infoCountdownEvents.equals(_existingCountdownEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "countdown_events(com.countjoy.data.local.entity.CountdownEventEntity).\n"
                  + " Expected:\n" + _infoCountdownEvents + "\n"
                  + " Found:\n" + _existingCountdownEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsMilestones = new HashMap<String, TableInfo.Column>(10);
        _columnsMilestones.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("eventId", new TableInfo.Column("eventId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("isNotificationEnabled", new TableInfo.Column("isNotificationEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("isAchieved", new TableInfo.Column("isAchieved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("achievedAt", new TableInfo.Column("achievedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("celebrationEffect", new TableInfo.Column("celebrationEffect", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMilestones = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMilestones.add(new TableInfo.ForeignKey("countdown_events", "CASCADE", "NO ACTION", Arrays.asList("eventId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMilestones = new HashSet<TableInfo.Index>(1);
        _indicesMilestones.add(new TableInfo.Index("index_milestones_eventId", false, Arrays.asList("eventId"), Arrays.asList("ASC")));
        final TableInfo _infoMilestones = new TableInfo("milestones", _columnsMilestones, _foreignKeysMilestones, _indicesMilestones);
        final TableInfo _existingMilestones = TableInfo.read(db, "milestones");
        if (!_infoMilestones.equals(_existingMilestones)) {
          return new RoomOpenHelper.ValidationResult(false, "milestones(com.countjoy.data.local.entity.MilestoneEntity).\n"
                  + " Expected:\n" + _infoMilestones + "\n"
                  + " Found:\n" + _existingMilestones);
        }
        final HashMap<String, TableInfo.Column> _columnsRecurrenceRules = new HashMap<String, TableInfo.Column>(16);
        _columnsRecurrenceRules.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("eventId", new TableInfo.Column("eventId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("pattern", new TableInfo.Column("pattern", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("intervalValue", new TableInfo.Column("intervalValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("daysOfWeek", new TableInfo.Column("daysOfWeek", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("dayOfMonth", new TableInfo.Column("dayOfMonth", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("weekOfMonth", new TableInfo.Column("weekOfMonth", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("monthOfYear", new TableInfo.Column("monthOfYear", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("endType", new TableInfo.Column("endType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("occurrenceCount", new TableInfo.Column("occurrenceCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("exceptions", new TableInfo.Column("exceptions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("skipWeekends", new TableInfo.Column("skipWeekends", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("skipHolidays", new TableInfo.Column("skipHolidays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("lastOccurrenceDate", new TableInfo.Column("lastOccurrenceDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurrenceRules.put("nextOccurrenceDate", new TableInfo.Column("nextOccurrenceDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecurrenceRules = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRecurrenceRules.add(new TableInfo.ForeignKey("countdown_events", "CASCADE", "NO ACTION", Arrays.asList("eventId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRecurrenceRules = new HashSet<TableInfo.Index>(1);
        _indicesRecurrenceRules.add(new TableInfo.Index("index_recurrence_rules_eventId", false, Arrays.asList("eventId"), Arrays.asList("ASC")));
        final TableInfo _infoRecurrenceRules = new TableInfo("recurrence_rules", _columnsRecurrenceRules, _foreignKeysRecurrenceRules, _indicesRecurrenceRules);
        final TableInfo _existingRecurrenceRules = TableInfo.read(db, "recurrence_rules");
        if (!_infoRecurrenceRules.equals(_existingRecurrenceRules)) {
          return new RoomOpenHelper.ValidationResult(false, "recurrence_rules(com.countjoy.data.local.entity.RecurrenceRuleEntity).\n"
                  + " Expected:\n" + _infoRecurrenceRules + "\n"
                  + " Found:\n" + _existingRecurrenceRules);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "36a311e77b72ba281dc1ad88244bd385", "249f8fa3e7467b3265c17fa38f7c9bd4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "countdown_events","milestones","recurrence_rules");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `countdown_events`");
      _db.execSQL("DELETE FROM `milestones`");
      _db.execSQL("DELETE FROM `recurrence_rules`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CountdownEventDao.class, CountdownEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MilestoneDao.class, MilestoneDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecurrenceRuleDao.class, RecurrenceRuleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CountdownEventDao countdownEventDao() {
    if (_countdownEventDao != null) {
      return _countdownEventDao;
    } else {
      synchronized(this) {
        if(_countdownEventDao == null) {
          _countdownEventDao = new CountdownEventDao_Impl(this);
        }
        return _countdownEventDao;
      }
    }
  }

  @Override
  public MilestoneDao milestoneDao() {
    if (_milestoneDao != null) {
      return _milestoneDao;
    } else {
      synchronized(this) {
        if(_milestoneDao == null) {
          _milestoneDao = new MilestoneDao_Impl(this);
        }
        return _milestoneDao;
      }
    }
  }

  @Override
  public RecurrenceRuleDao recurrenceRuleDao() {
    if (_recurrenceRuleDao != null) {
      return _recurrenceRuleDao;
    } else {
      synchronized(this) {
        if(_recurrenceRuleDao == null) {
          _recurrenceRuleDao = new RecurrenceRuleDao_Impl(this);
        }
        return _recurrenceRuleDao;
      }
    }
  }
}
