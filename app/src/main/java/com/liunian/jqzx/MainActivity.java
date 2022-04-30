package com.liunian.jqzx;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity---->";
    private EditText bz1b;
    private EditText ce1b;
    private EditText ca1b;
    private EditText q1b;
    private EditText bz3a;
    private EditText ce3a;
    private EditText ca3a;
    private EditText q3a;
    private EditText mpca3a;
    private EditText bz3b;
    private EditText ce3b;
    private EditText ca3b;
    private EditText q3b;
    private EditText mpca3b;
    private EditText bz201;
    private EditText ce201;
    private EditText ca201;
    private EditText q201;
    private EditText mpca201;
    private TextView h3a, h3b, h201, z3a, x3a, x3b, z3b, z201, x201, h1b;
    TextView s3a, s3b, s201;
    private Button calculate, clear;
    private AeDao aeDao = null;
    private Switch aSwitch;
    private boolean checked = true;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.select:
                Intent intent = SelectActivity.getIntent(this);
                startActivity(intent);
                break;
            case R.id.week:
                Intent i = WeekActivity.getIntent(this);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        aeDao = new AeDao(this);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                Toast.makeText(MainActivity.this, isChecked ? "开启保存" : "不保存", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            select("AE-51101B");
            select("AE-51103A");
            select("AE-51103B");
            select("AE-51201");
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "记录为空", Toast.LENGTH_SHORT).show();
        }
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] a = {1, 1, 1, 1};
                try {
                    a = ae101b();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "AE-51101B不能为空", Toast.LENGTH_SHORT).show();
                }
                try {
                    ae103a(a);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
                }
                try {
                    ae103b(a);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
                }
                try {
                    ae201(a);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
                }
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
                String time = dateFormat.format(date);
                int itime = Integer.parseInt(time);
                if (itime < 14 && itime > 8) {
                    time = "08";
                } else if (itime > 14 && itime < 20) {
                    time = "14";
                } else if (itime < 8 && itime > 2) {
                    time = "02";
                } else if (itime > 20) {
                    time = "20";
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", "环己酮加氢" + time + ":00样" +
                        "\n转化率 A:" + ZXS.Z103A + " B:" + ZXS.Z103B + "" +
                        "\n选择性 A: " + ZXS.X103A + " B:" + ZXS.X103B + "" +
                        "\n总转化率:" + ZXS.Z201 + "" +
                        "\n总选择性:" + ZXS.X201);
                cm.setPrimaryClip(clipData);
            }
        });
        //清空
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bz1b.setText("");
                ce1b.setText("");
                ca1b.setText("");
                q1b.setText("");
                bz3a.setText("");
                ce3a.setText("");
                ca3a.setText("");
                q3a.setText("");
                mpca3a.setText("");
                bz3b.setText("");
                ce3b.setText("");
                ca3b.setText("");
                q3b.setText("");
                mpca3b.setText("");
                bz201.setText("");
                ce201.setText("");
                ca201.setText("");
                q201.setText("");
                mpca201.setText("");
                h1b.setText("");
            }
        });

    }

    private static class ZXS {
        public static Double Z103A = 0.0;
        public static Double X103A = 0.0;
        public static Double S103A = 0.0;
        public static Double Z103B = 0.0;
        public static Double X103B = 0.0;
        public static Double S103B = 0.0;
        public static Double Z201 = 0.0;
        public static Double X201 = 0.0;
        public static Double S201 = 0.0;
    }

    //读取数据库数据写入文本框
    private void select(String w) {
        AeDao aeDao = new AeDao(this);
        ArrayList<AE> query = aeDao.query(w);
        for (AE ae : query) {
            String a_no = ae.getA_NO();
            switch (a_no) {
                case "AE-51101B":
                    bz1b.setText(ae.getBZ());
                    ce1b.setText(ae.getCE());
                    ca1b.setText(ae.getCA());
                    q1b.setText(ae.getQ());
                    break;
                case "AE-51103A":
                    bz3a.setText(ae.getBZ());
                    ce3a.setText(ae.getCE());
                    ca3a.setText(ae.getCA());
                    q3a.setText(ae.getQ());
                    mpca3a.setText(ae.getMPCA());
                    break;
                case "AE-51103B":
                    bz3b.setText(ae.getBZ());
                    ce3b.setText(ae.getCE());
                    ca3b.setText(ae.getCA());
                    q3b.setText(ae.getQ());
                    mpca3b.setText(ae.getMPCA());
                    break;
                case "AE-51201":
                    bz201.setText(ae.getBZ());
                    ce201.setText(ae.getCE());
                    ca201.setText(ae.getCA());
                    q201.setText(ae.getQ());
                    mpca201.setText(ae.getMPCA());
                    break;
            }
        }
    }

    private void ae201(double a[]) {
        double a1bz = a[0];
        double a1ce = a[1];
        double mpca1 = Double.parseDouble(String.valueOf(mpca201.getText()));
        double bz1 = Double.parseDouble(String.valueOf(bz201.getText()));
        double ce1 = Double.parseDouble(String.valueOf(ce201.getText()));
        double ca1 = Double.parseDouble(String.valueOf(ca201.getText()));
        double q1 = Double.parseDouble(String.valueOf(q201.getText()));
        addAe.addAe201 = (bz1 + ce1 + ca1 + q1 + mpca1);

        double wbz = bz1 / 78;
        double wce = ce1 / 82;
        double wca = (ca1 + mpca1) / 84;
        double wq = q1 / 99;
        double a201bz = wbz / (wbz + wce + wca + wq);
        double a201ce = wce / (wbz + wce + wca + wq);
       /* double a201ca = wca / (wbz + wce + wca + wq);
        double a201q = wq / (wbz + wce + wca + wq);*/

        BigDecimal bg = new BigDecimal(addAe.addAe201);
        addAe.addAe201 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        h201.setText(addAe.addAe201 + "");

        ZXS.Z201 = ((a1bz - a201bz) / a1bz) * 100;
        ZXS.Z201 = new BigDecimal(ZXS.Z201).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        z201.setText(ZXS.Z201 + "");
        ZXS.X201 = new BigDecimal(((a201ce - 0.5 * a1ce) / (a1bz - a201bz)) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        x201.setText(ZXS.X201 + "");
        double s = ZXS.Z201 * ZXS.X201;
        ZXS.S201 = new BigDecimal(s / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        s201.setText(ZXS.S201 + "");
        if (checked) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String time = dateFormat.format(date);
            AE ae = new AE(bz1 + "", ce1 + "", ca1 + "", q1 + "", mpca1 + "", "AE-51201", time);
            long insert = aeDao.insert(ae);
            if (insert == -1) {
                Toast.makeText(MainActivity.this, "201插入失败", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(MainActivity.this, "201插入成功", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ae201: 插入成功");
            }
        }
    }

    private void ae103b(double a[]) {
        double a1bz = a[0];
        double a1ce = a[1];
        double bz1 = Double.parseDouble(String.valueOf(bz3b.getText()));
        double ce1 = Double.parseDouble(String.valueOf(ce3b.getText()));
        double ca1 = Double.parseDouble(String.valueOf(ca3b.getText()));
        double q1 = Double.parseDouble(String.valueOf(q3b.getText()));
        double mpca1 = Double.parseDouble(String.valueOf(mpca3b.getText()));
        addAe.addAe103b = (bz1 + ce1 + ca1 + q1 + mpca1);
        double wbz = bz1 / 78;
        double wce = ce1 / 82;
        double wca = (ca1 + mpca1) / 84;
        double wq = q1 / 99;
        double a3bbz = wbz / (wbz + wce + wca + wq);
        double a3bce = wce / (wbz + wce + wca + wq);
      /*  double a3bca = wca / (wbz + wce + wca + wq);
        double a3bq = wq / (wbz + wce + wca + wq);*/

        BigDecimal bg = new BigDecimal(addAe.addAe103b);
        addAe.addAe103b = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        h3b.setText(addAe.addAe103b + "");
        ZXS.Z103B = ((a1bz - a3bbz) / a1bz) * 100;
        BigDecimal g = new BigDecimal(ZXS.Z103B);
        ZXS.Z103B = g.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        z3b.setText(ZXS.Z103B + "");
        ZXS.X103B = ((a3bce - 0.5 * a1ce) / (a1bz - a3bbz)) * 100;
        BigDecimal b = new BigDecimal(ZXS.X103B);
        ZXS.X103B = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        x3b.setText(ZXS.X103B + "");
        double s = ZXS.Z103B * ZXS.X103B;
        ZXS.S103B = new BigDecimal(s / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        s3b.setText(ZXS.S103B + "");
        if (checked) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String time = dateFormat.format(date);
            AE ae = new AE(bz1 + "", ce1 + "", ca1 + "", q1 + "", mpca1 + "", "AE-51103B", time);
            long insert = aeDao.insert(ae);
            if (insert == -1) {
                Toast.makeText(MainActivity.this, "103B插入失败", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this, "103B插入成功", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ae103b: 插入成功");
            }
        }
    }

    private void ae103a(double a[]) {
        double a1bz = a[0];
        double a1ce = a[1];
        double mpca1 = Double.parseDouble(String.valueOf(mpca3a.getText()));
        double bz1 = Double.parseDouble(String.valueOf(bz3a.getText()));
        double ce1 = Double.parseDouble(String.valueOf(ce3a.getText()));
        double ca1 = Double.parseDouble(String.valueOf(ca3a.getText()));
        double q1 = Double.parseDouble(String.valueOf(q3a.getText()));
        addAe.addAe103a = (mpca1 + bz1 + ce1 + ca1 + q1);
        double wbz = bz1 / 78;
        double wce = ce1 / 82;
        double wca = (ca1 + mpca1) / 84;
        double wq = q1 / 99;
        double a3abz = wbz / (wbz + wce + wca + wq);
        double a3ace = wce / (wbz + wce + wca + wq);
        /*double a3aca = wca / (wbz + wce + wca + wq);
        double a3aq = wq / (wbz + wce + wca + wq);*/


        BigDecimal bg = new BigDecimal(addAe.addAe103a);
        addAe.addAe103a = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        ZXS.Z103A = ((a1bz - a3abz) / a1bz) * 100;
        BigDecimal g = new BigDecimal(ZXS.Z103A);
        ZXS.Z103A = g.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        ZXS.X103A = ((a3ace - 0.5 * a1ce) / (a1bz - a3abz)) * 100;
        BigDecimal b = new BigDecimal(ZXS.X103A);
        ZXS.X103A = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        ZXS.S103A = ZXS.Z103A * ZXS.X103A;
        ZXS.S103A = new BigDecimal(ZXS.S103A / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        s3a.setText(ZXS.S103A + "");
        h3a.setText(addAe.addAe103a + "");
        z3a.setText(ZXS.Z103A + "");
        x3a.setText(ZXS.X103A + "");
        if (checked) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String time = dateFormat.format(date);
            AE ae = new AE(bz1 + "", ce1 + "", ca1 + "", q1 + "", mpca1 + "", "AE-51103A", time);
            long insert = aeDao.insert(ae);
            if (insert == -1) {
                Toast.makeText(MainActivity.this, "103A插入失败", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this, "103A插入成功", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ae103a: 插入成功");
            }
        }
    }

    private static class addAe {
        public static Double addAe101b = 0.0;
        public static Double addAe103a = 0.0;
        public static Double addAe103b = 0.0;
        public static Double addAe201 = 0.0;
    }

    private double[] ae101b() {
        double a[] = new double[2];
        double bz1 = Double.parseDouble(String.valueOf(bz1b.getText()));
        double ce1 = Double.parseDouble(String.valueOf(ce1b.getText()));
        double ca1 = Double.parseDouble(String.valueOf(ca1b.getText()));
        double q1 = Double.parseDouble(String.valueOf(q1b.getText()));
        double wbz = bz1 / 78;
        double wce = ce1 / 82;
        double wca = ca1 / 84;
        double wq = q1 / 99;

        addAe.addAe101b = (bz1 + ce1 + ca1 + q1);
        BigDecimal bg = new BigDecimal(addAe.addAe101b);
        addAe.addAe101b = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        h1b.setText(addAe.addAe101b + "");
        double a1bz = wbz / (wbz + wce + wca + wq);
        double a1ce = wce / (wbz + wce + wca + wq);
        a[0] = a1bz;
        a[1] = a1ce;
        if (checked) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String time = dateFormat.format(date);
            AE ae = new AE(bz1 + "", ce1 + "", ca1 + "", q1 + "", "AE-51101B", time);
            long insert = aeDao.insert(ae);
            if (insert == -1) {
                Toast.makeText(MainActivity.this, "101B插入失败", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this, "101B插入成功", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ae101b: 插入成功");
            }
        }
        return a;
    }

    private void init() {
        calculate = findViewById(R.id.calculate);
        bz1b = findViewById(R.id.bz1b);
        ce1b = findViewById(R.id.ce1b);
        ca1b = findViewById(R.id.ca1b);
        q1b = findViewById(R.id.q1b);
        bz3a = findViewById(R.id.bz3a);
        ce3a = findViewById(R.id.ce3a);
        ca3a = findViewById(R.id.ca3a);
        q3a = findViewById(R.id.q3a);
        mpca3a = findViewById(R.id.mpca3a);
        bz3b = findViewById(R.id.bz3b);
        ce3b = findViewById(R.id.ce3b);
        ca3b = findViewById(R.id.ca3b);
        q3b = findViewById(R.id.q3b);
        mpca3b = findViewById(R.id.mpca3b);
        bz201 = findViewById(R.id.bz201);
        ce201 = findViewById(R.id.ce201);
        ca201 = findViewById(R.id.ca201);
        q201 = findViewById(R.id.q201);
        mpca201 = findViewById(R.id.mpca201);
        h3a = findViewById(R.id.h3a);
        h3b = findViewById(R.id.h3b);
        h201 = findViewById(R.id.h201);
        z3a = findViewById(R.id.z3a);
        z3b = findViewById(R.id.z3b);
        z201 = findViewById(R.id.z201);
        x3a = findViewById(R.id.x3a);
        x3b = findViewById(R.id.x3b);
        x201 = findViewById(R.id.x201);
        s3a = findViewById(R.id.ss3a);
        s3b = findViewById(R.id.ss3b);
        s201 = findViewById(R.id.ss201);
        aSwitch = findViewById(R.id.switch1);
        checked = aSwitch.isChecked();
        clear = findViewById(R.id.clear);
        h1b = findViewById(R.id.h1b);
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
//System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aeDao.closeLink();
    }
}