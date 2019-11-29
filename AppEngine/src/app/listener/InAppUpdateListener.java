package app.listener;

/**
 * Created by Meenu Singh on 2019-08-29.
 */
public interface InAppUpdateListener {

    void onUpdateAvailable();

    void onUpdateNotAvailable();
}
