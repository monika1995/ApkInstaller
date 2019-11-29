package app.fcm.imageparser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class LoadImage extends AsyncTask<Void, Void, Void> {
    private Map<String, Bitmap> bitmap;
    private ImageDownloader imageDownloader;
    private ArrayList<String> imageURL;
    private WeakReference<Context> reference;

    public LoadImage(Context ctx, ArrayList<String> url, ImageDownloader downloader) {
        this.reference = new WeakReference<>(ctx);
        this.imageDownloader = downloader;
        this.bitmap = new HashMap<>();
        this.imageURL = url;
    }

    public LoadImage(Context ctx, String imageURl, ImageDownloader downloader) {
        this.reference = new WeakReference<>(ctx);
        this.imageDownloader = downloader;
        this.bitmap = new HashMap<>();
        this.imageURL = new ArrayList<>();
        this.imageURL.add(imageURl);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < this.imageURL.size(); i++) {
            try {
                if (!this.bitmap.containsKey(this.imageURL.get(i))) {
                    this.bitmap.put(this.imageURL.get(i), BitmapFactory.decodeStream((InputStream) new URL((String) this.imageURL.get(i)).getContent()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.imageDownloader.onImageDownload(this.bitmap);
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    public void startDownload() {
        execute(new Void[0]);
    }
}
