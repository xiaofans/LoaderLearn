package xiaofan.loaderlearn.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhaoyu on 2014/9/18.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "words.db";
    private static final int VERSION = 1;

    private static final String TABLE_WORDS = "beautiful_words";

    public DBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql = "create table " + TABLE_WORDS +"(" + "_id integer primary key autoincrement,words text)";
        database.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public static class WordsCursor extends CursorWrapper {

        public WordsCursor(Cursor cursor) {
            super(cursor);
        }

        public Words getWords(){
            Words w = new Words();
            long wordsId = getLong(getColumnIndex("_id"));
            w.setId(wordsId);
            String words = getString(getColumnIndex("words"));
            w.setWords(words);
            return w;
        }
    }

    public WordsCursor queryWords(){
        Cursor wrapped = getReadableDatabase().query(TABLE_WORDS,null,null,null,null,null,null);
        return new WordsCursor(wrapped);
    }

    public long  insertWords(Words words){
        ContentValues cv = new ContentValues();
        cv.put("words",words.getWords());
        return getWritableDatabase().insert(TABLE_WORDS,null,cv);
    }

    public WordsCursor queryWordsById(long id){
        Cursor wrapped = getWritableDatabase().query(TABLE_WORDS,null,"_id = ?",new String[]{ String.valueOf(id) },null,null,null,"1");
        return new WordsCursor(wrapped);
    }


}
