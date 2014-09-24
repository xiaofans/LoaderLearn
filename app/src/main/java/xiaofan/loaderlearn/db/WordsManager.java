package xiaofan.loaderlearn.db;

import android.content.Context;
/**
 * Created by zhaoyu on 2014/9/18.
 */
public class WordsManager {

    private Context mAppContext;
    private DBHelper mHelper;

    private static WordsManager sWordsManager;


    private WordsManager(Context appContext){
        this.mAppContext = appContext;
        mHelper = new DBHelper(appContext);
    }

    public static WordsManager get(Context context){
        // we use the application context to avoid leaking activities
        if(sWordsManager == null) sWordsManager = new WordsManager(context.getApplicationContext());
        return sWordsManager;
    }


    public Words insertWords(String aWord){
        Words w = new Words();
        w.setWords(aWord);
        w.setId(mHelper.insertWords(w));
        return w;
    }


    public DBHelper.WordsCursor queryWords(){
        return mHelper.queryWords();
    }

    public Words getWordsById(long id){
        Words words = null;
        DBHelper.WordsCursor  cursor = mHelper.queryWordsById(id);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) words = cursor.getWords();
        cursor.close();
        return words;
    }
}
