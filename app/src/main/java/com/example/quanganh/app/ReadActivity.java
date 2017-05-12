package com.example.quanganh.app;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ReadActivity extends AppCompatActivity {

    TextView tvContent;
    ScrollView svContent;
    Chap chap;
    Bundle bundle;
    Dialog dialog;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvContent = (TextView) findViewById(R.id.ar_content);
        svContent = (ScrollView) findViewById(R.id.ar_scroll_view);

        bundle = getIntent().getBundleExtra("bundle");
        chap = (Chap) bundle.getSerializable("chap");
        Log.e("search", "false");
        setTitle(chap.getDeName());

        content = format(chap.getDeContent());
        tvContent.setText(content);

        boolean search = getIntent().getBooleanExtra("search", false);
        if (search) {
            Log.e("search", "true");
            final String find = getIntent().getStringExtra("find");
            int index = content.indexOf(find);
            Spannable spannable = new SpannableString(tvContent.getText());
            if (index != -1) {
                spannable.setSpan(new BackgroundColorSpan(0xFFF9740E), index, index + find.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvContent.setText(spannable, TextView.BufferType.SPANNABLE);
                svContent.post(new Runnable() {
                    @Override
                    public void run() {
                        int indexOfWord = tvContent.getText().toString().indexOf(find);
                        int line = 0;
                        if (tvContent.getLayout().getLineForOffset(indexOfWord) - 10 < 0) {
                            line = tvContent.getLayout().getLineForOffset(indexOfWord);
                        } else {
                            line = tvContent.getLayout().getLineForOffset(indexOfWord) - 10;
                        }
                        int y = tvContent.getLayout().getLineTop(line);
                        svContent.scrollTo(0, y);
                    }
                });
            }
        }
    }

    private String format(String str) {
        String s = str;
        s = s.replaceAll("<br/>", "\n");
        s = s.replaceAll("<br />", "\n");
        s = s.replaceAll("<td>", " ");
        s = s.replaceAll("</td>", " ");
        s = s.replaceAll("<span>", " ");
        s = s.replaceAll("</span>", " ");
        s = s.replaceAll("<p>", "\n");
        s = s.replaceAll("</p>", "\n");
        return s;
    }

    public String format1(String str) {
        String s = str;
        s = s.trim();
        String regex = "-|\\.|,|;|\\\\|\\[|\\]|\\{|\\}|\\(|\\)|\\*|\\+|\\?|\\^|\\$|\\||:|\"|\'|!";
        s = s.replaceAll(regex, " ");
        regex = "(\\s|\\t)+";
        StringBuilder builder = new StringBuilder();
        String[] split = s.split(regex);
        for (String sp : split) {
            builder.append(sp);
            builder.append(" ");
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
                String con = format1(content);
                String[] split = con.split(" ");
                Log.e("content : ", con.substring(con.length() - 1000));
                for (String s : split) {
                    checkSyntax(s);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkSyntax(String str) {
        Rule[] rule = getRule();
        for (int i = 0; i < rule.length; ++i) {
            if (rule[i] != null) {
                if (!rule[i].checkValid(str)) {
                    System.err.println(str);
                    int index = content.indexOf(str);
                    Spannable spannable = new SpannableString(tvContent.getText());
                    if (index != -1) {
                        spannable.setSpan(new BackgroundColorSpan(0xFFF9740E), index, index + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvContent.setText(spannable, TextView.BufferType.SPANNABLE);
                    }
                    rule[i].show();
                }
            }
        }
    }

    private Rule[] getRule() {
        Rule[] rule = new Rule[33];
        rule[0] = new Rule1();
        rule[1] = new Rule2();
        rule[2] = new Rule3();
        rule[3] = new Rule4();
        rule[4] = new Rule5();
        rule[5] = new Rule6();
        rule[6] = new Rule7();
        rule[7] = new Rule8();
        rule[8] = new Rule9();
        rule[9] = new Rule10();
        rule[10] = new Rule11();
        rule[11] = new Rule12();
        rule[12] = new Rule13();
        rule[13] = new Rule14();
        rule[14] = new Rule15();
        rule[15] = new Rule16();
        rule[16] = new Rule17();
        rule[17] = new Rule18();
        rule[18] = new Rule19();
        rule[19] = new Rule20();
        rule[20] = new Rule21();
        rule[21] = new Rule22();
        rule[22] = new Rule23();
        rule[23] = new Rule24();
        rule[24] = new Rule25();
        rule[25] = new Rule26();
        rule[26] = new Rule27();
        rule[27] = new Rule28();
        rule[28] = new Rule29();
        rule[29] = new Rule30();
        rule[30] = new Rule31();
        return rule;
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
}
