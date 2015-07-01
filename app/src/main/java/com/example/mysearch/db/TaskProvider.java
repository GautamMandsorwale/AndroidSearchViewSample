package com.example.mysearch.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TaskProvider extends ContentProvider {

    private SQLiteDatabase db;
    private TaskDBHelper taskDBHelper;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.TABLE, TaskContract.TASKS_LIST);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.TABLE + "/#", TaskContract.TASKS_ITEM);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.TABLE + "/filter/*", TaskContract.TASKS_ITEM);
    }

    @Override
    public boolean onCreate() {
        boolean ret = true;
        taskDBHelper = new TaskDBHelper(getContext());
        db = taskDBHelper.getWritableDatabase();

        if (db == null) {
            ret = false;
        }

        if (db.isReadOnly()) {
            db.close();
            db = null;
            ret = false;
        }

        return ret;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TaskContract.TABLE);

        switch (uriMatcher.match(uri)) {
            case TaskContract.TASKS_LIST:
                break;

            case TaskContract.TASKS_ITEM:
                selection = "task LIKE ?";
                selectionArgs = new String[]{"%" + uri.getLastPathSegment() + "%"};
//                qb.appendWhere(TaskContract.Columns._ID + " = "+ uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, null);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case TaskContract.TASKS_LIST:
                return TaskContract.CONTENT_TYPE;

            case TaskContract.TASKS_ITEM:
                return TaskContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (uriMatcher.match(uri) != TaskContract.TASKS_LIST) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        long id = db.insert(TaskContract.TABLE, null, contentValues);

        if (id > 0) {
            return ContentUris.withAppendedId(uri, id);
        }
        throw new SQLException("Error inserting into table: " + TaskContract.TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deleted = 0;

        switch (uriMatcher.match(uri)) {
            case TaskContract.TASKS_LIST:
                db.delete(TaskContract.TABLE, selection, selectionArgs);
                break;

            case TaskContract.TASKS_ITEM:
                String where = TaskContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!selection.isEmpty()) {
                    where += " AND " + selection;
                }

                deleted = db.delete(TaskContract.TABLE, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        int updated = 0;

        switch (uriMatcher.match(uri)) {
            case TaskContract.TASKS_LIST:
                db.update(TaskContract.TABLE, contentValues, s, strings);
                break;

            case TaskContract.TASKS_ITEM:
                String where = TaskContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!s.isEmpty()) {
                    where += " AND " + s;
                }
                updated = db.update(TaskContract.TABLE, contentValues, where, strings);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return updated;
    }
}
