package ml.parshev.healthcontrol;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by mparshev on 03.11.15.
 */
public class HealthDb extends DatabaseContentProvider {

    public static final String AUTHORITY = "ml.parshev.healthcontrol";
    public static Uri CONTENT_URI = getContentUri(AUTHORITY);

    public static final int MEASURE_TYPE_WEIGHT = 1;
    public static final int MEASURE_TYPE_SUGAR = 2;
    public static final int MEASURE_TYPE_PRESSURE = 3;

    public static final String PREF_HEIGHT = "pref_height";
    public static final String PREF_WEIGHT = "pref_weight";

    private static final String DATABASE_NAME = "HealthDb";
    private static final int DATABASE_VERSION = 1;

    public static final class MEASURES {
        public static final String _TABLE = "measures";
        public static final Uri _URI = getTableUri(AUTHORITY, _TABLE);

        public static final String _ID = BaseColumns._ID;
        public static final String TIME = "measure_time";
        public static final String TYPE = "measure_type";
        public static final String TITLE = "measure_title";
        public static final String VALUE = "measure_value";
        public static final String COLOR = "measure_color";
        public static final String TEXT = "measure_text";

        public static String _CREATE_SQL = getCreateSQL(_TABLE, new String[] {
                TIME, TYPE_INTEGER,
                TYPE, TYPE_INTEGER,
                TITLE, TYPE_TEXT,
                VALUE, TYPE_TEXT,
                COLOR, TYPE_INTEGER,
                TEXT, TYPE_TEXT
                });

        public static ContentValues getContentValues(Date date, int type, String title, String value, int color, String text) {
            ContentValues values = new ContentValues();
            values.put(TIME, date.getTime());
            values.put(TYPE, type);
            values.put(TITLE, title);
            values.put(VALUE, value);
            values.put(COLOR, color);
            values.put(TEXT, text);
            return values;
        }
    }

    public static final int MEASURES_QUERY = 1;
    public static final int MEASURES_ROW_QUERY = 2;

    static {
        sUriMatcher.addURI(AUTHORITY, MEASURES._TABLE, MEASURES_QUERY);
        sUriMatcher.addURI(AUTHORITY, MEASURES._TABLE + "/#", MEASURES_ROW_QUERY);
    }

    public HealthDb() {
        super(AUTHORITY, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public void onCreateDatabase(SQLiteDatabase db) {
        db.execSQL(MEASURES._CREATE_SQL);
    }

    @Override
    public void onUpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + MEASURES._TABLE);
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch(sUriMatcher.match(uri)) {
            case MEASURES_QUERY:
                Cursor cursor = getReadableDatabase().query(MEASURES._TABLE, projection,
                        selection, selectionArgs, null, null, sortOrder);
                if(cursor != null)
                    cursor.setNotificationUri(getContext().getContentResolver(),CONTENT_URI);
                return cursor;
            case MEASURES_ROW_QUERY:
                return getReadableDatabase().query(MEASURES._TABLE, projection,
                        BaseColumns._ID + "=" + uri.getLastPathSegment(), null, null, null, null);
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case MEASURES_QUERY:
                long id = getWritableDatabase().insert(MEASURES._TABLE, null, values);
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                return Uri.withAppendedPath(uri, "" + id);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case MEASURES_ROW_QUERY:
                int rows = getWritableDatabase().update(MEASURES._TABLE, values,
                        BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                return rows;
        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case MEASURES_ROW_QUERY:
                int rows = getWritableDatabase().delete(MEASURES._TABLE,
                        BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                return rows;
        }
        return 0;
    }

}
