package xiaofan.loaderlearn.db;

import android.content.Context;

/**
 * Created by zhaoyu on 2014/9/18.
 */
public class WordLoader extends DataLoader<Words>{

    private long mId;

    public WordLoader(Context context,long id) {
        super(context);
        this.mId = id;
    }

    @Override
    public Words loadInBackground() {
        return WordsManager.get(getContext()).getWordsById(mId);
    }

}
