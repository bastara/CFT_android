package com.example.newsaggregator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mRefreshTextView;
    private String timeRefresh = "15mTn";
    private static final String TAG = "!REFRESH TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        https://ru.stackoverflow.com/questions/521780/Цвет-иконок-в-выезжающем-меню
        navigationView.setItemIconTintList(null);

        MenuItem itemTimeRefresh = navigationView.getMenu().findItem(R.id.nav_refresh);
        mRefreshTextView = (TextView) itemTimeRefresh.getActionView();

        initializeCountDrawer(timeRefresh);
        drawer.openDrawer(GravityCompat.START);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_TIME_REFRESH)) {
            // Получаем число из настроек
            timeRefresh = mSettings.getString(APP_PREFERENCES_TIME_REFRESH, String.valueOf(0));

            Log.d(TAG, "получили " + timeRefresh);
            // Выводим на экран данные из настроек
            mRefreshTextView.setText(timeRefresh);
        }
    }

    private void initializeCountDrawer(String timeView) {
        mRefreshTextView.setGravity(Gravity.CENTER_VERTICAL);
        mRefreshTextView.setTypeface(null, Typeface.BOLD);
        mRefreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        mRefreshTextView.setText(timeView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upDate) {
            // Handle the camera action
        } else if (id == R.id.nav_addSite) {
            Intent intent = new Intent(MainActivity.this,
                    AddSiteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_deleteSite) {

        } else if (id == R.id.nav_darkTheme) {

        } else if (id == R.id.nav_refresh) {
            Intent questionIntent = new Intent(MainActivity.this,
                    RefreshActivity.class);
            startActivityForResult(questionIntent, 0);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String thiefname = data.getStringExtra(RefreshActivity.THIEF);
                mRefreshTextView.setText(thiefname);
                timeRefresh = thiefname;
                Log.d(TAG, timeRefresh);
            } else {
                mRefreshTextView.setText(""); // стираем текст
            }
        }
    }


    // это имя файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    //время обновления
    public static final String APP_PREFERENCES_TIME_REFRESH = "refresher";
    //Создаём переменную, представляющую экземпляр класса SharedPreferences,
    // который отвечает за работу с настройками:
    private SharedPreferences mSettings;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "записываем " + timeRefresh);
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_TIME_REFRESH, timeRefresh);
        editor.apply();
    }

}
