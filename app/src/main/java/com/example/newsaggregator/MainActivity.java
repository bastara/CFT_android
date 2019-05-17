package com.example.newsaggregator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.newsaggregator.activity.WebActivity;
import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.worker.MyWorker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsaggregator.data.preference.Preference;
import com.example.newsaggregator.network.Update;
import com.example.newsaggregator.activity.AddSiteActivity;
import com.example.newsaggregator.activity.DeleteSiteActivity;
import com.example.newsaggregator.activity.NewsActivity;
import com.example.newsaggregator.activity.RefreshActivity;
import com.example.newsaggregator.adapter.NewsAdapter;

import java.net.ConnectException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsAdapter.ItemClickListener {

    private TextView refreshTextView;
    private TextView notificationTextView;

    private NewsAdapter adapter;
    private RecyclerView recyclerView;

    Preference preference;

    Handler handler;

    Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        setTheme(R.style.AppThemeLight);

//        LinearLayout linearLayout = findViewById(R.id.SSS);

//        NavigationView navigationVw = (NavigationView) findViewById(R.id.nav_view);
//        View hView =  navigationVw.getHeaderView(0);
//

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TypedValue tv = new TypedValue();
                int actionBarHeight = 0;
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                }

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics metricsB = new DisplayMetrics();
                display.getMetrics(metricsB);

                recyclerView.scrollBy(0, +metricsB.heightPixels - toolbar.getHeight() - actionBarHeight);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        MenuItem itemTimeRefresh = navigationView.getMenu()
                                                 .findItem(R.id.nav_refresh);
        MenuItem itemToggleNotification = navigationView.getMenu()
                                                        .findItem(R.id.nav_notification);
        refreshTextView = (TextView) itemTimeRefresh.getActionView();
        notificationTextView = (TextView) itemToggleNotification.getActionView();

        preference = new Preference(MainActivity.this);

//        initializeCountDrawer(timeRefreshMenu(preference.getTimeRefresh()), preference.getNotification());
//        drawer.openDrawer(GravityCompat.START);

        handler = new MyHandlerMainActivity(this);

        doWorkManager();

        switch (preference.getLastScreen()) {
            case "AddSiteActivity":
                Intent intent = new Intent(MainActivity.this,
                        AddSiteActivity.class);
                startActivity(intent);
                break;
            case "DeleteSiteActivity":
                intent = new Intent(MainActivity.this,
                        DeleteSiteActivity.class);
                startActivity(intent);
                break;
            case "WebActivity":
                intent = new Intent(MainActivity.this,
                        WebActivity.class);
                intent.putExtra("url", preference.getLastSite());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    private void refreshData() {
        initializeCountDrawer(timeRefreshMenu(preference.getTimeRefresh()), preference.getNotification());

//        DBRequest dbRequest = new DBRequest(MainActivity.this);

// почему так не создается экземпляр???
//        DBAdapter dbAdapter = new DBAdapter();
        DBAdapter dbAdapter = (DBAdapter) getApplication();

//        Cursor cursor = dbRequest.getCursorNews();
        Cursor cursor = dbAdapter.dbRequest.getCursorNews();


        recyclerView = findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, cursor);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        preference.setLastScreen("MainActivity");

        Log.d(Contract.Entry.TAG, "ONRESUME");

        if (listState != null) {
            Objects.requireNonNull(recyclerView.getLayoutManager())
                   .onRestoreInstanceState(listState);
        }
    }

    private void doWorkManager() {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder
                (MyWorker.class, preference.getTimeRefresh() / 60000, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                .addTag("TRSS")
                .build();
        WorkManager.getInstance()
                   .enqueueUniquePeriodicWork
                           ("Refresh News", ExistingPeriodicWorkPolicy.REPLACE, request);
    }

    private String timeRefreshMenu(int time) {
        String timeRefreshMenu;
        switch (time) {
            case 900000:
                timeRefreshMenu = "15min";
                break;
            case 3600000:
                timeRefreshMenu = "60min";
                break;
            case 21600000:
                timeRefreshMenu = "6h";
                break;
            default:
                timeRefreshMenu = "24h";
                break;
        }
        return timeRefreshMenu;
    }

    private void initializeCountDrawer(String timeView, boolean notificationView) {
        refreshTextView.setGravity(Gravity.CENTER_VERTICAL);
        refreshTextView.setTypeface(null, Typeface.BOLD);
        refreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        refreshTextView.setText(timeView);
        notificationTextView.setGravity(Gravity.CENTER_VERTICAL);
        notificationTextView.setTypeface(null, Typeface.BOLD);
        notificationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        if (notificationView) {
            notificationTextView.setText("вкл");
        } else {
            notificationTextView.setText("выкл");
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean fromNotification = false;

        if (id == R.id.nav_upDate) {
            Update update = new Update();
            try {
                update.upDate(MainActivity.this, true);
            } catch (ConnectException e) {
                Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT)
                     .show();
                e.printStackTrace();
            }
            //TODO не обновляется, потоки.
            //TODO возм надо новый поток
            onPause();
            onResume();
        } else if (id == R.id.nav_addSite) {
            Intent intent = new Intent(MainActivity.this,
                    AddSiteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_deleteSite) {
            Intent intent = new Intent(MainActivity.this,
                    DeleteSiteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_darkTheme) {

        } else if (id == R.id.nav_notification) {
            if (preference.getNotification()) {
                preference.setNotification(false);
                notificationTextView.setText("выкл");
            } else {
                preference.setNotification(true);
                notificationTextView.setText("вкл");
            }
            fromNotification = true;
        } else if (id == R.id.nav_refresh) {
            Intent questionIntent = new Intent(MainActivity.this,
                    RefreshActivity.class);
            startActivityForResult(questionIntent, 0);
        }

        if (!fromNotification) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void handleMessage(android.os.Message msg) {
        if (msg.what == 2) {
        } else if (msg.what == 1) {
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int timeRefreshMilli = Objects.requireNonNull(data.getExtras())
                                              .getInt(Contract.Entry.REFRESH_TIME);
                refreshTextView.setText(timeRefreshMenu(timeRefreshMilli));
                preference.setTimeRefresh(timeRefreshMilli);
                Log.d(Contract.Entry.TAG, "время обновления в настройки" + timeRefreshMilli);
            } else {
                refreshTextView.setText("");
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT)
             .show();
        Intent intent = new Intent(MainActivity.this,
                NewsActivity.class);
        intent.putExtra("id", String.valueOf(position));
        startActivity(intent);
    }

    public void onClickTop(MenuItem item) {
        recyclerView.smoothScrollToPosition(0);

    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        listState = Objects.requireNonNull(recyclerView.getLayoutManager())
                           .onSaveInstanceState();
        state.putParcelable(Contract.Entry.KEY_RECYCLER_STATE, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if (state != null)
            listState = state.getParcelable(Contract.Entry.KEY_RECYCLER_STATE);
    }

}



