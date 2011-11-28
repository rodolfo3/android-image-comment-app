package droid.ImageComments;

import android.app.Activity;
import android.os.Bundle;

// interface with service
import android.content.ServiceConnection;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;

// util
import android.util.Log;

// MIX
import java.lang.StringBuilder;

// widgets
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

public class ImageComments extends Activity
{
    private SDCardServiceInterface SDCard;
    private static int ADD_RESULT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        this.bindService(
            new Intent(ImageComments.this, SDCardService.class),
            sConnection,
            Context.BIND_AUTO_CREATE
        );
    }

    protected void fillList()
    {
        List<HashMap<String, String>> fillMaps =
            new ArrayList<HashMap<String, String>>();

        try
        {
            for (String filename: SDCard.listImages())
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("filename", filename);
                fillMaps.add(map);
            }
        } catch (RemoteException exce) {
            // FIXME
            Log.d("APP", "Remote Exception: " + exce.getMessage());
        }

        String[] from = new String[] {
            "filename"
        };
        int[] to = new int[] {
            R.id.filename
        };

        SimpleAdapter adapter = new SimpleAdapter(
                this, fillMaps, R.layout.comment_item, from, to
            );
        ListView lv = (ListView) findViewById(R.id.image_list);
        lv.setAdapter(adapter);
    }

    private void installEvents()
    {
        ListView lv = (ListView) findViewById(R.id.image_list);
        lv.setOnItemClickListener(
            new ListView.OnItemClickListener()
            {
                public void onItemClick(AdapterView parent,
                    View v,
                    int position,
                    long id)
                {
                    String filename = null;
                    try
                    {
                        String [] list = SDCard.listImages();
                        filename = list[position];
                        comment(filename);
                    } catch (RemoteException exce) {
                        // FIXME
                        Log.d("APP", "Remote Exception: " + exce.getMessage());
                    }
                }
            }
        );
    }

    public void comment(String filename)
    {
        Intent intent = new Intent(getApplicationContext(),
            ImageAddComment.class);
        intent.putExtra("filename", filename);
        startActivityForResult(intent, ADD_RESULT);
    }

    private ServiceConnection sConnection = new ServiceConnection()
    {
        public void onServiceConnected(
            ComponentName className, IBinder service)
        {
            SDCard = SDCardServiceInterface.Stub.asInterface((IBinder)service);
            Log.d("APP", "Service starded and connected!");
            fillList();
            installEvents();
        }
        public void onServiceDisconnected(
            ComponentName className)
        {
            SDCard = null;
            Log.d("APP", "Service disconnected!");
        }
    };

}
