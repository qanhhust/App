package com.example.quanganh.app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    DatabaseAdapter adapter;
    SQLiteDatabase database;
    ImageView img, imgFavorite;
    TextView tvName, tvNum, tvDescribe;
    Button btnRead;
    Account account;
    Story story;
    boolean like = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        img = (ImageView) findViewById(R.id.ad_image);
        imgFavorite = (ImageView) findViewById(R.id.ad_favorite);
        tvName = (TextView) findViewById(R.id.ad_name);
        tvNum = (TextView) findViewById(R.id.ad_num_chap);
        tvDescribe = (TextView) findViewById(R.id.ad_describe);
        btnRead = (Button) findViewById(R.id.ad_btn_read);

        adapter = new DatabaseAdapter(getApplicationContext());
        adapter.open();
        database = adapter.getDatabase();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        story = (Story) bundle.getSerializable("story");
        account = (Account) bundle.getSerializable("account");

        if (account != null) {
            if (isFavorite() == 1) {
                imgFavorite.setImageResource(android.R.drawable.star_on);
                like = true;
            }
        }

        setTitle(story.getStName());
        tvName.setText(story.getStName());
        tvDescribe.setText(Html.fromHtml(story.getStDescribe(), 1), TextView.BufferType.SPANNABLE);
        img.setImageResource(getMipmapResIdByName(story.getStImage()));

        Cursor cursor = database.rawQuery(
                "select count(stID) from st_kimdung where stID = ?",
                new String[] {String.valueOf(story.getStId())}
        );
        while (cursor.moveToNext()) {
            tvNum.setText(cursor.getString(0));
        }
        cursor.close();

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account != null) {
                    if (like) {
                        database.delete("favorite", "stID = ?", new String[] { String.valueOf(story.getStId()) });
                        imgFavorite.setImageResource(android.R.drawable.star_off);
                        like = false;
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("username", account.getUserName());
                        values.put("stID", story.getStId());
                        database.insert("favorite", null, values);
                        imgFavorite.setImageResource(android.R.drawable.star_on);
                        like = true;
                    }
                } else {
                    new AlertDialog.Builder(DetailActivity.this)
                            .setIcon(R.drawable.info)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setTitle("Thông báo")
                            .setMessage("Bạn chưa đăng nhập!")
                            .show();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ChapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("story", story);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    private int isFavorite() {
        int count = 0;

        Cursor cursor = database.rawQuery("select * from favorite where username = ? and stID = ?",
                new String[] { account.getUserName(), String.valueOf(story.getStId()) });
        while (cursor.moveToNext()) {
            count++;
        }
        cursor.close();

        return count;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("id", story.getStId());
                intent.putExtra("like", like);
                setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("id", story.getStId());
            intent.putExtra("like", like);
            setResult(1, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public int getMipmapResIdByName(String resName)  {
        String pkgName = getPackageName();
        int resID = getResources().getIdentifier(resName , "drawable", pkgName);
        return resID;
    }

}
