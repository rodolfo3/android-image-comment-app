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
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class ImageAddComment extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_add);
        initFields();
    }

    private String getFilename()
    {
        Bundle bundle = this.getIntent().getExtras();
        return bundle.getString("filename");
    }

    private void initFields()
    {
        ImageView iv = (ImageView) findViewById(R.id.image);
        Bitmap myBitmap = BitmapFactory.decodeFile(getFilename());
        iv.setImageBitmap(myBitmap);
    }

}
