package com.aldroid.opencamera.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class Utils {

    final static public String APP_KEY = "yzkwwegqg2e2mt1";
    final static public String APP_SECRET = "2dc14mqgp9n0rrn";


    final static public String MEDIA_FOLDER = "/media";
    final static public String APP_FOLDER = "/opencamera";

    public static void log(String log){
        Log.v("eltonkolaxx", log);
    }

    public static void share(@Nullable Context mContext, @NotNull File localFile) {

        if(!localFile.exists()){
            return;
        }else {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+localFile.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            mContext.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }



    }

    /*

    Dahua	                60.191.94.122:8086	554 admin/admin321      Camera is live
    Hikvision	            123.157.208.28:81	555 admin/abcd1234	    Camera back up (3/29)
    Uniview (UNV)	        61.164.52.166:88	--  admin/Uniview2018	Camera is live
    Bosch (Dinion7000HD)	193.159.244.134	    --  service/Xbks8tr8vT	Camera is live
    Bosch (Autodom7000)	    193.159.244.132     --  service/Xbks8tr8vT	Camera is live

    */

}
