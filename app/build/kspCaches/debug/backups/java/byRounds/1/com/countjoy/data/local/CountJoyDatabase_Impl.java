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

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `countdown_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `category` TEXT NOT NULL, `target_date_time` INTEGER NOT NULL, `reminder_enabled` INTEGER NOT NULL, `reminder_time` INTEGER, `color` INTEGER, `icon` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `is_active` INTEGER NOT NULL, `priority` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_category` ON `countdown_events` (`category`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_target_date_time` ON `countdown_events` (`target_date_time`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_countdown_events_is_active` ON `countdown_events` (`is_active`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2b8ca2b3ea7a8aee0a827636826f445c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `countdown_events`");
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
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2b8ca2b3ea7a8aee0a827636826f445c", "1b60caf939344ac9f807dbae34429b2b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "countdown_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `countdown_events`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
}
