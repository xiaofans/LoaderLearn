package xiaofan.loaderlearn;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import xiaofan.loaderlearn.db.DBHelper;
import xiaofan.loaderlearn.db.SQLiteCursorLoader;
import xiaofan.loaderlearn.db.WordLoader;
import xiaofan.loaderlearn.db.Words;
import xiaofan.loaderlearn.db.WordsManager;
import xiaofan.loaderlearn.provider.Constants;


public class MyActivity2 extends ActionBarActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listView;
    private WordsCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        listView = (ListView) findViewById(R.id.list);

        getSupportLoaderManager().initLoader(2,null,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(MyActivity2.this, Constants.CONTENT_URI,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        DBHelper.WordsCursor wordsCursor = new DBHelper.WordsCursor(cursor);
        cursorAdapter = new WordsCursorAdapter(MyActivity2.this,wordsCursor);
        listView.setAdapter(cursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        listView.setAdapter(null);
    }


    private static class WordsCursorAdapter extends CursorAdapter{

        private DBHelper.WordsCursor mWordsCursor;

        public WordsCursorAdapter(Context context, DBHelper.WordsCursor c) {
            super(context, c,true);
            this.mWordsCursor = c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater
                    .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Words words = mWordsCursor.getWords();
            TextView startDateTextView = (TextView)view;
            startDateTextView.setText(words.getWords());
        }
    }


}
