package com.example.quanganh.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by QuangAnh on 5/2/2017.
 */

public class ListFindAdapter extends RecyclerView.Adapter<ListFindAdapter.ViewHolder> {

    private List<FindContent> list;
    private Context context;

    public ListFindAdapter(Context context, List<FindContent> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.list_find_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FindContent findContent = list.get(position);
        TextView tvName = holder.tvName;
        tvName.setText(String.valueOf(findContent.getChapName()));
        final WebView wvContent = holder.wvContent;
        String content = "<html><body style=\"color: white; font-size: 15px;\">" + findContent.getContent() + "</body></html>";
        wvContent.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        wvContent.setBackgroundColor(Color.TRANSPARENT);
        CardView cardView = holder.cardView;
        cardView.setCardBackgroundColor(Color.argb(64, 167, 200, 233));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        WebView wvContent;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.chap_name);
            wvContent = (WebView) itemView.findViewById(R.id.content_find);
            cardView = (CardView) itemView.findViewById(R.id.lfl_card_view);
        }
    }
}
