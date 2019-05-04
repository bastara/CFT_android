package com.example.newsaggregator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.db.NewsContract;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteViewHolder> {
    private Context context;
    private Cursor cursor;

    public SiteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    class SiteViewHolder extends RecyclerView.ViewHolder {
        TextView linkText;

        SiteViewHolder(View itemView) {
            super(itemView);

            linkText = itemView.findViewById(R.id.tvSite);
        }
    }

    @NonNull
    @Override
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.site_item, parent, false);
        return new SiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String link = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_LINK_SITE));
        holder.linkText.setText(link);
    }

    public String getItem(int id) {
        cursor.moveToPosition(id);
        return cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ID_SITE));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }

        cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}