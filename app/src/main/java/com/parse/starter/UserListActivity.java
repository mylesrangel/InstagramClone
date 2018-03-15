package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent intent = getIntent();

        //activeUsername is named that was clicked!
        String activeUsername = intent.getStringExtra("username");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setTitle(activeUsername + "'s feed");
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username", activeUsername);
        query.orderByDescending("createdAt");

        //process to retrieve the image file from Parse server
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        //create an object to store the Objects(images)
                        for(ParseObject object: objects){
                            //file now has the file of the image but need to get the data
                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                //data is now in a byte array. Something we can use to decode it to a bitmap for displaying
                                public void done(byte[] data, ParseException e) {
                                    if(e == null){
                                        if(data != null){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                            //this code adds an imageview to a linearlayout
                                            //how you want the imageview to display (also create imageview)
                                            ImageView imageView = new ImageView(getApplicationContext());
                                            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                            ));

                                            //set the image that you want to be displayed(in drawable of resources)
                                            //imageView.setImageDrawable(getResources().getDrawable(R.drawable.instagramlogo));
                                            //set the image from drawable
                                            imageView.setImageBitmap(bitmap);

                                            //add the imageView to the linearlayout
                                            linearLayout.addView(imageView);

                                        }
                                    }


                                }
                            });

                        }
                    }
                }
            }
        });





    }
}
