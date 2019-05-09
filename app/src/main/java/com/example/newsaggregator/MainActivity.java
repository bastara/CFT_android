package com.example.newsaggregator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;

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

import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.newsaggregator.data.db.DBHelper;
import com.example.newsaggregator.data.db.NewsContract;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsAdapter.ItemClickListener {


    //TODO проверить закрытие ресурсов.
    private TextView refreshTextView;
    private static final String TAG = "!REFRESH REFRESH_TIME";

    private SQLiteDatabase dataBase;
    private NewsAdapter adapter;

    private RecyclerView recyclerView;

    Preference preference;

    public static boolean newCursor = false;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics metricsB = new DisplayMetrics();
                display.getMetrics(metricsB);
                recyclerView.scrollBy(0, +metricsB.heightPixels - 176);
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
        refreshTextView = (TextView) itemTimeRefresh.getActionView();

        preference = new Preference(MainActivity.this);

        initializeCountDrawer(timeRefreshMenu(preference.getTimeRefresh()));

//        drawer.openDrawer(GravityCompat.START);

        DBHelper dbHelper = new DBHelper(this);
        dataBase = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, getAllItems());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        doWorkManager();
    }

    private void doWorkManager() {
        //TODO выставить сохраненное время.
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder
                (MyWorker.class, 1, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                .addTag("TRSS")
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork
                ("Refresh News", ExistingPeriodicWorkPolicy.REPLACE, request);
//        LiveData<WorkInfo> info = WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId());
//        info.observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(@Nullable WorkInfo workInfo) {
//                Log.d(MyWorker.TAG, "onChanged " + Thread.currentThread().getName() + " " + workInfo.getState());
//            }
//        });

    }



    private String timeRefreshMenu(String time1111) {
        String timeRefreshMenu;
        switch (time1111) {
            case "900000":
                timeRefreshMenu = "15min";
                break;
            case "3600000":
                timeRefreshMenu = "60min";
                break;
            case "21600000":
                timeRefreshMenu = "6h";
                break;
            default:
                timeRefreshMenu = "24h";
                break;
        }
        return timeRefreshMenu;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (newCursor) {
            Intent i = new Intent(this, this.getClass());
            finish();
            newCursor = false;
            this.startActivity(i);
        }
    }

    private void initializeCountDrawer(String timeView) {
        refreshTextView.setGravity(Gravity.CENTER_VERTICAL);
        refreshTextView.setTypeface(null, Typeface.BOLD);
        refreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        refreshTextView.setText(timeView);
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

        if (id == R.id.nav_upDate) {
//            Update.upDate(MainActivity.this);
//            Intent i = new Intent(this, this.getClass());
//            finish();
//            newCursor = false;
//            this.startActivity(i);

            Update update = new Update();
            update.upDate(MainActivity.this);
            Intent i = new Intent(this, this.getClass());
            finish();
            newCursor = false;
            this.startActivity(i);

        } else if (id == R.id.nav_addSite) {
            Intent intent = new Intent(MainActivity.this,
                    AddSiteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_deleteSite) {
            Intent intent = new Intent(MainActivity.this,
                    DeleteSiteActivity.class);
            startActivity(intent);
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
                String timeRefreshMilli = data.getStringExtra(RefreshActivity.REFRESH_TIME);
                refreshTextView.setText(timeRefreshMenu(timeRefreshMilli));
                preference.setTimeRefresh(timeRefreshMilli);
                Log.d(TAG, timeRefreshMilli);
            } else {
                refreshTextView.setText(""); // стираем текст
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,
                NewsActivity.class);
        intent.putExtra("id", String.valueOf(position));
        startActivity(intent);
    }

    private Cursor getAllItems() {
        return dataBase.query(
                NewsContract.NewsEntry.TABLE_NEWS,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_ID + " DESC"
        );
    }

    public void onClickTop(MenuItem item) {
        recyclerView.scrollBy(0, -10000000);
    }
}