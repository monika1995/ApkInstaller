package app.fcm.imageparser;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public interface ImageDownloader {

    void onImageDownload(Map<String, Bitmap> mMap);
}
