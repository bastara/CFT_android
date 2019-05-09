package com.example.newsaggregator.adapter;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.data.db.NewsContract;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private Cursor cursor;
    private ItemClickListener itemClickListener;

    public NewsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        TextView dateText;
        TextView newsText;

        NewsViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName);
            dateText = itemView.findViewById(R.id.tvDate);
            newsText = itemView.findViewById(R.id.tvNews);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
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
        holder.dateText.setText(date.substring(0, 16));
        if (news.length() > 180) {
            news = news.substring(0, 180) + "...";
            holder.newsText.setText(news);
        } else {
            news = news + "...";
            holder.newsText.setText(news);
        }
    }

    public String getItem(int id) {
        cursor.moveToPosition(id);
        return cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ID));
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}