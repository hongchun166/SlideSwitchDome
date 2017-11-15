package com.thc.mydomeview.dome;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.thc.mydomeview.R;

import thc.com.view.slideswitch.alphabetView.AlphabetView;
import thc.com.view.slideswitch.alphabetView.OnTouchAssortListener;
import thc.com.view.slideswitch.alphabetView.OnTouchAssortListenerImp;

/**
 * Created by Nicky on 2017/11/15.
 */

public class AlphabetDemoActivity extends AppCompatActivity {
    AlphabetView alphabetView;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_demo_activity);
         alphabetView= (AlphabetView) findViewById(R.id.alphabetView);
        textView= (TextView) findViewById(R.id.textView);
        alphabetView.setDefultImpOnTouchAssortListener();
        alphabetView.setOnTouchAssortListener(new OnTouchAssortListenerImp() {
            @Override
            public void onTouchAssortChanged(String s) {
                textView.setText(s);
            }
        });
    }
}
