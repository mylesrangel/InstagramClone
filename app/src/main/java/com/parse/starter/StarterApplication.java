package com.parse.starter;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);


    /*
    Amazon AWS EC2 Parse Server is what is used here for the server.
    Need to set up your own to store users


    How to get information below
    need to ssh into parse server to find this information (putty)
    need the EC2 public ipv4 address as the host name in putty
    need .ppk in auth section of putty
    need to generate the .ppk in puttygen using a .pem
    if you dont have a .pem you need to remake the instance in EC2 to get a new .pem
    then place the public dns into address with /apps on the end and it should bring up the server
    ex. ec2-34-209-55-211.us-west-2.compute.amazonaws.com/apps

    Dashboard password is in ec2 Dashboard/instances
    highlight instance then action->instance settings->getsystemlog
    will display username and pw


     */



    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("a119eb45626548b4ef4e74bd7201294a79329b46")
            .clientKey("f9d3968fa508daecb5c4326ae865b9e84ca45ee7") //masterkey
            .server("http://34.209.55.211:80/parse/")
            .build()
    );

    //ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
