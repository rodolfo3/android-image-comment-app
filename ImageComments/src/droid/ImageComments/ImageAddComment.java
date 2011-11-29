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
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import android.widget.ImageView;
import android.net.Uri;

// data wrapper
import android.content.ContentValues;

public class ImageAddComment extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_add);
        initFields();
        installEvents();
    }

    private String getFilename()
    {
        Bundle bundle = this.getIntent().getExtras();
        return bundle.getString("filename");
    }

    private void initFields()
    {
        Log.d("APP::Add", getFilename());
        ImageView iv = (ImageView) findViewById(R.id.image);
        Uri uri = Uri.parse(getFilename());
        iv.setImageURI(uri);
    }

    protected ContentValues getContent()
    {
        ContentValues content = new ContentValues();
        EditText aux;

        content.put("filename", getFilename());

        aux = (EditText) findViewById(R.id.title);
        content.put("title", aux.getText().toString());

        aux = (EditText) findViewById(R.id.comment);
        content.put("comment", aux.getText().toString());

        return content;
    }

    protected void installEvents()
    {
        Button btn = (Button) findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });

    }

    public void save()
    {
        // E/AndroidRuntime(12351): java.lang.NullPointerException
        Log.d("APP:Add", "save");
        DBAdapter db; // database wrapper
        db = new DBAdapter(this);
        db.open();
        db.save(getContent());
        db.close();

        Toast.makeText(getApplicationContext(),
            "Saved!", 10).show();
    }

    public void load(ContentValues content)
    {
        EditText aux;

        aux = (EditText) findViewById(R.id.title);
        aux.setText((CharSequence) content.get("title"));

        aux = (EditText) findViewById(R.id.comment);
        aux.setText((CharSequence) content.get("comment"));
    }

}
