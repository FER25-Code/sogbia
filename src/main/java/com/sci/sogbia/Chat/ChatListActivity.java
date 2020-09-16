package com.sci.sogbia.Chat;

import android.content.Context;

import android.os.Bundle;


import com.sci.sogbia.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListActivity extends AppCompatActivity {
    private ArrayList<Profail> profails = new ArrayList<Profail>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private  ChatAdapter  adapter;
    Context contest=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_activity);
        Recycle();
        for (int i = 0; i < 5; i++) {
            Profail profail = new Profail();
            profail.setNameprofil("fer Ramy");
            profails.add(profail);
        }

    }
    public void Recycle (){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewchat);
        adapter = new ChatAdapter(contest, profails);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(ChatListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
