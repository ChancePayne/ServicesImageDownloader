package com.lambdaschool.serviceimagedownloader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

// S03M04-1 create new service
public class LargeImageDownloadService extends Service {

    public static final String FILE_DOWNLOADED_ACTION = "com.lambdaschool.serviceimagedownloader.FILE_DOWNLOADED";
    public static final String DOWNLOADED_IMAGE = "downloaded_image";

    private static final String TAG = "LargeImageDownloadServi";

    public LargeImageDownloadService() {
        Log.i(TAG, "in constructor");
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "in onCreate");
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "in onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "in onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     *
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     *
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override // S03M04-2 Override this method
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final Context context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // S03M04-3 Add network call
                final Bitmap bitmap = NetworkAdapter.getBitmapFromUrl("https://www.photolib.noaa.gov/bigs/amer0006.jpg");

                Intent imageBroadcast = new Intent(FILE_DOWNLOADED_ACTION);
                imageBroadcast.putExtra(DOWNLOADED_IMAGE, bitmap);
                LocalBroadcastManager.getInstance(context).sendBroadcast(imageBroadcast);

                stopSelf();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
