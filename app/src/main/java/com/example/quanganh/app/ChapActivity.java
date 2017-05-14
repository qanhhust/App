package com.example.quanganh.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ChapActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseAdapter adapter;
    SQLiteDatabase database;
    List<Chap> listChap;
    private Map<String, Integer> map;
    Story story;
    Chap chap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);

        init();

        new AlertDialog.Builder(ChapActivity.this)
                .setMessage("Bạn có muốn đọc tiếp không ?")
                .setIcon(R.drawable.info)
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (chap != null) {
                            Log.e("chap", chap.getDeName());
                            Intent intent = new Intent(ChapActivity.this, ReadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("line", map.get(chap.getDeContent()));
                            bundle.putSerializable("chap", chap);
                            intent.putExtra("bundle", bundle);
                            startActivityForResult(intent, 1);
                        } else {
                            dialog.cancel();
                            Log.e("chap", "null");
                        }
                    }
                })
                .show();
    }

    public void savingPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonString = gson.toJson(chap);
        editor.putString(story.getStName(), jsonString);

        JSONObject jsonObject = new JSONObject(map);
        jsonString = jsonObject.toString();
        editor.remove("map").commit();
        editor.putString("map", jsonString);

        editor.commit();
    }

    public void storingPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);

        map = new HashMap<>();
        try {
            Gson gson = new Gson();
            String jsonString = sharedPreferences.getString(story.getStName(), "");
            chap = gson.fromJson(jsonString, Chap.class);

            jsonString = sharedPreferences.getString("map", (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                int value = (Integer) jsonObject.get(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreference();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.ac_list_chap);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        story = (Story) bundle.getSerializable("story");

        setTitle(story.getStName());

        storingPreference();

        adapter = new DatabaseAdapter(getApplicationContext());
        adapter.open();
        database = adapter.getDatabase();
        String query = "select * from st_kimdung where stID = ?";
        String[] selectionArgs = new String[] { String.valueOf(story.getStId()) };
        Cursor cursor = database.rawQuery(query, selectionArgs);

        listChap = new ArrayList<>();
        while (cursor.moveToNext()) {
            Chap c = new Chap();
            c.setDeId(cursor.getInt(0));
            c.setDeName(cursor.getString(1));
            c.setDeSource(cursor.getString(2));
            c.setDeContent(cursor.getString(3));
            c.setStId(cursor.getInt(4));
            c.setDeDate(cursor.getString(5));
            listChap.add(c);
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
                startActivityForResult(intent, 1);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                int value = data.getBundleExtra("bundle").getInt("line");
                chap = (Chap) data.getBundleExtra("bundle").getSerializable("chap");
                map.put(chap.getDeContent(), value);
                Log.e("chap", chap.getDeName());
                Log.e("map", map.get(chap.getDeContent()) + "");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onKeyDown(keyCode, event);
    }
}
