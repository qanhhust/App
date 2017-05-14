package com.example.quanganh.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quang Anh on 3/1/2017.
 */

public class ListStoryAdapter extends RecyclerView.Adapter<ListStoryAdapter.ViewHolder> {

    private List<Story> list;
    private Context context;

    public ListStoryAdapter(Context context, List<Story> list) {
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
        View storyView = inflater.inflate(R.layout.list_story_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(storyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Story story = this.list.get(position);
        ImageView imageView = holder.imageView;
        imageView.setImageResource(getContext().getResources().getIdentifier(story.getStImage(), "drawable", getContext().getPackageName()));
        TextView tvName = holder.tvName;
        tvName.setText(story.getStName());
//        TextView tvAuthor = holder.tvAuthor;
//        tvAuthor.setText("Tác giả : Kim Dung");
//        TextView tvDescribe = holder.tvDescribe;
//        String text = story.getStDescribe();
//        text = text.replaceAll("<br />", "\n");
//        text = text.replaceAll("<p>", "");
//        text = text.replaceAll("</p>", "");
//        text = text.replaceAll("<span>", "");
//        text = text.replaceAll("</span>", "");
//        tvDescribe.setText("Nội dung : " + text);
        CardView cardView = holder.cardView;
//        cardView.setCardBackgroundColor(Color.argb(64, 52, 73, 94));
        cardView.setCardBackgroundColor(Color.argb(64, 206, 230, 255));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(List<Story> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvAuthor, tvDescribe;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.lsl_image);
            tvName = (TextView)itemView.findViewById(R.id.lsl_name);
//            tvAuthor = (TextView)itemView.findViewById(R.id.lsl_author);
//            tvDescribe = (TextView)itemView.findViewById(R.id.lsl_describe);
            cardView = (CardView)itemView.findViewById(R.id.lsl_card_view);
        }

    }

}
