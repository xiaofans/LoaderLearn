package xiaofan.loaderlearn.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by zhaoyu on 2014/9/18.
 * SQLiteCursorLoader implements the AsyncTaskLoader API to efficiently load and hold a Cursor in the mCursor instance variable.
 The loadInBackground() method calls the abstract loadCursor() method to get the Cursor and calls the getCount() method on
 the cursor to ensure that the data is available in memory once it is passed over to the main thread.
 The deliverResult(Cursor) method takes care of two things. If the loader is started (which means the data can be delivered),
 the superclass implementation of deliverResult(…) is called. If the old cursor is no longer needed, it is closed to free up its
 resources. Because an existing cursor may be cached and redelivered, it is important to make sure that the old cursor and the
 new cursor are not the same before the old cursor is closed.
 The remaining method implementations are not critical to understand for the purposes of RunTracker, but you can find more
 details in the API documentation for AsyncTaskLoader.
 With this base class implemented, you can now implement a very simple subclass, RunListCursorLoader, in RunListFragment as
 an inner class.
 */

public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;

    protected abstract Cursor loadCursor();

    public SQLiteCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if(cursor != null){
            // Ensure that the content window is filled
            cursor.getCount();
        }
        return cursor;
    }


    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;
        if(isStarted()){
            super.deliverResult(data);
        }

        if(oldCursor != null && oldCursor != data && !oldCursor.isClosed()){
            oldCursor.close();
        }

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mCursor != null){
            deliverResult(mCursor);
        }

        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
       cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        if(data != null && !data.isClosed()){
            data.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        if (mCursor != null && !mCursor.isClosed()){
            mCursor.close();
        }

        mCursor = null;
    }
}
