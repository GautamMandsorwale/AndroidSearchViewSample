package com.example.mysearch;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;

import com.example.mysearch.db.TaskContract;
import com.example.mysearch.db.TaskDBHelper;

// TODO: Auto-generated Javadoc

/**
 * The Class MainActivity.
 */
public class MainActivity extends ListActivity implements
        LoaderCallbacks<Cursor> {


    private MyAdapter mMyAdapter;

    /**
     * The mTaskDBHelper.
     */
    private TaskDBHelper mTaskDBHelper;

    public static boolean mSearchQueryChanged;
    SearchView mSearchView;
    public static String mSearchQuery;
    SQLiteDatabase mSQLiteDatabase;

    String[] mDummyTextsStrArray = new String[]{
            "Christmas is a bridge.We need bridges as the river of time flows past.Today's Christmas should mean creating happy hours,For tomorrow and reliving those of yesterday."
            , "Do only one thing at a time.It is lazy people who want to do everything at once..!!"
            , "A beauTiful Dress Can Change the perSonality But,Beautiful Behavior can change the Life!!"
            , "A real experience of life, when you give lot of importance to smmeone in your life...You lose your importanc in their life..!!"
            , "If U Never Taste A Bad Apple. You Would Never Appreciate A Good Apple.Sometimes We Need To Experience Bitterness Of Life To Understand The Value Of Sweetness."
            , "A person can not achieve his real success & happiness until he learns the art of having tolerance,sacrifice,care and respect for others. "
            , "The best part of life is not just surviving, but thriving with passion & style & compassion & generosity & humor and finally with kindness."
            , "A successful man is one who can lay a firm foundation with the bricks that others throw at him. Good luck "
            , "Don't be proud if you gain. Nor be sorry if you lose."
            , "Every man has his secret sorrows which the world knows not; And often times we call a man cold when he is only sad."
            , "Life's nothing but a journey where every now and then we find ourselves in transit... waiting to fly again!"
            , "Making mistakes is better than faking perfections!"
            , "One should take every opportunity to laugh... because there is always something around the corner to make us cry!"
            , "�Be soft. Do not let the world make you hard. Do not let pain make you hate. Do not let the bitterness steal your sweetness.� "
            , "People only treat you One Way... the way you allow them!"
            , "A strong man is not The one who can lift the most weight,it is The one who can resist temptation"
            , "When it comes to teaching no one can compete with you. Happy teachers day."
            , "A wife is like a hand grenade. Remove the ring... and your house is gone!"
    };

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTaskDBHelper = new TaskDBHelper(MainActivity.this);
        mSQLiteDatabase = mTaskDBHelper.getWritableDatabase();
        if (!hasRowsinTable())
            insertIntoDB();

        mSearchView = (SearchView) findViewById(R.id.searchView1);
        initSearchView();

        updateUI();

        getLoaderManager().initLoader(TaskContract.QUERY_ID, null, this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    private boolean hasRowsinTable() {
        Cursor mCursor = mSQLiteDatabase.rawQuery("SELECT * FROM " + TaskContract.TABLE, null);
        boolean mHasRows = false;
        int mRowCount = mCursor.getCount();
        if (mRowCount > 0)  // there are rows in the table
        {
            mHasRows = true;
            return mHasRows;
        }

        return mHasRows;
    }

    private void insertIntoDB() {
        ContentValues mContentValues = null;
        for (int i = 0; i < mDummyTextsStrArray.length; i++) {
            mContentValues = new ContentValues();
            mContentValues.put(TaskContract.Columns.TASK, mDummyTextsStrArray[i]);
            if (mContentValues != null) {
                Uri uri = TaskContract.CONTENT_URI;
                getApplicationContext().getContentResolver()
                        .insert(uri, mContentValues);
            }
        }
    }

    /**
     * Update ui.
     */
    private void updateUI() {
        Uri uri = TaskContract.CONTENT_URI;
        // Cursor cursor = this.getContentResolver().query(uri, null, null,
        // null,
        // null);

        // mMyAdapter = new SimpleCursorAdapter(this, R.layout.task_view,
        // cursor,
        // new String[] { TaskContract.Columns.TASK },
        // new int[] { R.id.taskTextView }, 0);

        mMyAdapter = new MyAdapter(this);
        this.setListAdapter(mMyAdapter);

    }

    /**
     * On done button click.
     *
     * @param view the view
     */
 /*   public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String mTaskTxtView = taskTextView.getText().toString();

        Uri uri = TaskContract.CONTENT_URI;
        this.getContentResolver().delete(uri, TaskContract.Columns.TASK + "=?",
                new String[]{mTaskTxtView});

        // updateUI();
    }*/

    private void initSearchView() {
        // In version 3.0 and later, sets up and configures the ActionBar
        // SearchView
        // Retrieves the system search manager service
        final SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // Retrieves the SearchView from the search menu item
        mSearchView.setIconifiedByDefault(false);
        // int searchSrcTextId =
        // getResources().getIdentifier("android:id/search_src_text", null,
        // null);
        // EditText searchEditText = (EditText)
        // searchView.findViewById(searchSrcTextId);
        // searchEditText.setTextColor(Color.BLUE);
        // searchEditText.setHintTextColor(Color.BLUE);
        //
        // int closeButtonId =
        // getResources().getIdentifier("android:id/search_close_btn", null,
        // null);
        // ImageView closeButtonImage = (ImageView)
        // searchView.findViewById(closeButtonId);

        int mImageId = getResources().getIdentifier(
                "android:id/search_mag_icon", null, null); //$NON-NLS-1$
        ImageView mSearchHintIcon = (ImageView) mSearchView
                .findViewById(mImageId);

        // method 1: does not work persistently, because the next line
        // should be probably called after every manipulation with
        // SearchView
        // icon.setVisibility(View.GONE);

        // method 2: working code
        mSearchHintIcon.setAdjustViewBounds(true);
        mSearchHintIcon.setMaxWidth(0);
        mSearchHintIcon.setLayoutParams(new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        mSearchHintIcon.setImageDrawable(null);

        mSearchView.setOnCloseListener(new OnCloseListener() {

            @Override
            public boolean onClose() {
                // TODO Auto-generated method stub
                return false;
            }
        });

        // Assign searchable info to SearchView
        mSearchView.setSearchableInfo(mSearchManager
                .getSearchableInfo(getComponentName()));

        // Set listeners for SearchView
        mSearchView
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String queryText) {
                        // Nothing needs to happen when the user submits the
                        // search string
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Called when the action bar search text has
                        // changed. Updates
                        // the search filter, and restarts the loader to do
                        // a new query
                        // using the new search string.
                        String newFilter = !TextUtils.isEmpty(newText) ? newText
                                : null;

                        // Don't do anything if the filter is empty
                        if (mSearchQuery == null && newFilter == null) {
                            return true;
                        }

                        // Don't do anything if the new filter is the same
                        // as the current filter
                        if (mSearchQuery != null
                                && mSearchQuery.equals(newFilter)) {
                            return true;
                        }

                        // Updates current filter to new filter
                        mSearchQuery = newFilter;

                        // Restarts the loader. This triggers
                        // onCreateLoader(), which builds the
                        // necessary content Uri from mSearchTerm.
                        mSearchQueryChanged = true;
                        getLoaderManager().restartLoader(TaskContract.QUERY_ID,
                                null, MainActivity.this);

                        return true;
                    }
                });

        if (mSearchQuery != null) {
            // If mSearchQuery is already set here then this fragment is
            // being restored from a saved state and the search menu item
            // needs to be expanded and populated again.

            // Stores the search term (as it will be wiped out by
            // onQueryTextChange() when the menu item is expanded).
            final String savedSearchTerm = mSearchQuery;

            // Sets the SearchView to the previous search string
            mSearchView.setQuery(savedSearchTerm, false);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
        // If this is the loader for finding contacts in the Contacts Provider
        // (the only one supported)
        if (id == TaskContract.QUERY_ID) {
            Uri contentUri;

            // There are two types of searches, one which displays all contacts
            // and
            // one which filters contacts by a search query. If mSearchTerm is
            // set
            // then a search query has been entered and the latter should be
            // used.

            if (mSearchQuery == null) {
                // Since there's no search string, use the content URI that
                // searches the entire
                // Contacts table
                contentUri = TaskContract.CONTENT_URI;
            } else {
                // Since there's a search string, use the special content Uri
                // that searches the
                // Contacts table. The URI consists of a base Uri and the search
                // string.
                contentUri = Uri.withAppendedPath(TaskContract.FILTER_URI,
                        Uri.encode(mSearchQuery));
            }

            // Returns a new CursorLoader for querying the Contacts table. No
            // arguments are used
            // for the selection clause. The search string is either encoded
            // onto the content URI,
            // or no contacts search string is used. The other search criteria
            // are constants. See
            // the ContactsQuery interface.
            return new CursorLoader(MainActivity.this, contentUri,
                    TaskContract.PROJECTION, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor) {
        mMyAdapter.swapCursor(newCursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == TaskContract.QUERY_ID) {
            // When the loader is being reset, clear the cursor from the
            // adapter. This allows the
            // cursor resources to be freed.
            mMyAdapter.swapCursor(null);
        }

    }
}
