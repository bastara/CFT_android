package com.example.newsaggregator.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsaggregator.R;
import com.example.newsaggregator.adapter.SiteAdapter;
import com.example.newsaggregator.db.DBHelper;
import com.example.newsaggregator.db.NewsContract;

public class DeleteSiteActivity extends AppCompatActivity {
    private SQLiteDatabase dataBase;
    private SiteAdapter adapter;
    private TextView linkSite;
//    private TextView mTextViewAmount;
//    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_site);

        DBHelper dbHelper = new DBHelper(this);
        dataBase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SiteAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getApplicationContext(), "DDDDDDDDD", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String cursor = adapter.getItem(viewHolder.getAdapterPosition());
                removeItem(Integer.parseInt(cursor));
            }
        }).attachToRecyclerView(recyclerView);

        linkSite = findViewById(R.id.tvSite);
    }

    private void removeItem(int id) {
        dataBase.delete(NewsContract.NewsEntry.TABLE_SITES,
                NewsContract.NewsEntry.COLUMN_ID_SITE + "=" + id, null);
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return dataBase.query(
                NewsContract.NewsEntry.TABLE_SITES,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_LINK_SITE
        );
    }
}