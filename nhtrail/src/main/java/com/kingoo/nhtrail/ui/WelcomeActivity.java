package com.kingoo.nhtrail.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kingoo.nhtrail.R;

/**
 * @author Administrator
 * @date 2015/5/21
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 延迟进入
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MapActivity.class));
                finish();
            }
        }, 2000);

    }
}
