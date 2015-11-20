package ml.parshev.healthcontrol;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 *
 * Abstract ContentProvider based on SQLiteDatabase
 *
 * Created by mparshev on 04.11.15.
 */
public abstract class DatabaseContentProvider extends ContentProvider {

    public static final String  TYPE_NULL = "NULL";
    public static final String  TYPE_BLOB = "BLOB";
    public static final String  TYPE_INTEGER = "INTEGER";
    public static final String  TYPE_REAL = "REAL";
    public static final String  TYPE_TEXT = "TEXT";

    protected static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private String mAuthority;
    private String mDatabaseName;
    private int mDatabaseVersion;

    private SQLiteOpenHelper mDataHelper;


    protected DatabaseContentProvider(String authority, String databaseName, int databaseVersion) {
        mAuthority = authority;
        mDatabaseName = databaseName;
        mDatabaseVersion = databaseVersion;
    }

    protected static Uri getContentUri(String authority) {
        return Uri.parse("content://"+authority);
    }

    public static Uri getTableUri(String authority, String table) {
        return Uri.withAppendedPath(getContentUri(authority), table);
    }

    public static String getCreateSQL(String table, String[] fields) {
        String sql = "create table " + table + "("
                + BaseColumns._ID + " integer primary key autoincrement";
        for(int i = 0; i < fields.length; i += 2) {
            sql += ", " + fields[i] + " " + fields[i+1];
        }
        sql += ")";
        return sql;
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDataHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDataHelper.getWritableDatabase();
    }

    public abstract void onCreateDatabase(SQLiteDatabase db);
    public abstract void onUpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion);

    @Override
    public boolean onCreate() {
        mDataHelper = new SQLiteOpenHelper(getContext(), mDatabaseName, null, mDatabaseVersion) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                onCreateDatabase(db);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgradeDatabase(db, oldVersion, newVersion);
            }
        };
        return true;
    }

    //TODO Add default implementation of rest ContentProvider methods

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static Date getDate(Cursor cursor, String columnName) {
        return new Date(getLong(cursor, columnName));
    }

}
