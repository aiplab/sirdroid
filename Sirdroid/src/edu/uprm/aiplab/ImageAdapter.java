package edu.uprm.aiplab;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    int mGalleryItemBackground;
    private Context mContext;

    /*private Integer[] mImageIds = {
            R.drawable.ic_tab,
    };*/
    
    private List<String> ls;

    public ImageAdapter(Context c, List<String> ls) {
        mContext = c;
        this.ls=ls;
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.TestActivity);
        mGalleryItemBackground = attr.getResourceId(
                R.styleable.TestActivity_android_galleryItemBackground, 0);
        attr.recycle();
    }

    public int getCount() {
        //return mImageIds.length;
    	return ls.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        //imageView.setImageResource(mImageIds[position]);
        imageView.setImageBitmap(downloadFile("http://136.145.56.222/sirlab/"+ls.get(position)));
        imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);

        return imageView;
    }

    private Bitmap downloadFile(String fileUrl){
        URL myFileUrl =null; 
        Bitmap bm=null;
        try {
             myFileUrl= new URL(fileUrl);
        } catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
        }
        try {
             HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
             conn.setDoInput(true);
             conn.connect();
             InputStream is = conn.getInputStream();
             
              bm = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
        }
        return bm;
   }
}