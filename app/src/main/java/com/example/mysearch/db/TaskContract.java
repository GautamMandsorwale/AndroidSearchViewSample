package com.example.mysearch.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.mycontentprov.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";
    public static final String AUTHORITY = "com.example.mycontentprov.mytasks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int TASKS_LIST = 1;
    public static final int TASKS_ITEM = 2;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/example.tasksDB/" + TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/example/tasksDB/" + TABLE;
    public static final int QUERY_ID = 0;

    /**
     * The content:// style URI used for "type-to-filter" functionality on the
     * {@link #CONTENT_URI} URI. The filter string will be used to match
     * various parts of the contact name. The filter argument should be passed
     * as an additional path segment after this URI.
     */
    public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(
            CONTENT_URI, "filter");

    // The search/filter query Uri
    public final static Uri FILTER_URI = CONTENT_FILTER_URI;

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }

    public final static String[] PROJECTION = {

            // The contact's row id
            Columns._ID,

            // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
            // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
            // a "permanent" contact URI.
            Columns.TASK,

    };
}
