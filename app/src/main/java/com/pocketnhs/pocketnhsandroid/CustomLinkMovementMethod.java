package com.pocketnhs.pocketnhsandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;

import java.lang.ref.WeakReference;

/**
 * Created by MacBook Pro on 9/18/2016.
 */

public class CustomLinkMovementMethod extends LinkMovementMethod {

    private static Context movementContext;


    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();
    private static WeakReference<Activity> activityRef;

    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                String url = link[0].getURL();
                if (url.startsWith("http")) {
                    //Log.d("Link", url);
                    //Toast.makeText(movementContext, "Link was clicked", Toast.LENGTH_LONG).show();

                        ApplicationState.sLastWebViewURL = url;
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activityRef.get())
                                .toBundle();
                        Intent intent = new Intent(activityRef.get(), ArticleWebView.class);
                        activityRef.get().startActivity(intent,bundle);

                }
                /*else if (url.startsWith("tel"))
                {
                    Log.d("Link", url);
                    Toast.makeText(movementContext, "Tel was clicked", Toast.LENGTH_LONG).show();
                } else if (url.startsWith("mailto"))
                {
                    Log.d("Link", url);
                    Toast.makeText(movementContext, "Mail link was clicked", Toast.LENGTH_LONG).show();
                }
                */
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static android.text.method.MovementMethod getInstance(Context c, Activity activity) {
        movementContext = c;
        activityRef = new WeakReference<Activity>(activity);
        return linkMovementMethod;
    }
}
