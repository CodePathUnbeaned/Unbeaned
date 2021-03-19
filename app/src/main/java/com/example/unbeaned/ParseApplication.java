package com.example.unbeaned;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NhtrxOnE0JorwCP3TxQFBMWLHwJdm0vCp3hHtNgU")
                .clientKey("NKWFJGN3VmskzrypGOtnVPXXoqRXK288hVo8FOhY")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
