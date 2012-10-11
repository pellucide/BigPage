package com.example.android;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DragableSpace space = new DragableSpace(this.getApplicationContext(), 2,2);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        BreadCrumb bc = new BreadCrumb(this);
        bc.setText("caliber");
        //setContentView(R.layout.main1);
        setContentView(R.layout.main);
        LinearLayout bigpage = (LinearLayout) findViewById(R.id.bigpage);
        DragableSpace space = (DragableSpace) findViewById(R.id.space);
        CrumbView crumbView = (CrumbView) findViewById(R.id.crumbView);
        AlphaAnimation aa = new AlphaAnimation(0f,1f);
        aa.setDuration(10000);
        bc.startAnimation(aa);
        if (crumbView != null)
            crumbView.addView(bc);
        
        //setContentView(space);
        //space.disableSnap();
    }
}
