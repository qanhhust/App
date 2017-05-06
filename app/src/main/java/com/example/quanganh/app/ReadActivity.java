package com.example.quanganh.app;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanganh.app.rule.Rule;
import com.example.quanganh.app.rule.Rule1;
import com.example.quanganh.app.rule.Rule2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class ReadActivity extends AppCompatActivity {

    WebView wvContent;
    Chap chap;
    Bundle bundle;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        wvContent = (WebView) findViewById(R.id.ar_content);
        wvContent.setBackgroundColor(Color.TRANSPARENT);

        bundle = getIntent().getBundleExtra("bundle");
        chap = (Chap) bundle.getSerializable("chap");
        Log.e("search", "false");
        setTitle(chap.getDeName());

        String content = "<html><body style=\"color: white; font-size: 15px;\">" + chap.getDeContent() + "</body></html>";
        content = content.trim();
        content = format(content);
        wvContent.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

        boolean search = getIntent().getBooleanExtra("search", false);
        if (search) {
            Log.e("search", "true");
            String find = getIntent().getStringExtra("find");
            wvContent.findAllAsync(find);
            try {
                Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                m.invoke(wvContent, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.action_search:
                dialog = new Dialog(ReadActivity.this, android.R.style.Theme_Holo_Light_Dialog);
                dialog.setContentView(R.layout.search_dialog);
                dialog.setTitle("Tìm kiếm");

                Button btnSearch = (Button) dialog.findViewById(R.id.search_dialog_button_search);
                Button btnExit = (Button) dialog.findViewById(R.id.search_dialog_button_exit);
                ImageButton speechName = (ImageButton) dialog.findViewById(R.id.search_dialog_speech_name);
                ImageButton speechContent = (ImageButton) dialog.findViewById(R.id.search_dialog_speech_content);

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String storyName = ((EditText) dialog.findViewById(R.id.search_dialog_edit_text_story_name)).getText().toString();
                        String paragraph = ((EditText) dialog.findViewById(R.id.search_dialog_edit_text_paragraph)).getText().toString();
                        if (storyName.equals("")) {
                            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên truyện", Toast.LENGTH_SHORT).show();
                        } else if (paragraph.equals("")) {
                            Toast.makeText(getApplicationContext(), "Bạn chưa nhập đoạn cần tìm", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(ReadActivity.this, FindActivity.class);
                            intent.putExtra("name", storyName);
                            intent.putExtra("find", paragraph);
                            startActivity(intent);
//                            startActivityForResult(intent, 1);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                speechName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptSpeechInput(1);
                    }
                });
                speechContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptSpeechInput(2);
                    }
                });
                dialog.show();
                break;
            case R.id.action_check_syntax:
                Rule[] rule = new Rule[2];
                rule[0] = new Rule1();
                rule[1] = new Rule2();
                String s = chap.getDeContent();
                s = s.replaceAll("<br />", " ");
                Log.e("content", s.substring(100, 200));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptSpeechInput(int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(ReadActivity.this, "Sorry! Your device doesn't support speech language", Toast.LENGTH_SHORT).show();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ((EditText) dialog.findViewById(R.id.search_dialog_edit_text_story_name)).setText(result.get(0));
                }
                break;
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ((EditText) dialog.findViewById(R.id.search_dialog_edit_text_paragraph)).setText(result.get(0));
                }
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("result", "result");
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == 1) {
//            if (data != null) {
//                chap = (Chap) data.getBundleExtra("bundle").getSerializable("chap");
//                setTitle(chap.getDeName());
//                String content = "<html><body style=\"color: white; font-size: 15px;\">" + chap.getDeContent() + "</body></html>";
//                wvContent.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//
//                String find = data.getStringExtra("find");
//                wvContent.findAllAsync(find);
//                try {
//                    Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
//                    m.invoke(wvContent, true);
//                    Log.e("ádf", "ádfasdf");
//                } catch (Exception e) {
//                    Log.e("Error", "Lỗi");
//                }
//            } else {
//                Log.e("data : ", "null");
//            }
//        }
//    }
}
