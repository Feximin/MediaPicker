package com.feximin.mediapicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add("选择图片（单选）");
        adapter.add("选择图片（多选）");
        adapter.add("选择视频（单选）");
        adapter.add("选择视频（多选）");
        adapter.add("选择图片或视频（单选）");
        adapter.add("选择图片或视频（多选）");
        adapter.add("选择音频（单选）");
        adapter.add("选择音频（多选）");
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}