package com.croxx.michelsoncounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity {

    static {
        OpenCVLoader.initDebug();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button HoughCaler = (Button) findViewById(R.id.HoughCaller);
        Button CenterCaller = (Button) findViewById(R.id.CenterCaller);
        HoughCaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HoughActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        CenterCaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CenterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }


}