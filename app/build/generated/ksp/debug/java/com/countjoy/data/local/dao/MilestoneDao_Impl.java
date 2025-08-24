package com.countjoy.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.countjoy.data.local.converter.DateTimeConverter;
import com.countjoy.data.local.entity.MilestoneEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MilestoneDao_Impl implements MilestoneDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MilestoneEntity> __insertionAdapterOfMilestoneEntity;

  private final DateTimeConverter __dateTimeConverter = new DateTimeConverter();

  private final EntityDeletionOrUpdateAdapter<MilestoneEntity> __deletionAdapterOfMilestoneEntity;

  private final EntityDeletionOrUpdateAdapter<MilestoneEntity> __updateAdapterOfMilestoneEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkMilestoneAsAchieved;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMilestonesByEventId;

  public MilestoneDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMilestoneEntity = new EntityInsertionAdapter<MilestoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `milestones` (`id`,`eventId`,`type`,`value`,`title`,`message`,`isNotificationEnabled`,`isAchieved`,`achievedAt`,`celebrationEffect`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MilestoneEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEventId());
        statement.bindString(3, entity.getType());
        statement.bindDouble(4, entity.getValue());
        statement.bindString(5, entity.getTitle());
        statement.bindString(6, entity.getMessage());
        final int _tmp = entity.isNotificationEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isAchieved() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final Long _tmp_2 = __dateTimeConverter.instantToTimestamp(entity.getAchievedAt());
        if (_tmp_2 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_2);
        }
        statement.bindString(10, entity.getCelebrationEffect());
      }
    };
    this.__deletionAdapterOfMilestoneEntity = new EntityDeletionOrUpdateAdapter<MilestoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `milestones` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MilestoneEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfMilestoneEntity = new EntityDeletionOrUpdateAdapter<MilestoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `milestones` SET `id` = ?,`eventId` = ?,`type` = ?,`value` = ?,`title` = ?,`message` = ?,`isNotificationEnabled` = ?,`isAchieved` = ?,`achievedAt` = ?,`celebrationEffect` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MilestoneEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEventId());
        statement.bindString(3, entity.getType());
        statement.bindDouble(4, entity.getValue());
        statement.bindString(5, entity.getTitle());
        statement.bindString(6, entity.getMessage());
        final int _tmp = entity.isNotificationEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isAchieved() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final Long _tmp_2 = __dateTimeConverter.instantToTimestamp(entity.getAchievedAt());
        if (_tmp_2 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_2);
        }
        statement.bindString(10, entity.getCelebrationEffect());
        statement.bindString(11, entity.getId());
      }
    };
    this.__preparedStmtOfMarkMilestoneAsAchieved = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE milestones SET isAchieved = 1, achievedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMilestonesByEventId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM milestones WHERE eventId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMilestone(final MilestoneEntity milestone,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMilestoneEntity.insert(milestone);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMilestones(final List<MilestoneEntity> milestones,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMilestoneEntity.insert(milestones);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMilestone(final MilestoneEntity milestone,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMilestoneEntity.handle(milestone);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMilestone(final MilestoneEntity milestone,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMilestoneEntity.handle(milestone);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markMilestoneAsAchieved(final String milestoneId, final long achievedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkMilestoneAsAchieved.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, achievedAt);
        _argIndex = 2;
        _stmt.bindString(_argIndex, milestoneId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkMilestoneAsAchieved.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMilestonesByEventId(final String eventId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMilestonesByEventId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, eventId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteMilestonesByEventId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MilestoneEntity>> getMilestonesByEventId(final String eventId) {
    final String _sql = "SELECT * FROM milestones WHERE eventId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, eventId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"milestones"}, new Callable<List<MilestoneEntity>>() {
      @Override
      @NonNull
      public List<MilestoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isNotificationEnabled");
          final int _cursorIndexOfIsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "isAchieved");
          final int _cursorIndexOfAchievedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "achievedAt");
          final int _cursorIndexOfCelebrationEffect = CursorUtil.getColumnIndexOrThrow(_cursor, "celebrationEffect");
          final List<MilestoneEntity> _result = new ArrayList<MilestoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MilestoneEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final float _tmpValue;
            _tmpValue = _cursor.getFloat(_cursorIndexOfValue);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsNotificationEnabled);
            _tmpIsNotificationEnabled = _tmp != 0;
            final boolean _tmpIsAchieved;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAchieved);
            _tmpIsAchieved = _tmp_1 != 0;
            final Instant _tmpAchievedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfAchievedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfAchievedAt);
            }
            _tmpAchievedAt = __dateTimeConverter.fromInstant(_tmp_2);
            final String _tmpCelebrationEffect;
            _tmpCelebrationEffect = _cursor.getString(_cursorIndexOfCelebrationEffect);
            _item = new MilestoneEntity(_tmpId,_tmpEventId,_tmpType,_tmpValue,_tmpTitle,_tmpMessage,_tmpIsNotificationEnabled,_tmpIsAchieved,_tmpAchievedAt,_tmpCelebrationEffect);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnachievedMilestones(final String eventId,
      final Continuation<? super List<MilestoneEntity>> $completion) {
    final String _sql = "SELECT * FROM milestones WHERE eventId = ? AND isAchieved = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, eventId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MilestoneEntity>>() {
      @Override
      @NonNull
      public List<MilestoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isNotificationEnabled");
          final int _cursorIndexOfIsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "isAchieved");
          final int _cursorIndexOfAchievedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "achievedAt");
          final int _cursorIndexOfCelebrationEffect = CursorUtil.getColumnIndexOrThrow(_cursor, "celebrationEffect");
          final List<MilestoneEntity> _result = new ArrayList<MilestoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MilestoneEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final float _tmpValue;
            _tmpValue = _cursor.getFloat(_cursorIndexOfValue);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsNotificationEnabled);
            _tmpIsNotificationEnabled = _tmp != 0;
            final boolean _tmpIsAchieved;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAchieved);
            _tmpIsAchieved = _tmp_1 != 0;
            final Instant _tmpAchievedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfAchievedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfAchievedAt);
            }
            _tmpAchievedAt = __dateTimeConverter.fromInstant(_tmp_2);
            final String _tmpCelebrationEffect;
            _tmpCelebrationEffect = _cursor.getString(_cursorIndexOfCelebrationEffect);
            _item = new MilestoneEntity(_tmpId,_tmpEventId,_tmpType,_tmpValue,_tmpTitle,_tmpMessage,_tmpIsNotificationEnabled,_tmpIsAchieved,_tmpAchievedAt,_tmpCelebrationEffect);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMilestoneById(final String milestoneId,
      final Continuation<? super MilestoneEntity> $completion) {
    final String _sql = "SELECT * FROM milestones WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, milestoneId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MilestoneEntity>() {
      @Override
      @Nullable
      public MilestoneEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isNotificationEnabled");
          final int _cursorIndexOfIsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "isAchieved");
          final int _cursorIndexOfAchievedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "achievedAt");
          final int _cursorIndexOfCelebrationEffect = CursorUtil.getColumnIndexOrThrow(_cursor, "celebrationEffect");
          final MilestoneEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final float _tmpValue;
            _tmpValue = _cursor.getFloat(_cursorIndexOfValue);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsNotificationEnabled);
            _tmpIsNotificationEnabled = _tmp != 0;
            final boolean _tmpIsAchieved;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAchieved);
            _tmpIsAchieved = _tmp_1 != 0;
            final Instant _tmpAchievedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfAchievedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfAchievedAt);
            }
            _tmpAchievedAt = __dateTimeConverter.fromInstant(_tmp_2);
            final String _tmpCelebrationEffect;
            _tmpCelebrationEffect = _cursor.getString(_cursorIndexOfCelebrationEffect);
            _result = new MilestoneEntity(_tmpId,_tmpEventId,_tmpType,_tmpValue,_tmpTitle,_tmpMessage,_tmpIsNotificationEnabled,_tmpIsAchieved,_tmpAchievedAt,_tmpCelebrationEffect);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MilestoneEntity>> getAchievementHistory() {
    final String _sql = "SELECT * FROM milestones WHERE isAchieved = 1 ORDER BY achievedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"milestones"}, new Callable<List<MilestoneEntity>>() {
      @Override
      @NonNull
      public List<MilestoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isNotificationEnabled");
          final int _cursorIndexOfIsAchieved = CursorUtil.getColumnIndexOrThrow(_cursor, "isAchieved");
          final int _cursorIndexOfAchievedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "achievedAt");
          final int _cursorIndexOfCelebrationEffect = CursorUtil.getColumnIndexOrThrow(_cursor, "celebrationEffect");
          final List<MilestoneEntity> _result = new ArrayList<MilestoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MilestoneEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final float _tmpValue;
            _tmpValue = _cursor.getFloat(_cursorIndexOfValue);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsNotificationEnabled);
            _tmpIsNotificationEnabled = _tmp != 0;
            final boolean _tmpIsAchieved;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAchieved);
            _tmpIsAchieved = _tmp_1 != 0;
            final Instant _tmpAchievedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfAchievedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfAchievedAt);
            }
            _tmpAchievedAt = __dateTimeConverter.fromInstant(_tmp_2);
            final String _tmpCelebrationEffect;
            _tmpCelebrationEffect = _cursor.getString(_cursorIndexOfCelebrationEffect);
            _item = new MilestoneEntity(_tmpId,_tmpEventId,_tmpType,_tmpValue,_tmpTitle,_tmpMessage,_tmpIsNotificationEnabled,_tmpIsAchieved,_tmpAchievedAt,_tmpCelebrationEffect);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
