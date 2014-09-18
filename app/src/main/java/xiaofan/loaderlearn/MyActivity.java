package xiaofan.loaderlearn;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class MyActivity extends ActionBarActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listView;
    private WordsCursorAdapter cursorAdapter;
    private WordsManager wordsManager;

    private Button insertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putLong("id",1);
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(1,args,new WordsLoaderCallbacks());
            }
        });
        wordsManager = WordsManager.get(this);
       // cursorAdapter = new WordsCursorAdapter(this,wordsManager.queryWords());
       // listView.setAdapter(cursorAdapter);

        insertBtn = (Button) findViewById(R.id.insert_btn);
        insertBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                wordsManager.insertWords("目子子善");
            }
        });

        getSupportLoaderManager().initLoader(2,null,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Restart the loader to get any new run available
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new WordsListCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursorAdapter = new WordsCursorAdapter(this,(DBHelper.WordsCursor)wordsManager.queryWords());
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
            // Set up the start date text view
            TextView startDateTextView = (TextView)view;
            startDateTextView.setText(words.getWords());
        }
    }


    private static class WordsListCursorLoader extends SQLiteCursorLoader{

        public WordsListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            // Query the list of runs
            return WordsManager.get(getContext()).queryWords();
        }
    }

    private class WordsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Words>{

        @Override
        public Loader<Words> onCreateLoader(int i, Bundle bundle) {
            return new WordLoader(MyActivity.this,bundle.getLong("id"));
        }

        @Override
        public void onLoadFinished(Loader<Words> wordsLoader, Words words) {
            Toast.makeText(MyActivity.this,"click words text is:" + words.getWords(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoaderReset(Loader<Words> wordsLoader) {

        }
    }
}
