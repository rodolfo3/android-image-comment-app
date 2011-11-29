package droid.ImageComments;

// Log and tools
import android.util.Log;
// context
import android.content.Context;

// Database
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.Cursor;

// data wrapper
import android.content.ContentValues;

public class DBAdapter {

    // Database conf
    private static String TABLE_NAME = "comment";
    private static String DB_NAME = "PhotoComment";
    private static int DB_VERSION = 1;

    // class to create database table and encapsulate SQL
    private static class DatabaseHelper extends SQLiteOpenHelper {

        // initial conf
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        // create table
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("create table %s (%s, %s, %s, %s);",
                    TABLE_NAME,
                    "_id integer primary key autoincrement",
                    "filename text",
                    "title text",
                    "comment text"
                )
            );
        }

        // update table (is not needed now)
        public void onUpgrade(SQLiteDatabase db,
             int OldVersion, int NewVersion) {
            // do nothing (by now)
        }

    }

    // adapter
    private final Context context;
    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        context = ctx;
        dbhelper = new DatabaseHelper(context);
    }

    public DBAdapter open() throws SQLException
    {
        // open database to read and write
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() throws SQLException
    {
        dbhelper.close();
    }

    public void save(ContentValues content)
    {
        if (db == null) open();
        String filename = content.getAsString("filename");
        Log.d("DBAdapter", ">> SAVE: ");
        Log.d("DBSave", db == null ? "null" : "value!");
        // FIXME
        delete(filename);
        db.insert(TABLE_NAME, null, content);
        /*
        if (filename != null) { // FIXME
            db.insert(TABLE_NAME, null, content);
        } else {
        Log.d("DBAdapter", ">> UPDATE >> " + filename);
            db.update(TABLE_NAME, content, "filename = " + filename, null);
        }
        Log.d("DBAdapter", ">>" + filename);
        Log.d("DBAdapter", "SAVE <<");
        */
    }

    public int delete(String filename)
    {
        return db.delete(TABLE_NAME, String.format("filename = '" + filename + "'"), null);
    }

    public Cursor filter(String[] columns,
        String selection, String[] selectionArgs, String orderBy,
        String group_by)
    {
        Cursor cursor = db.query(TABLE_NAME,
            new String[] {"_id", "filename", "title", "comment"},
            null,
            null,
            null,
            null,
            null
        );
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public ContentValues get(String filename)
    {
        Cursor cursor = db.query(TABLE_NAME,
            new String[] {"_id", "filename", "title", "comment"},
            "filename = '" + filename + "'",
            null,
            null,
            null,
            null
            );
        if (cursor.moveToFirst()) {
            ContentValues content = new ContentValues();
            content.put("_id", cursor.getString(0));
            content.put("filename", cursor.getString(1));
            content.put("title", cursor.getString(2));
            content.put("comment", cursor.getString(3));
            return content;
        }
        return null;
    }

}

