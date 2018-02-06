package com.yunnuo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yunuo.scale.ScaleSuler;

public class MainActivity extends AppCompatActivity {

    private ScaleSuler suler;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        suler = (ScaleSuler) findViewById(R.id.suler);
        tv = (TextView) findViewById(R.id.value);
        suler.setCallBack(new ScaleSuler.SulerCallBack() {
            @Override
            public void sulerValue(int value) {
                tv.setText(value+"");
            }
        });
    }
}
