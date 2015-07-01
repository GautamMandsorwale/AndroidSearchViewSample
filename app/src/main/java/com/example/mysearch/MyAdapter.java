package com.example.mysearch;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {
	private LayoutInflater mInflater; // Stores the layout inflater
	Context mContext;
	private TextAppearanceSpan mHighlightTextSpan; // Stores the highlight

	// text appearance style

	/**
	 * Instantiates a new Contacts Adapter.
	 * 
	 * @param context
	 *            A context that has access to the app's layout.
	 */
	public MyAdapter(Context context) {
		super(context, null, 0);

		// Stores inflater for use later
		mInflater = LayoutInflater.from(context);

		mContext = context;
		// Defines a span for highlighting the part of a display name that
		// matches the search
		// string
		mHighlightTextSpan = new TextAppearanceSpan(mContext,
				R.style.searchTextHiglight);
	}

	/**
	 * Overrides newView() to inflate the list item views.
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		// Inflates the list item layout.
		final View itemLayout = mInflater.inflate(R.layout.task_view,
				viewGroup, false);

		// Creates a new ViewHolder in which to store handles to each view
		// resource. This
		// allows bindView() to retrieve stored references instead of calling
		// findViewById for
		// each instance of the layout.
		final ViewHolder mHolder = new ViewHolder();
		mHolder.mTaskTxtView = (TextView) itemLayout.findViewById(R.id.taskTextView);

		// Stores the resourceHolder instance in itemLayout. This makes
		// resourceHolder
		// available to bindView and other methods that receive a handle to the
		// item view.
		itemLayout.setTag(mHolder);

		// Returns the item layout view
		return itemLayout;
	}

	/**
	 * Binds data from the Cursor to the provided view.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Gets handles to individual view resources
		final ViewHolder holder = (ViewHolder) view.getTag();

		String mTask = cursor.getString(1);
		// Toast.makeText(mContext, "TASK-> "+cursor.getString(1),
		// Toast.LENGTH_SHORT).show();

		final int mStartIndex = indexOfSearchQuery(mTask);

		if (mStartIndex == -1) {
			// If the user didn't do a search, or the search string didn't
			// match a display
			// name, show the mTask name without highlighting
			holder.mTaskTxtView.setText(mTask);

		} else {
			// If the search string matched the mTask name, applies a
			// SpannableString to
			// highlight the search string with the displayed display name

			// Wraps the display name in the SpannableString
			final SpannableString mHighlightedName = new SpannableString(mTask);

			// Sets the span to start at the starting point of the match and
			// end at "length"
			// characters beyond the starting point
			mHighlightedName.setSpan(mHighlightTextSpan, mStartIndex, mStartIndex
					+ MainActivity.mSearchQuery.length(), 0);

			// Binds the SpannableString to the display name View object
			holder.mTaskTxtView.setText(mHighlightedName);
		}

	}

	/**
	 * Overrides swapCursor to move the new Cursor into the CursorAdapter.
	 */
	@Override
	public Cursor swapCursor(Cursor newCursor) {
		return super.swapCursor(newCursor);
	}

	/**
	 * An override of getCount that simplifies accessing the Cursor. If the
	 * Cursor is null, getCount returns zero. As a result, no test for Cursor ==
	 * null is needed.
	 */
	@Override
	public int getCount() {
		if (getCursor() == null) {
			return 0;
		}
		return super.getCount();
	}

	/**
	 * Identifies the start of the search string in the display name column of a
	 * Cursor row. E.g. If displayName was "Adam" and search query (mSearchTerm)
	 * was "da" this would return 1.
	 *
	 * @param displayName
	 *            The contact display name.
	 * @return The starting position of the search string in the display name,
	 *         0-based. The method returns -1 if the string is not found in the
	 *         display name, or if the search string is empty or null.
	 */
	private int indexOfSearchQuery(String displayName) {
		if (!TextUtils.isEmpty(MainActivity.mSearchQuery)) {
			return displayName.toLowerCase(Locale.getDefault()).indexOf(
					MainActivity.mSearchQuery.toLowerCase(Locale.getDefault()));
		}
		return -1;
	}

	/**
	 * A class that defines fields for each resource ID in the list item layout.
	 * This allows ContactsAdapter.newView() to store the IDs once, when it
	 * inflates the layout, instead of calling findViewById in each iteration of
	 * bindView.
	 */
	private class ViewHolder {
		TextView mTaskTxtView;
	}

}
