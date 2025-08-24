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
import com.countjoy.data.local.entity.RecurrenceRuleEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
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
public final class RecurrenceRuleDao_Impl implements RecurrenceRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecurrenceRuleEntity> __insertionAdapterOfRecurrenceRuleEntity;

  private final DateTimeConverter __dateTimeConverter = new DateTimeConverter();

  private final EntityDeletionOrUpdateAdapter<RecurrenceRuleEntity> __deletionAdapterOfRecurrenceRuleEntity;

  private final EntityDeletionOrUpdateAdapter<RecurrenceRuleEntity> __updateAdapterOfRecurrenceRuleEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOccurrenceDates;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRecurrenceRuleByEventId;

  public RecurrenceRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecurrenceRuleEntity = new EntityInsertionAdapter<RecurrenceRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recurrence_rules` (`id`,`eventId`,`pattern`,`intervalValue`,`daysOfWeek`,`dayOfMonth`,`weekOfMonth`,`monthOfYear`,`endType`,`endDate`,`occurrenceCount`,`exceptions`,`skipWeekends`,`skipHolidays`,`lastOccurrenceDate`,`nextOccurrenceDate`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurrenceRuleEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEventId());
        statement.bindString(3, entity.getPattern());
        statement.bindLong(4, entity.getIntervalValue());
        if (entity.getDaysOfWeek() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDaysOfWeek());
        }
        if (entity.getDayOfMonth() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDayOfMonth());
        }
        if (entity.getWeekOfMonth() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getWeekOfMonth());
        }
        if (entity.getMonthOfYear() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getMonthOfYear());
        }
        statement.bindString(9, entity.getEndType());
        final Long _tmp = __dateTimeConverter.localDateToLong(entity.getEndDate());
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp);
        }
        if (entity.getOccurrenceCount() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getOccurrenceCount());
        }
        if (entity.getExceptions() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getExceptions());
        }
        final int _tmp_1 = entity.getSkipWeekends() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.getSkipHolidays() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        final Long _tmp_3 = __dateTimeConverter.localDateToLong(entity.getLastOccurrenceDate());
        if (_tmp_3 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_3);
        }
        final Long _tmp_4 = __dateTimeConverter.localDateToLong(entity.getNextOccurrenceDate());
        if (_tmp_4 == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfRecurrenceRuleEntity = new EntityDeletionOrUpdateAdapter<RecurrenceRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recurrence_rules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurrenceRuleEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecurrenceRuleEntity = new EntityDeletionOrUpdateAdapter<RecurrenceRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recurrence_rules` SET `id` = ?,`eventId` = ?,`pattern` = ?,`intervalValue` = ?,`daysOfWeek` = ?,`dayOfMonth` = ?,`weekOfMonth` = ?,`monthOfYear` = ?,`endType` = ?,`endDate` = ?,`occurrenceCount` = ?,`exceptions` = ?,`skipWeekends` = ?,`skipHolidays` = ?,`lastOccurrenceDate` = ?,`nextOccurrenceDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurrenceRuleEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEventId());
        statement.bindString(3, entity.getPattern());
        statement.bindLong(4, entity.getIntervalValue());
        if (entity.getDaysOfWeek() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDaysOfWeek());
        }
        if (entity.getDayOfMonth() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDayOfMonth());
        }
        if (entity.getWeekOfMonth() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getWeekOfMonth());
        }
        if (entity.getMonthOfYear() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getMonthOfYear());
        }
        statement.bindString(9, entity.getEndType());
        final Long _tmp = __dateTimeConverter.localDateToLong(entity.getEndDate());
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp);
        }
        if (entity.getOccurrenceCount() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getOccurrenceCount());
        }
        if (entity.getExceptions() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getExceptions());
        }
        final int _tmp_1 = entity.getSkipWeekends() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.getSkipHolidays() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        final Long _tmp_3 = __dateTimeConverter.localDateToLong(entity.getLastOccurrenceDate());
        if (_tmp_3 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_3);
        }
        final Long _tmp_4 = __dateTimeConverter.localDateToLong(entity.getNextOccurrenceDate());
        if (_tmp_4 == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, _tmp_4);
        }
        statement.bindString(17, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateOccurrenceDates = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE recurrence_rules SET lastOccurrenceDate = ?, nextOccurrenceDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteRecurrenceRuleByEventId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recurrence_rules WHERE eventId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecurrenceRule(final RecurrenceRuleEntity rule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecurrenceRuleEntity.insert(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecurrenceRule(final RecurrenceRuleEntity rule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecurrenceRuleEntity.handle(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRecurrenceRule(final RecurrenceRuleEntity rule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecurrenceRuleEntity.handle(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOccurrenceDates(final String ruleId, final LocalDate lastDate,
      final LocalDate nextDate, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOccurrenceDates.acquire();
        int _argIndex = 1;
        final Long _tmp = __dateTimeConverter.localDateToLong(lastDate);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 2;
        final Long _tmp_1 = __dateTimeConverter.localDateToLong(nextDate);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, ruleId);
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
          __preparedStmtOfUpdateOccurrenceDates.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecurrenceRuleByEventId(final String eventId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRecurrenceRuleByEventId.acquire();
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
          __preparedStmtOfDeleteRecurrenceRuleByEventId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecurrenceRuleByEventId(final String eventId,
      final Continuation<? super RecurrenceRuleEntity> $completion) {
    final String _sql = "SELECT * FROM recurrence_rules WHERE eventId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, eventId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RecurrenceRuleEntity>() {
      @Override
      @Nullable
      public RecurrenceRuleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfDayOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfMonth");
          final int _cursorIndexOfWeekOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfMonth");
          final int _cursorIndexOfMonthOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "monthOfYear");
          final int _cursorIndexOfEndType = CursorUtil.getColumnIndexOrThrow(_cursor, "endType");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "exceptions");
          final int _cursorIndexOfSkipWeekends = CursorUtil.getColumnIndexOrThrow(_cursor, "skipWeekends");
          final int _cursorIndexOfSkipHolidays = CursorUtil.getColumnIndexOrThrow(_cursor, "skipHolidays");
          final int _cursorIndexOfLastOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastOccurrenceDate");
          final int _cursorIndexOfNextOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextOccurrenceDate");
          final RecurrenceRuleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final String _tmpDaysOfWeek;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmpDaysOfWeek = null;
            } else {
              _tmpDaysOfWeek = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            final Integer _tmpDayOfMonth;
            if (_cursor.isNull(_cursorIndexOfDayOfMonth)) {
              _tmpDayOfMonth = null;
            } else {
              _tmpDayOfMonth = _cursor.getInt(_cursorIndexOfDayOfMonth);
            }
            final Integer _tmpWeekOfMonth;
            if (_cursor.isNull(_cursorIndexOfWeekOfMonth)) {
              _tmpWeekOfMonth = null;
            } else {
              _tmpWeekOfMonth = _cursor.getInt(_cursorIndexOfWeekOfMonth);
            }
            final Integer _tmpMonthOfYear;
            if (_cursor.isNull(_cursorIndexOfMonthOfYear)) {
              _tmpMonthOfYear = null;
            } else {
              _tmpMonthOfYear = _cursor.getInt(_cursorIndexOfMonthOfYear);
            }
            final String _tmpEndType;
            _tmpEndType = _cursor.getString(_cursorIndexOfEndType);
            final LocalDate _tmpEndDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __dateTimeConverter.fromLocalDate(_tmp);
            final Integer _tmpOccurrenceCount;
            if (_cursor.isNull(_cursorIndexOfOccurrenceCount)) {
              _tmpOccurrenceCount = null;
            } else {
              _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            }
            final String _tmpExceptions;
            if (_cursor.isNull(_cursorIndexOfExceptions)) {
              _tmpExceptions = null;
            } else {
              _tmpExceptions = _cursor.getString(_cursorIndexOfExceptions);
            }
            final boolean _tmpSkipWeekends;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSkipWeekends);
            _tmpSkipWeekends = _tmp_1 != 0;
            final boolean _tmpSkipHolidays;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSkipHolidays);
            _tmpSkipHolidays = _tmp_2 != 0;
            final LocalDate _tmpLastOccurrenceDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLastOccurrenceDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfLastOccurrenceDate);
            }
            _tmpLastOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_3);
            final LocalDate _tmpNextOccurrenceDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfNextOccurrenceDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfNextOccurrenceDate);
            }
            _tmpNextOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_4);
            _result = new RecurrenceRuleEntity(_tmpId,_tmpEventId,_tmpPattern,_tmpIntervalValue,_tmpDaysOfWeek,_tmpDayOfMonth,_tmpWeekOfMonth,_tmpMonthOfYear,_tmpEndType,_tmpEndDate,_tmpOccurrenceCount,_tmpExceptions,_tmpSkipWeekends,_tmpSkipHolidays,_tmpLastOccurrenceDate,_tmpNextOccurrenceDate);
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
  public Flow<RecurrenceRuleEntity> observeRecurrenceRuleByEventId(final String eventId) {
    final String _sql = "SELECT * FROM recurrence_rules WHERE eventId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, eventId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recurrence_rules"}, new Callable<RecurrenceRuleEntity>() {
      @Override
      @Nullable
      public RecurrenceRuleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfDayOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfMonth");
          final int _cursorIndexOfWeekOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfMonth");
          final int _cursorIndexOfMonthOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "monthOfYear");
          final int _cursorIndexOfEndType = CursorUtil.getColumnIndexOrThrow(_cursor, "endType");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "exceptions");
          final int _cursorIndexOfSkipWeekends = CursorUtil.getColumnIndexOrThrow(_cursor, "skipWeekends");
          final int _cursorIndexOfSkipHolidays = CursorUtil.getColumnIndexOrThrow(_cursor, "skipHolidays");
          final int _cursorIndexOfLastOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastOccurrenceDate");
          final int _cursorIndexOfNextOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextOccurrenceDate");
          final RecurrenceRuleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final String _tmpDaysOfWeek;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmpDaysOfWeek = null;
            } else {
              _tmpDaysOfWeek = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            final Integer _tmpDayOfMonth;
            if (_cursor.isNull(_cursorIndexOfDayOfMonth)) {
              _tmpDayOfMonth = null;
            } else {
              _tmpDayOfMonth = _cursor.getInt(_cursorIndexOfDayOfMonth);
            }
            final Integer _tmpWeekOfMonth;
            if (_cursor.isNull(_cursorIndexOfWeekOfMonth)) {
              _tmpWeekOfMonth = null;
            } else {
              _tmpWeekOfMonth = _cursor.getInt(_cursorIndexOfWeekOfMonth);
            }
            final Integer _tmpMonthOfYear;
            if (_cursor.isNull(_cursorIndexOfMonthOfYear)) {
              _tmpMonthOfYear = null;
            } else {
              _tmpMonthOfYear = _cursor.getInt(_cursorIndexOfMonthOfYear);
            }
            final String _tmpEndType;
            _tmpEndType = _cursor.getString(_cursorIndexOfEndType);
            final LocalDate _tmpEndDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __dateTimeConverter.fromLocalDate(_tmp);
            final Integer _tmpOccurrenceCount;
            if (_cursor.isNull(_cursorIndexOfOccurrenceCount)) {
              _tmpOccurrenceCount = null;
            } else {
              _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            }
            final String _tmpExceptions;
            if (_cursor.isNull(_cursorIndexOfExceptions)) {
              _tmpExceptions = null;
            } else {
              _tmpExceptions = _cursor.getString(_cursorIndexOfExceptions);
            }
            final boolean _tmpSkipWeekends;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSkipWeekends);
            _tmpSkipWeekends = _tmp_1 != 0;
            final boolean _tmpSkipHolidays;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSkipHolidays);
            _tmpSkipHolidays = _tmp_2 != 0;
            final LocalDate _tmpLastOccurrenceDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLastOccurrenceDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfLastOccurrenceDate);
            }
            _tmpLastOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_3);
            final LocalDate _tmpNextOccurrenceDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfNextOccurrenceDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfNextOccurrenceDate);
            }
            _tmpNextOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_4);
            _result = new RecurrenceRuleEntity(_tmpId,_tmpEventId,_tmpPattern,_tmpIntervalValue,_tmpDaysOfWeek,_tmpDayOfMonth,_tmpWeekOfMonth,_tmpMonthOfYear,_tmpEndType,_tmpEndDate,_tmpOccurrenceCount,_tmpExceptions,_tmpSkipWeekends,_tmpSkipHolidays,_tmpLastOccurrenceDate,_tmpNextOccurrenceDate);
          } else {
            _result = null;
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
  public Object getAllRecurrenceRules(
      final Continuation<? super List<RecurrenceRuleEntity>> $completion) {
    final String _sql = "SELECT * FROM recurrence_rules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecurrenceRuleEntity>>() {
      @Override
      @NonNull
      public List<RecurrenceRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfDayOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfMonth");
          final int _cursorIndexOfWeekOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfMonth");
          final int _cursorIndexOfMonthOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "monthOfYear");
          final int _cursorIndexOfEndType = CursorUtil.getColumnIndexOrThrow(_cursor, "endType");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "exceptions");
          final int _cursorIndexOfSkipWeekends = CursorUtil.getColumnIndexOrThrow(_cursor, "skipWeekends");
          final int _cursorIndexOfSkipHolidays = CursorUtil.getColumnIndexOrThrow(_cursor, "skipHolidays");
          final int _cursorIndexOfLastOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastOccurrenceDate");
          final int _cursorIndexOfNextOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextOccurrenceDate");
          final List<RecurrenceRuleEntity> _result = new ArrayList<RecurrenceRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurrenceRuleEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final String _tmpDaysOfWeek;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmpDaysOfWeek = null;
            } else {
              _tmpDaysOfWeek = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            final Integer _tmpDayOfMonth;
            if (_cursor.isNull(_cursorIndexOfDayOfMonth)) {
              _tmpDayOfMonth = null;
            } else {
              _tmpDayOfMonth = _cursor.getInt(_cursorIndexOfDayOfMonth);
            }
            final Integer _tmpWeekOfMonth;
            if (_cursor.isNull(_cursorIndexOfWeekOfMonth)) {
              _tmpWeekOfMonth = null;
            } else {
              _tmpWeekOfMonth = _cursor.getInt(_cursorIndexOfWeekOfMonth);
            }
            final Integer _tmpMonthOfYear;
            if (_cursor.isNull(_cursorIndexOfMonthOfYear)) {
              _tmpMonthOfYear = null;
            } else {
              _tmpMonthOfYear = _cursor.getInt(_cursorIndexOfMonthOfYear);
            }
            final String _tmpEndType;
            _tmpEndType = _cursor.getString(_cursorIndexOfEndType);
            final LocalDate _tmpEndDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __dateTimeConverter.fromLocalDate(_tmp);
            final Integer _tmpOccurrenceCount;
            if (_cursor.isNull(_cursorIndexOfOccurrenceCount)) {
              _tmpOccurrenceCount = null;
            } else {
              _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            }
            final String _tmpExceptions;
            if (_cursor.isNull(_cursorIndexOfExceptions)) {
              _tmpExceptions = null;
            } else {
              _tmpExceptions = _cursor.getString(_cursorIndexOfExceptions);
            }
            final boolean _tmpSkipWeekends;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSkipWeekends);
            _tmpSkipWeekends = _tmp_1 != 0;
            final boolean _tmpSkipHolidays;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSkipHolidays);
            _tmpSkipHolidays = _tmp_2 != 0;
            final LocalDate _tmpLastOccurrenceDate;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfLastOccurrenceDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfLastOccurrenceDate);
            }
            _tmpLastOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_3);
            final LocalDate _tmpNextOccurrenceDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfNextOccurrenceDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfNextOccurrenceDate);
            }
            _tmpNextOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_4);
            _item = new RecurrenceRuleEntity(_tmpId,_tmpEventId,_tmpPattern,_tmpIntervalValue,_tmpDaysOfWeek,_tmpDayOfMonth,_tmpWeekOfMonth,_tmpMonthOfYear,_tmpEndType,_tmpEndDate,_tmpOccurrenceCount,_tmpExceptions,_tmpSkipWeekends,_tmpSkipHolidays,_tmpLastOccurrenceDate,_tmpNextOccurrenceDate);
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
  public Object getRecurrenceRulesDueBy(final LocalDate date,
      final Continuation<? super List<RecurrenceRuleEntity>> $completion) {
    final String _sql = "SELECT * FROM recurrence_rules WHERE nextOccurrenceDate <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __dateTimeConverter.localDateToLong(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecurrenceRuleEntity>>() {
      @Override
      @NonNull
      public List<RecurrenceRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfIntervalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalValue");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfDayOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfMonth");
          final int _cursorIndexOfWeekOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfMonth");
          final int _cursorIndexOfMonthOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "monthOfYear");
          final int _cursorIndexOfEndType = CursorUtil.getColumnIndexOrThrow(_cursor, "endType");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfOccurrenceCount = CursorUtil.getColumnIndexOrThrow(_cursor, "occurrenceCount");
          final int _cursorIndexOfExceptions = CursorUtil.getColumnIndexOrThrow(_cursor, "exceptions");
          final int _cursorIndexOfSkipWeekends = CursorUtil.getColumnIndexOrThrow(_cursor, "skipWeekends");
          final int _cursorIndexOfSkipHolidays = CursorUtil.getColumnIndexOrThrow(_cursor, "skipHolidays");
          final int _cursorIndexOfLastOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastOccurrenceDate");
          final int _cursorIndexOfNextOccurrenceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextOccurrenceDate");
          final List<RecurrenceRuleEntity> _result = new ArrayList<RecurrenceRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurrenceRuleEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEventId;
            _tmpEventId = _cursor.getString(_cursorIndexOfEventId);
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final int _tmpIntervalValue;
            _tmpIntervalValue = _cursor.getInt(_cursorIndexOfIntervalValue);
            final String _tmpDaysOfWeek;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmpDaysOfWeek = null;
            } else {
              _tmpDaysOfWeek = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            final Integer _tmpDayOfMonth;
            if (_cursor.isNull(_cursorIndexOfDayOfMonth)) {
              _tmpDayOfMonth = null;
            } else {
              _tmpDayOfMonth = _cursor.getInt(_cursorIndexOfDayOfMonth);
            }
            final Integer _tmpWeekOfMonth;
            if (_cursor.isNull(_cursorIndexOfWeekOfMonth)) {
              _tmpWeekOfMonth = null;
            } else {
              _tmpWeekOfMonth = _cursor.getInt(_cursorIndexOfWeekOfMonth);
            }
            final Integer _tmpMonthOfYear;
            if (_cursor.isNull(_cursorIndexOfMonthOfYear)) {
              _tmpMonthOfYear = null;
            } else {
              _tmpMonthOfYear = _cursor.getInt(_cursorIndexOfMonthOfYear);
            }
            final String _tmpEndType;
            _tmpEndType = _cursor.getString(_cursorIndexOfEndType);
            final LocalDate _tmpEndDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __dateTimeConverter.fromLocalDate(_tmp_1);
            final Integer _tmpOccurrenceCount;
            if (_cursor.isNull(_cursorIndexOfOccurrenceCount)) {
              _tmpOccurrenceCount = null;
            } else {
              _tmpOccurrenceCount = _cursor.getInt(_cursorIndexOfOccurrenceCount);
            }
            final String _tmpExceptions;
            if (_cursor.isNull(_cursorIndexOfExceptions)) {
              _tmpExceptions = null;
            } else {
              _tmpExceptions = _cursor.getString(_cursorIndexOfExceptions);
            }
            final boolean _tmpSkipWeekends;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSkipWeekends);
            _tmpSkipWeekends = _tmp_2 != 0;
            final boolean _tmpSkipHolidays;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfSkipHolidays);
            _tmpSkipHolidays = _tmp_3 != 0;
            final LocalDate _tmpLastOccurrenceDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfLastOccurrenceDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfLastOccurrenceDate);
            }
            _tmpLastOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_4);
            final LocalDate _tmpNextOccurrenceDate;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfNextOccurrenceDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfNextOccurrenceDate);
            }
            _tmpNextOccurrenceDate = __dateTimeConverter.fromLocalDate(_tmp_5);
            _item = new RecurrenceRuleEntity(_tmpId,_tmpEventId,_tmpPattern,_tmpIntervalValue,_tmpDaysOfWeek,_tmpDayOfMonth,_tmpWeekOfMonth,_tmpMonthOfYear,_tmpEndType,_tmpEndDate,_tmpOccurrenceCount,_tmpExceptions,_tmpSkipWeekends,_tmpSkipHolidays,_tmpLastOccurrenceDate,_tmpNextOccurrenceDate);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
