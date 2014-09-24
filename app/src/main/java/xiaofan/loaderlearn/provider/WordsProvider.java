package xiaofan.loaderlearn.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import static android.provider.BaseColumns._ID;
import static xiaofan.loaderlearn.provider.Constants.CONTENT_URI;
import static xiaofan.loaderlearn.provider.Constants.TABLE_NAME;

import xiaofan.loaderlearn.db.DBHelper;

/**
 * Created by zhaoyu on 2014/9/24.
 */

public class WordsProvider extends ContentProvider{

    public  static final String AUTHORITY = "xiaofan.loaderlearn";
    private static final  int WORDS = 1;
    private static final  int WORDS_ID = 2;

    /** The MIME type of a directory of words */
    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.xiaofan.loaderlearn";
    /** The MIME type of a single word*/
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.xiaofan.loaderlearn";

    private UriMatcher uriMatcher;
    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"beautiful_words",WORDS);
        uriMatcher.addURI(AUTHORITY,"beautiful_words/#",WORDS_ID);
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        if(uriMatcher.match(uri) == WORDS_ID){
            long id = Long.parseLong(uri.getPathSegments().get(1));
            selection = appendRowId(selection,id);
        }
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,orderBy);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case WORDS:
                return CONTENT_TYPE;
            case WORDS_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknow uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(uriMatcher.match(uri) != WORDS){
            throw new IllegalArgumentException("Unknown uri:" + uri);
        }
        // insert
        long id = db.insert(TABLE_NAME,null,contentValues);
        // Notify any watchers of the uri
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI,id);
        getContext().getContentResolver().notifyChange(newUri,null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case WORDS:
                count = db.delete(TABLE_NAME,selection,selectionArgs);
                break;
            case WORDS_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.delete(TABLE_NAME,appendRowId(selection,id),selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknow URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case WORDS:
                count = db.update(TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case WORDS_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                count = db.update(TABLE_NAME,contentValues,appendRowId(selection,id),selectionArgs);
                break;
            default: throw  new IllegalArgumentException("Unknow URI:" + uri);
        }
        return count;
    }

    private String appendRowId(String selection,long id){
        return _ID + "=" + id + (TextUtils.isEmpty(selection) ? "AND(" + selection + ")" : "");
    }
}
