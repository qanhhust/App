package com.example.quanganh.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class FindActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.af_list_find);
        String name = getIntent().getStringExtra("name");
        String find = getIntent().getStringExtra("find");
        setTitle(name);
        new Find().execute(name, find);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Integer> findIndex(String string, String source) {
        return findRegularExpression(string.toLowerCase().trim(), source.toLowerCase().trim());
    }

    private static List<Integer> findRegularExpression(String string, String source) {
        String s = format(string);
        String regex = "(" + s + ")+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<Integer> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.start());
            Log.e("index", String.valueOf(matcher.start()));
        }
        return list;
    }

    public static String format(String str) {
        String s = str.trim();
        String regex = "(\\s|\\t)+";
        StringBuilder builder = new StringBuilder();
        String[] split = s.split(regex);
        for (String st : split) {
            builder.append(st + " ");
        }
        return builder.toString();
    }

    private static List<Integer> find1(String string, String source) {
        String regex = "";
        return null;
    }

    class Find extends AsyncTask<String, Void, List<FindContent>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindActivity.this);
            progressDialog.setMessage("Đang tìm...");
            progressDialog.show();
            Log.e("Progess", "Show");
        }

        @Override
        protected List<FindContent> doInBackground(String... params) {
            String query = "select stID from kimdung where stName like ?";
            String[] selectionArgs = new String[] { params[0] };
            DatabaseAdapter adapter = new DatabaseAdapter(getApplicationContext());
            adapter.open();
            SQLiteDatabase database = adapter.getDatabase();
            Cursor cursor = database.rawQuery(query, selectionArgs);
            int id = 0;
            while (cursor.moveToNext()) {
                id = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
            query = "select decontent, deID, deName from st_kimdung where stID = ?";
            selectionArgs = new String[] { String.valueOf(id) };
            cursor = database.rawQuery(query, selectionArgs);
            List<String> content = new ArrayList<>();
            List<Integer> chapID = new ArrayList<>();
            List<String> chapName = new ArrayList<>();
            while (cursor.moveToNext()) {
                content.add(cursor.getString(0));
                chapID.add(cursor.getInt(1));
                chapName.add(cursor.getString(2));
            }
            cursor.close();
            adapter.close();
            List<FindContent> list = new ArrayList<>();
            String find = params[1];
            String[] split = find.split(" ");
            for (int i = 0; i < content.size(); ++i) {
                List<Integer> index = findIndex(find, content.get(i));
                for (int ind : index) {
                    String sub = content.get(i).substring(ind, ind + 30);
                    int chapId = chapID.get(i);
                    String name = chapName.get(i);
                    list.add(new FindContent(chapId, sub, name));
                    Log.e("content : ", content.get(i).substring(0, 30));
                    Log.e("sub : ", content.get(i).substring(ind, ind + 30));
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(final List<FindContent> list) {
            super.onPostExecute(list);
            if (list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemAnimator(new SlideInUpAnimator());
                recyclerView.setAdapter(new ListFindAdapter(getApplicationContext(), list));
                progressDialog.dismiss();
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        FindActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FindContent findContent = list.get(position);
                        DatabaseAdapter adapter = new DatabaseAdapter(getApplicationContext());
                        adapter.open();
                        SQLiteDatabase database = adapter.getDatabase();
                        Cursor cursor = database.rawQuery(
                                "select * from st_kimdung where deID = ?",
                                new String[] { String.valueOf(findContent.getDeID()) }
                        );
                        Chap chap = new Chap();
                        while (cursor.moveToNext()) {
                            chap.setDeId(cursor.getInt(0));
                            chap.setDeName(cursor.getString(1));
                            chap.setDeSource(cursor.getString(2));
                            chap.setDeContent(cursor.getString(3));
                            chap.setStId(cursor.getInt(4));
                            chap.setDeDate(cursor.getString(5));
                        }
                        cursor.close();
                        adapter.close();
                        Intent intent = new Intent(FindActivity.this, ReadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chap", chap);
                        intent.putExtra("bundle", bundle);
                        intent.putExtra("find", findContent.getContent());
                        intent.putExtra("search", true);
                        startActivity(intent);
//                        setResult(1, intent);
//                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }));
            } else {
                Toast.makeText(FindActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

}
