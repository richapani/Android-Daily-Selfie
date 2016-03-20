package com.example.root.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by root on 21/8/15.
 */
class PhotoAdapter extends ArrayAdapter<String>{

    private final Activity context;

    private final ArrayList<String> listOfImages;

    public PhotoAdapter(Activity context, ArrayList<String> listOfImages) {
        super(context, R.layout.viewlayout, listOfImages);
        this.context=context;
        this.listOfImages = listOfImages;

    }


    public View getView(int position,View view,ViewGroup parent) {

        ViewHold holder;

        if(view == null)
        {
            LayoutInflater inflater=context.getLayoutInflater();
            view =inflater.inflate(R.layout.viewlayout, null,true);

            holder = new ViewHold();
            holder.imageView = (ImageView) view.findViewById(R.id.imagev);
            holder.txtTitle = (TextView) view.findViewById(R.id.image_name);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHold) view.getTag();
        }


        Bitmap bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
        File f = new File(listOfImages.get(position));

        holder.txtTitle.setText(f.getName());
        holder.imageView.setImageBitmap(bitmap);

        return view;

    };



}

class ViewHold {
    TextView txtTitle;
    ImageView imageView;
}
