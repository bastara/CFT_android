package com.example.newsaggregator.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.adapter.SiteAdapter;
import com.example.newsaggregator.data.db.NewsContract;

public class DeleteSiteActivity extends AppCompatActivity {

    private SQLiteDatabase dataBase;
    private SiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_site);
        DBAdapter dbAdapter = (DBAdapter) this.getApplication();
        dataBase = dbAdapter.getDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SiteAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String cursor = adapter.getItem(viewHolder.getAdapterPosition());
                removeItem(cursor);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void removeItem(String str) {
        dataBase.delete(NewsContract.NewsEntry.TABLE_SITES, NewsContract.NewsEntry.COLUMN_URL + "=?", new String[]{String.valueOf(str)});
        dataBase.delete(NewsContract.NewsEntry.TABLE_NEWS, NewsContract.NewsEntry.COLUMN_URL + "=?", new String[]{String.valueOf(str)});
        MainActivity.newCursor = true;
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        DBAdapter dbAdapter = (DBAdapter) this.getApplication();
        return dbAdapter.getCursorAllItem();
    }
}