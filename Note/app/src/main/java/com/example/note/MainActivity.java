package com.example.note;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button textbtn,imgbtn,videobtn;
    private ListView lv;
    Intent intent;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        lv = (ListView)findViewById(R.id.list);
        textbtn = (Button)findViewById(R.id.text);
        imgbtn = (Button)findViewById(R.id.img);
        videobtn = (Button)findViewById(R.id.video);
        textbtn.setOnClickListener(this);
        imgbtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, SelectAct.class);
                i.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH, cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(this,AddContent.class);
        switch (view.getId()){
            case R.id.text:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
        }
    }

    public void selecctDB(){
        cursor = dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null,null);
        adapter = new MyAdapter(this,cursor);
        lv.setAdapter(adapter);
    }

    protected void onResume(){
        super.onResume();
        selecctDB();
    }

}