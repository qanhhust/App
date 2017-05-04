package com.example.quanganh.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Quang Anh on 3/6/2017.
 */

public class ListChapAdapter extends RecyclerView.Adapter<ListChapAdapter.ViewHolder>{

    private List<Chap> list;
    private Context context;

    public ListChapAdapter(Context context, List<Chap> list) {
        this.context = context;
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View chapView = inflater.inflate(R.layout.list_chap_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(chapView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chap chap = this.list.get(position);
        TextView tvChap = holder.tvChap;
        tvChap.setText(chap.getDeName());
        CardView cardView = holder.cardView;
//        cardView.setCardBackgroundColor(Color.argb(64, 52, 73, 94));
        cardView.setCardBackgroundColor(Color.argb(64, 167, 200, 233));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvChap;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvChap = (TextView)itemView.findViewById(R.id.lcl_chap_name);
            cardView = (CardView)itemView.findViewById(R.id.lcl_card_view);
        }

    }

}
