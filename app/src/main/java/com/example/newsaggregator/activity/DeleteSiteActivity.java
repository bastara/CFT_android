package com.example.newsaggregator.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.adapter.NewsAdapter;
import com.example.newsaggregator.db.DBHelper;
import com.example.newsaggregator.db.NewsContract;

public class DeleteSiteActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private NewsAdapter mAdapter;
    private TextView mTextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_site);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        mTextName = findViewById(R.id.tvName);
        mTextViewAmount = findViewById(R.id.tvDate);

    }

    private void removeItem(long id) {
        mDatabase.delete(NewsContract.NewsEntry.TABLE_NEWS,
                NewsContract.NewsEntry.COLUMN_ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                NewsContract.NewsEntry.TABLE_NEWS,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_ID + " DESC"
        );
    }
}