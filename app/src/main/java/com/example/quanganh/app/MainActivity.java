package com.example.quanganh.app;

import android.app.Application;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragment mainFragment = null;
    private FavoriteFragment favoriteFragment = null;
    private boolean isLogin = false;
    private NavigationView navigationView;
    private Account account = null;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(getApplicationContext(), MainFragment.class);
        setIntent(intent);

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, mainFragment, "main_fragment")
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(4).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                if (!mainFragment.isVisible()) {
                    if (isLogin) {
                        intent = new Intent(this, MainFragment.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("account", account);
                        intent.putExtra("bundle", bundle);
                        setIntent(intent);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_main, mainFragment, "main_fragment")
                            .commit();
                }
                break;
            case R.id.nav_favorite:
                if (isLogin) {
                    if (favoriteFragment == null) {
                        favoriteFragment = new FavoriteFragment();
                        intent = new Intent(this, FavoriteFragment.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("account", account);
                        intent.putExtra("bundle", bundle);
                        setIntent(intent);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, favoriteFragment, "favorite_fragment")
                                .commit();
                    } else if (!favoriteFragment.isVisible()) {
                        intent = new Intent(this, FavoriteFragment.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("account", account);
                        intent.putExtra("bundle", bundle);
                        setIntent(intent);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, favoriteFragment, "favorite_fragment")
                                .commit();
                    }
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Bạn chưa đăng nhập")
                            .setIcon(R.drawable.info)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
                break;
            case R.id.nav_info:
                break;
            case R.id.nav_log_in:
                logIn();
                break;
            case R.id.nav_log_out:
                logOut();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Bạn có muốn thoát không?")
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
                            finish();
                        }
                    })
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void logOut() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn thoát không ?")
                .setIcon(R.drawable.info)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLogin = false;
                        TextView tv = (TextView) MainActivity.this.navigationView.getHeaderView(0).findViewById(R.id.nhm_username);
                        tv.setText("Đăng nhập");
                        intent = new Intent(getApplicationContext(), MainFragment.class);
                        account = null;
                        Bundle bundle = null;
                        intent.putExtra("bundle", bundle);
                        MainActivity.this.setIntent(intent);
                        navigationView.getMenu().getItem(3).setVisible(true);
                        navigationView.getMenu().getItem(4).setVisible(false);
                        if (!mainFragment.isVisible()) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, mainFragment, "main_fragment")
                                    .addToBackStack("main_fragment")
                                    .commit();
                        }
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void logIn() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog);
        dialog.setContentView(R.layout.log_in_dialog);
        dialog.setTitle("Đăng nhập");

        Button btnLogin = (Button) dialog.findViewById(R.id.lid_btn_log_in);
        Button btnRegister = (Button) dialog.findViewById(R.id.lid_btn_register);
        Button btnExit = (Button) dialog.findViewById(R.id.lid_btn_exit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = (EditText) dialog.findViewById(R.id.lid_username);
                EditText etPassword = (EditText) dialog.findViewById(R.id.lid_password);
                DatabaseAdapter adapter = new DatabaseAdapter(MainActivity.this);
                adapter.open();
                SQLiteDatabase database = adapter.getDatabase();
                String query = "select username, password from account where username = ? and password = ?";
                String[] selectionArgs = new String[]{etUsername.getText().toString(), etPassword.getText().toString()};
                Cursor cursor = database.rawQuery(query, selectionArgs);
                int count = 0;
                while (cursor.moveToNext()) {
                    count++;
                }
                cursor.close();
                database.close();
                if (count == 1) {
                    isLogin = true;
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    TextView tv = (TextView) MainActivity.this.navigationView.getHeaderView(0).findViewById(R.id.nhm_username);
                    tv.setText(etUsername.getText().toString());
                    intent = new Intent(getApplicationContext(), MainFragment.class);
                    account = new Account(etUsername.getText().toString(), etPassword.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("account", account);
                    intent.putExtra("bundle", bundle);
                    MainActivity.this.setIntent(intent);
                    MainActivity.this.navigationView.getMenu().getItem(3).setVisible(false);
                    MainActivity.this.navigationView.getMenu().getItem(4).setVisible(true);
                } else {
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogRes = new Dialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog);
                dialogRes.setContentView(R.layout.register_dialog);
                dialogRes.setTitle("Đăng ký tài khoản");

                Button resBtnRegister = (Button) dialogRes.findViewById(R.id.res_btn_register);
                Button resBtnExit = (Button) dialogRes.findViewById(R.id.res_btn_exit);

                resBtnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText resUserName = (EditText) dialogRes.findViewById(R.id.res_username);
                        EditText resPassWord = (EditText) dialogRes.findViewById(R.id.res_password);
                        EditText resConfirm = (EditText) dialogRes.findViewById(R.id.res_confirm_password);
                        EditText resPhone = (EditText) dialogRes.findViewById(R.id.res_phone);

                        DatabaseAdapter adapter = new DatabaseAdapter(MainActivity.this);
                        adapter.open();
                        SQLiteDatabase database = adapter.getDatabase();
                        Cursor cursor = database.rawQuery("select username from account where username = ?",
                                new String[] { resUserName.getText().toString() });
                        int count = 0;
                        while (cursor.moveToNext()) {
                            count++;
                        }
                        cursor.close();
                        if (!resUserName.getText().toString().equals("")
                                || !resPassWord.getText().toString().equals("")
                                || !resConfirm.getText().toString().equals("")
                                || !resPhone.getText().toString().equals("")) {
                            if (count > 0) {
                                Toast.makeText(MainActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!resPassWord.getText().toString().equals(resConfirm.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Xác nhận mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                                } else {
                                    ContentValues values = new ContentValues();
                                    values.put("username", resUserName.getText().toString());
                                    values.put("password", resPassWord.getText().toString());
                                    values.put("phone", resPhone.getText().toString());
                                    database.insert("account", null, values);
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Bạn chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                resBtnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogRes.cancel();
                    }
                });

                dialogRes.show();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
