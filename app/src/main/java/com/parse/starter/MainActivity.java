package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Button signUpButton;
  Button loginButton;
  EditText username;
  EditText password;
  RelativeLayout relativeLayout;
  ImageView logo;
  ParseUser user;
  ParseQuery<ParseUser> userQuery;
  ParseQuery<ParseObject> query;
  Intent mainFeed;




  //when a key is pressed (Password field in this case)
  //i is the code of the key that is pressed(NOTE: need to define if you want when key is pressed down or up defalut is both)
  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    //When enter button is pressed click the correct button (click the Visible one)
    if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

      //click the button that is currently visible
      if(signUpButton.getVisibility() == View.VISIBLE){
        signup(view);
      }else if(loginButton.getVisibility() == View.VISIBLE){
        login(view);
      }

    }

  Log.i("I = ", Integer.toString(i));


    return false;
  }


  @Override
  public void onClick(View view) {

    //finds what was clicked by the user (NOTE: logoImageView isn't registering a click, says it is background being clicked)
    //if the background is clicked hide the soft input (Keyboard)
    if(view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView){
      //close the phone keyboard if it is open
      //Log.i("View Clicked: ", "BackGround ");
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }



  }

  public void login(View view){

    Log.i("Value", username.getText().toString());

    ParseUser.logInInBackground(username.getText().toString().toLowerCase(), password.getText().toString(), new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {

        if(e == null){
          if(user.getUsername().equals(username.getText().toString().toLowerCase())){
           //user was found and logged in
            Toast.makeText(MainActivity.this, "User is logged in", Toast.LENGTH_LONG).show();
            startActivity(mainFeed);

          }else{
            Toast.makeText(MainActivity.this, "Incorrent username/password", Toast.LENGTH_LONG).show();
            Log.i("user value: " , user.getUsername());
            Log.i("username value:  " , username.getText().toString().toLowerCase());
          }
        }else{
          Log.i("ERROR!: " , e.toString());
        }


      }
    });





  }

  public void signup(View view){

    //new user will be logged in
    user = new ParseUser();
    //set the username to lowercase
    user.setUsername(username.getText().toString().toLowerCase());
    user.setPassword(password.getText().toString());

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null){
          Log.i("Signed up: " , "Great Success!");
          Toast.makeText(MainActivity.this, "Signed up ", Toast.LENGTH_LONG).show();
          startActivity(mainFeed);

        }else{
          Log.i("Signed up: " , "Failed!!" + e);
          Toast.makeText(MainActivity.this, "User already exists!! ", Toast.LENGTH_LONG).show();
        }
      }
    });






//    //check if user is in system
//    userQuery = ParseUser.getQuery();
//    userQuery.whereEqualTo("username",username.getText().toString());
//    userQuery.whereEqualTo("password",password.getText().toString());
//    userQuery.findInBackground(new FindCallback<ParseUser>() {
//      @Override
//      public void done(List<ParseUser> objects, ParseException e) {
//        if(e == null){
//          if(objects.size() > 0){
//            Log.i("User: " , "Found");
//            Toast.makeText(MainActivity.this, "User already exists!! ", Toast.LENGTH_LONG).show();
//
//          }else{
//            //no user founc continue to add them to the server
//
//
//
//
//          }
//        }
//      }
//    });


    //Log.i("CLicked: ", "signup button");

    //user.getCurrentUser().getUsername();

    //Log.i("User: ", user.getCurrentUser().getUsername());

  }


  public void changeButtons(View view){

    //the change button link next to button
    TextView textView = (TextView)findViewById(R.id.changeButtonTextView);


    //if signUpButton is visible
    if(signUpButton.getVisibility() == View.VISIBLE){

      signUpButton.setVisibility(View.INVISIBLE);
      loginButton.setVisibility(View.VISIBLE);
      textView.setText("or, Signup");

    }else{

      signUpButton.setVisibility(View.VISIBLE);
      loginButton.setVisibility(View.INVISIBLE);
      textView.setText("or, Login");
    }

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    signUpButton = (Button)findViewById(R.id.signupButton);
    loginButton = (Button)findViewById(R.id.loginButton);
    username = (EditText)findViewById(R.id.usernameTextView);
    password = (EditText)findViewById(R.id.passwordTextView);
    logo = (ImageView)findViewById(R.id.logoImageView);
    relativeLayout = (RelativeLayout)findViewById(R.id.backgroundRelativeLayout);
    mainFeed = new Intent(getApplicationContext(),MainFeed.class);

    relativeLayout.setOnClickListener(this);
    password.setOnKeyListener(this);

    //check if there is a user logged in already
    if(ParseUser.getCurrentUser() != null){
      startActivity(mainFeed);
    }

















    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Everything below this is for testing of ParseServer //

    //Log user out///

    //ParseUser.logOut();

    //Check if user is logged in //
    /*


    if(ParseUser.getCurrentUser() != null){
      Log.i("User Login: ", ParseUser.getCurrentUser().getUsername());
    }else{
      Log.i("User Login" , "no");
    }


    */

    //Log a user in ///
    /*

    ParseUser.logInInBackground("Myles R", "ass", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {

        if(user != null){

          Log.i("Login: " , "Successful!!");

        }else{

          Log.i("Login", "Failed!" + e.toString());
        }

      }
    });

  */


    //How to sign a user Up///
    /*
    ParseUser user = new ParseUser();

    user.setUsername("Myles R");
    user.setPassword("pass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {

        if(e == null){

          Log.i("Signup", "Success!");

        }else{
          Log.i("Signup", "Failed!");
        }


      }
    });

    */



    ///////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Manipulating ParseObjects

    /*

    //create a ParseQuery called query that gets the "table" called "Score"
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Score");

    //Basically a "WHERE" clause. Find all the rows that meet this requirement
    query.whereGreaterThan("score", 50);

    //Find in the background thread
    query.findInBackground(new FindCallback<ParseObject>() {
      //objects are the rows it found in a List
      @Override
      public void done(List<ParseObject> objects, ParseException e) {

        if(e == null){
          if(objects.size() > 0){
            //take all the objects(rows that were found) and puts them in a ParseObject variable that is called object
            for(ParseObject object : objects){
              //get the single object and add 50 to the score value
              object.put("score", object.getInt("score") + 50);
              //save it
              object.saveInBackground();

              Log.i("Score Value: ", Integer.toString(object.getInt("score")));
            }

          }

        }

      }
    });

   */

  /*
    /////Retrieving multiple rows from ParseObject//////

    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Score");

    //retrieve a row im looking for
    parseQuery.whereEqualTo("username", "Myles");
    parseQuery.setLimit(1);

    parseQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {

        if(e == null){
          Log.i("Find in background", "Retrieved" + objects.size());
        }
        if(objects.size() > 0){
          for(ParseObject object : objects){
              //take that row and find the "score" or any other value in that row
              Log.i("Background Results: ", Integer.toString(object.getInt("score")));

          }
        }

      }
    });


*/
    /* /////////////////////////////////////////////////////////

    How to save data as a ParseObject


    ParseObject score = new ParseObject("Score");
    score.put("username" , "myles");
    score.put("score", 83);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {

        if(e == null){
          Log.i("SaveInBackground", "Successful");
        }else{
          Log.i("SaveInBackground", "Failed");
        }


      }
    });

    ////////////////////////////////////////////
    ////////////////////////////////////////////
    ////////////////////////////////////////////


    // How to read data from ParseObject //

    //what Object are you wanting to retrieve (Bitnami)
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    //ObjectId of the object you want to retrieve (in the website[server ip])
    query.getInBackground("8N3Nefv9l3", new GetCallback<ParseObject>() {
        @Override
      public void done(ParseObject object, ParseException e) {

        if(e == null && object != null){

          //Updates the values of the object created in aws server
          object.put("score", 98);
          object.saveInBackground();

          Log.i("ObjectValue", object.getString("username"));
          Log.i("Score value" , Integer.toString(object.getInt("score")));

        }
      }
    });

    //////////////////////////////////////////////////////
    */

    //////Creating a new ParseObject///////



//    ParseObject object = new ParseObject("Tweet");
//    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Tweet");
//
//
//    object.put("username", "M DOE");
//    object.put("tweet", "My new tweet");
//
//    object.saveInBackground(new SaveCallback() {
//      @Override
//      public void done(ParseException e) {
//
//        if(e != null) {
//          Log.i("Object save: ", "Failed");
//        }else{
//          Log.i("Object save: ", "Successful");
//        }
//
//      }
//    });

    /*

    ///////////////////////////////////////
    //////////////////////////////////////
    //////////////////////////////////////

    //// Grab the values you just created////

    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Tweet");

    parseQuery.getInBackground("ese3Q291oS", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {

        if(e == null){

          Log.i("Username", object.getString("username"));
          Log.i("Tweet value:", object.getString("tweet"));
        }else{
          Log.i("An error occured", "There was an error somewhere good luck finding it. Mwhahahahahahahaha");
        }


      }
    });


*/

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
}
