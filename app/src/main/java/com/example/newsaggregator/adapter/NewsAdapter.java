package com.example.newsaggregator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.db.NewsContract;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public NewsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView dateText;
        public TextView newsText;

        public NewsViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.tvName);
            dateText = itemView.findViewById(R.id.tvDate);
            newsText = itemView.findViewById(R.id.tvNews);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        String date = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBDATE));
        String news = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));

        holder.nameText.setText(name);
        holder.dateText.setText(date);
        holder.newsText.setText(news);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}