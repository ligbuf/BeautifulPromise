package com.beautifulpromise.facebooklibrary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.beautifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.facebooklibrary.AsyncFacebookRunner.RequestListener;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseRequestListener implements RequestListener {

    public void onFacebookError(FacebookError e, final Object state) {
       // Log.e("Facebook", e.getMessage());
    	Microlog4Android.logger.error("Facebook"+" - "+e.getMessage());
        e.printStackTrace();
    }

    public void onFileNotFoundException(FileNotFoundException e,
                                        final Object state) {
      //  Log.e("Facebook", e.getMessage());
    	Microlog4Android.logger.error("Facebook"+" - "+e.getMessage());
        e.printStackTrace();
    }

    public void onIOException(IOException e, final Object state) {
      //  Log.e("Facebook", e.getMessage());
    	Microlog4Android.logger.error("Facebook"+" - "+e.getMessage());
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e,
                                        final Object state) {
       // Log.e("Facebook", e.getMessage());
    	Microlog4Android.logger.error("Facebook"+" - "+e.getMessage());
        e.printStackTrace();
    }
    
}
