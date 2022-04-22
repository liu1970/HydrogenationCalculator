package com.liunian.jqzx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {

    private final List<Map<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        select();
        ListView listView = findViewById(R.id.list);
        int[] a = {R.id.a_no, R.id.time, R.id.bz, R.id.ce, R.id.ca, R.id.mpca, R.id.q};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.list_item, new String[]{"位号", "时间",
                "BZ", "CE", "CA", "MPCA", "轻"}, a);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(itemClickListener(simpleAdapter));

    }

    private AdapterView.OnItemClickListener itemClickListener(SimpleAdapter simpleAdapter){
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = list.get(position);
                String tim = map.get("时间");
                String wh = map.get("位号");
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示")
                        .setMessage("是否删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AeDao aeDao = new AeDao(context);
                                StringBuffer str = new StringBuffer();
                                str.append("time='")
                                        .append(tim)
                                        .append("' and a_no='")
                                        .append(wh)
                                        .append("'");
                                int delete = aeDao.delete(str.toString());
                                String result = "";
                                if (delete != 0) {
                                    result = "删除成功";
                                    list.remove(position);
                                } else {
                                    result = "删除失败";
                                }
                                Toast.makeText(SelectActivity.this, result, Toast.LENGTH_SHORT).show();
                                simpleAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .create().show();
            }
        };
        return onItemClickListener;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SelectActivity.class);
        return intent;
    }

    private void select() {
        AeDao aeDao = new AeDao(this);
        ArrayList<AE> query = aeDao.query();
        for (AE ae : query) {
            Map<String, String> map = new HashMap<>();
            map.put("位号", ae.getA_NO());
            map.put("时间", ae.getTIME());
            map.put("BZ", ae.getBZ());
            map.put("CE", ae.getCE());
            map.put("CA", ae.getCA());
            map.put("MPCA", ae.getMPCA());
            map.put("轻", ae.getQ());
            list.add(map);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}