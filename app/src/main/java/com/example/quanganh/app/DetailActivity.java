package com.example.quanganh.app;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseAdapter adapter;
    SQLiteDatabase database;
    ImageView img, imgFavorite;
    TextView tvName, tvNum;
    WebView wvDescribe;
    Button btnRead;

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
        wvDescribe = (WebView) findViewById(R.id.ad_describe);
        btnRead = (Button) findViewById(R.id.ad_btn_read);

        adapter = new DatabaseAdapter(getApplicationContext());
        adapter.open();
        database = adapter.getDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        final Story story = (Story) bundle.getSerializable("story");
        final Account account = (Account) bundle.getSerializable("account");
        setTitle(story.getStName());
        tvName.setText(story.getStName());
        wvDescribe.setBackgroundColor(Color.TRANSPARENT);
        String describe = "<html><body style=\"color: white; font-size: 15px;\">" + story.getStDescribe() + "</body></html>";
        wvDescribe.loadDataWithBaseURL(null, describe, "text/html", "utf-8", null);
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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onKeyDown(keyCode, event);
    }

    public int getMipmapResIdByName(String resName)  {
        String pkgName = getPackageName();
        int resID = getResources().getIdentifier(resName , "drawable", pkgName);
        return resID;
    }

}
