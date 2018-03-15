package com.parse.starter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//MainFeed is the feed of the app.
public class MainFeed extends AppCompatActivity {

    ListView feedListView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;



    public void getPhoto(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //requestCode identifies the intent
        startActivityForResult(intent,1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2){
            if(grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]){

                getPhoto();
            }
        }
    }


    public void updateListUser(){

        Log.i("View: ", "Made it to Update List user");

        //Create a Parse User query to find Users
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //get all users except the one that is logged in
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e == null){
                    if(objects.size()>0) {
                        //get all the objects and turn them into users then get the username
                        for (ParseUser user : objects) {

                            arrayList.add(user.getUsername());
                            Log.i("Adding User:  ", user.getUsername());
                        }
                        //set the array adapter to show the results
                        //can also use arrayAdapter.notify...to tell the results changed in the List
                        feedListView.setAdapter(arrayAdapter);

                    }else{
                        Log.i("Error! ", "No users found (updateLIstUser)");
                    }
                }else{
                    Log.i("Error!", String.valueOf(e));
                }
            }
        });


    }

    //Items selected in the top right menu area
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);



        super.onCreateOptionsMenu(menu);
        return true;
    }
    //this is called when an item is selected from OptionsMenu (above)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.share){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                }else{

                    getPhoto();
                }
            }else{
                getPhoto();
            }

        }else if(item.getItemId() == R.id.logout){

            ParseUser.logOut();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    //get a result from the intent that was called
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            //data.getData() returns location of where the picture(in this case) is at.
            Log.i("Data: " , String.valueOf(data.getData()));

            try {
                //get the selected image that user clicks
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);

                Log.i("Photo: ", "Recieved");

                //this will allow me to format the picture into something I can store in Parse
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //store the compressed image into byteArray
                byte[] byteArray = stream.toByteArray();

                //create a file to be saved in Parse. Call the image saves "image.png" (ByteArray is the compressed image
                ParseFile file = new ParseFile("image.png", byteArray);
                //create the new object in Parse
                ParseObject object = new ParseObject("Image");
                //put objects
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null){

                            Toast.makeText(MainFeed.this, "Image Saved", Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(MainFeed.this, "Image Not Saved! Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            } catch (IOException e) {

                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        feedListView = (ListView) findViewById(R.id.feedListView);
        arrayList = new ArrayList<>();
        updateListUser();
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                intent.putExtra("username" , arrayList.get(i));
                startActivity(intent);
            }
        });
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);




        Log.i("View: ", "Made it to Main Feed");
    }
}
