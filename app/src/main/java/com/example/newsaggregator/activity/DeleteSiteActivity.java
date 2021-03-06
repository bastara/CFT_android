package com.example.newsaggregator.activity;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.widget.Toast;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.MyApplication;
import com.example.newsaggregator.R;
import com.example.newsaggregator.adapter.SiteAdapter;
import com.example.newsaggregator.data.preference.Preference;

public class DeleteSiteActivity extends AppCompatActivity {

    private MyApplication myApplication;

    private SiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Preference preference = new Preference(DeleteSiteActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);

        preference = new Preference(DeleteSiteActivity.this);
        preference.setLastScreen("DeleteSiteActivity");

        setContentView(R.layout.activity_delete_site);

        myApplication = (MyApplication) getApplication();

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
        myApplication.getDatabase()
                     .delete(Contract.Entry.TABLE_SITES, Contract.Entry.COLUMN_URL + "=?", new String[]{String.valueOf(str)});
        myApplication.getDatabase()
                     .delete(Contract.Entry.TABLE_NEWS, Contract.Entry.COLUMN_URL + "=?", new String[]{String.valueOf(str)});
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return myApplication.dbRequest.getCursorAllItem();
    }
}