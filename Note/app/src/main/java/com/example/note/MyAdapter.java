package com.example.note;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.app.ActivityCompat;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;


    public MyAdapter(Context context,Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout)inflater.inflate(R.layout.cell,null);
        TextView contenttv = (TextView)layout.findViewById(R.id.list_content);
        TextView timttv = (TextView)layout.findViewById(R.id.list_time);
        ImageView imgiv = (ImageView) layout.findViewById(R.id.list_img);
        ImageView videoiv = (ImageView) layout.findViewById(R.id.list_video) ;
        cursor.moveToPosition(i);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path"));
        String urlvedio = cursor.getString(cursor.getColumnIndex("video"));
        contenttv.setText(content);
        timttv.setText(time);
        videoiv.setImageBitmap(getVideoThumbnail(urlvedio,200,200, MediaStore.Images.Thumbnails.MICRO_KIND));
        imgiv.setImageBitmap(getImageThumbnail(url,200,200));
        return layout;
    }

    public Bitmap getImageThumbnail(String url,int width,int height){
        //获取缩略图
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        int beWidth = options.outWidth/width;
        int beHeight = options.outHeight/height;
        int be = 1;
        if(beWidth < beWidth){
            be = beWidth;
        }else{
            be = beHeight;
        }
        if(be <=0){
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(url,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
