package com.unbeaned.app;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.models.Images;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

public class ParseApplication extends Application {
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NhtrxOnE0JorwCP3TxQFBMWLHwJdm0vCp3hHtNgU")
                .clientKey("NKWFJGN3VmskzrypGOtnVPXXoqRXK288hVo8FOhY")
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Review.class);
        ParseObject.registerSubclass(Images.class);
        ParseObject.registerSubclass(Comment.class);
    }
}