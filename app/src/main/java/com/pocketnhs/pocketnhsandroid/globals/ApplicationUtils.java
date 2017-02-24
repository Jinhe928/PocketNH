package com.pocketnhs.pocketnhsandroid.globals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;


import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by MacBook Pro on 7/21/2016.
 */

public class ApplicationUtils {

    public static String readFromFile(String fileName, Context context) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            Log.e("MainActivity", fileName + " not found");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return new String(bytes);
    }

    public static boolean writeToFile(String fileName, JSONObject object, Context context) {
        File path = context.getFilesDir();
        Log.e("DPFMS", path.toString());
        File file = new File(path, fileName);
        boolean written = false;

        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(object.toString().getBytes());
            stream.close();
            written = true;
        }  catch (FileNotFoundException e){
            e.printStackTrace();
            written = false;
            Log.e("DPFMS", "File not found");
        }  catch (IOException ioe) {
            ioe.printStackTrace();
            written = false;
        }
        return written;
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static void saveBitmapToFile(Bitmap bitmap, NHSOrganisation organisation) {
        if (bitmap!=null) {
            try {

                File file = new File(ApplicationState.getAppContext().getFilesDir(), organisation.defineFilename());
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("save bitmap", "Save file error!");
            }
        } else{
            Log.e("save bitmap", "bitmap is null");
        }
    }

    public static String cleanText(String mText) {

        String clean = mText.replace("Â","");
        clean =  clean.replace("â","");
        clean =  clean.replace("\u0080","");
        clean =  clean.replace("\u0093","");
        clean =  clean.replace("\u0099","");
        return clean;
    }
}
