package xiaofan.loaderlearn.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zhaoyu on 2014/9/24.
 */
public interface Constants extends BaseColumns{
    public static final String TABLE_NAME = "beautiful_words";
    public static final String AUTHORITY = "xiaofan.loaderlearn";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final String WORDS = "words";


}
