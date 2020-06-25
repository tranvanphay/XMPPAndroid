package com.phaytran.xmppAndroidClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private Adapter adapter;
    private ArrayList<MessageData> messageData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv=findViewById(R.id.rv);
        adapter = new Adapter(messageData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rv.addItemDecoration(decoration);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }
}
