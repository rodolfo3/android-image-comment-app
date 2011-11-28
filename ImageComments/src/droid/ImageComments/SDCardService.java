package droid.ImageComments;

// To create a service
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;

// util
import java.util.ArrayList;
import java.util.List;

// file operation
import android.os.Environment;
import java.io.File;
import android.os.FileObserver;
import java.io.FilenameFilter;
import android.util.Log;

public class SDCardService extends Service
{
    private List<String> images = new ArrayList<String>();
    private FileObserver _observer;

    private final SDCardServiceInterface.Stub mBinder =
        new SDCardServiceInterface.Stub() {
            public String [] listImages()
            {
                return images.toArray(new String[] {});
            }
        };

    public void onCreate()
    {
        super.onCreate();
        Log.d("SERVICE", "onCreate");
        this.updateImagesList();
        this.startObserver();
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d("SERVICE", "onDestroy");
    }

    private void updateImagesList()
    {
        File home = Environment.getExternalStorageDirectory();
        File fileList[] = home.listFiles(new ImageFilter());

        Log.d("SERVICE", "populating list...");
        if (fileList != null)
        {
            List<String> list_images = new ArrayList<String>();
            for (File file : fileList )
            {
                list_images.add(file.getAbsolutePath());
            }
            this.images = list_images;
        }
    }

    public IBinder onBind(Intent intent)
    {
        return this.mBinder;
    }

    private void startObserver()
    {
        File home = Environment.getExternalStorageDirectory();

        Log.d("SERVICE", "Starting observer at: " + home.getAbsolutePath());
        FileObserver observer =
            new FileObserver(home.getAbsolutePath()
                ) {
                //FileObserver.CREATE & FileObserver.DELETE) {
            public void onEvent(int event, String path)
            {
                Log.d("SERVICE", "event into file: " + path);
                if (! path.endsWith(".jpg")) return; //FIXME Use ImageFilter
                switch(event)
                {
                    case FileObserver.CREATE:
                            images.add(path);
                        break;
                    case FileObserver.DELETE:
                            images.remove(path);
                        break;
                }
            }
        };
        observer.startWatching();
        Log.d("SERVICE", "Watching...");
        this._observer = observer;
    }
}

class ImageFilter implements FilenameFilter
{
    public boolean accept(File dir, String name) {
        return (name.endsWith(".jpg")); //TODO .png, .gif, etc.
    }
}
