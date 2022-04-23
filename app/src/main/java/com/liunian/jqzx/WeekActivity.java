package com.liunian.jqzx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

public class WeekActivity extends AppCompatActivity {
    EditText bz, ce, ca, lastWeek, thisWeek;
    TextView zbz, zh2, t2709;
    private float bz_value;
    private float ce_value;
    private float ca_value;
    private float last_week_value;
    private float this_week_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        init();

    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, WeekActivity.class);
        return intent;
    }

    private void init() {
        bz = findViewById(R.id.bz_week);
        ce = findViewById(R.id.ce_week);
        ca = findViewById(R.id.ca_week);
        lastWeek = findViewById(R.id.lastWeek);
        thisWeek = findViewById(R.id.thisWeek);
        zbz = findViewById(R.id.zbz);
        zh2 = findViewById(R.id.zh2);
        t2709 = findViewById(R.id.t512709);
    }

    private void getValues() {
        String s = bz.getText().toString();
        bz_value = Float.parseFloat(s);
        s = ce.getText().toString();
        ce_value = Float.parseFloat(s);
        s = ca.getText().toString();
        ca_value = Float.parseFloat(s);
        s = lastWeek.getText().toString();
        last_week_value = Float.parseFloat(s);
        s = thisWeek.getText().toString();
        this_week_value = Float.parseFloat(s);

    }


    private void compute() {
        float liquidity = this_week_value - last_week_value;
        double zsbz = (liquidity * 17.46 * 78 * ((ce_value / 82) + (ca_value / 84) + (bz_value / 78)))/100;
        double zsh2 = (liquidity * 17.46 * 1000 * 22.4 * (ce_value / 41 + ca_value / 28))/100;
        liquidity = new BigDecimal(liquidity).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        zsbz = new BigDecimal(zsbz).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        zsh2 = new BigDecimal(zsh2).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        t2709.setText(liquidity + "");
        zbz.setText(zsbz + "");
        zh2.setText(zsh2 + "");
    }

    public void start(View view) {
        try {
            getValues();
            compute();
        } catch (Exception e) {
            Toast.makeText(WeekActivity.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}