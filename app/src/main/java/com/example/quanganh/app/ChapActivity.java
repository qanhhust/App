package com.example.quanganh.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ChapActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseAdapter adapter;
    SQLiteDatabase database;
    List<Chap> listChap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.ac_list_chap);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        final Story story = (Story) bundle.getSerializable("story");

        setTitle(story.getStName());

        adapter = new DatabaseAdapter(getApplicationContext());
        adapter.open();
        database = adapter.getDatabase();
        String query = "select * from st_kimdung where stID = ?";
        String[] selectionArgs = new String[] { String.valueOf(story.getStId()) };
        Cursor cursor = database.rawQuery(query, selectionArgs);

        listChap = new ArrayList<>();
        while (cursor.moveToNext()) {
            Chap chap = new Chap();
            chap.setDeId(cursor.getInt(0));
            chap.setDeName(cursor.getString(1));
            chap.setDeSource(cursor.getString(2));
            chap.setDeContent(cursor.getString(3));
            chap.setStId(cursor.getInt(4));
            chap.setDeDate(cursor.getString(5));
            listChap.add(chap);
        }

        ListChapAdapter listChapAdapter = new ListChapAdapter(getApplicationContext(), listChap);
        recyclerView.setAdapter(listChapAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Chap chap = listChap.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chap", chap);
                Intent intent = new Intent(ChapActivity.this, ReadActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onKeyDown(keyCode, event);
    }
}
