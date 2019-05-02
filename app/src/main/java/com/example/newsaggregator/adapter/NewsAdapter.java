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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private Cursor cursor;

    public NewsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView dateText;
        TextView newsText;

        NewsViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName);
            dateText = itemView.findViewById(R.id.tvDate);
            newsText = itemView.findViewById(R.id.tvNews);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String name = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        String date = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBDATE));
        String news = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));

        holder.nameText.setText(name);
        holder.dateText.setText(date);
        holder.newsText.setText(news);
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