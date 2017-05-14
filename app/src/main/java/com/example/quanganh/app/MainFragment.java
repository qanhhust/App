package com.example.quanganh.app;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Trang chủ");

        adapter = new DatabaseAdapter(view.getContext());
        adapter.open();
        database = adapter.getDatabase();

        Cursor cursor = database.query("kimdung", null, null, null, null, null, null);
        list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Story story = new Story(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            list.add(story);
        }
        cursor.close();

        recyclerView = (RecyclerView) view.findViewById(R.id.fm_list_story);
        listStoryAdapter = new ListStoryAdapter(view.getContext(), list);
        recyclerView.setAdapter(listStoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Story story = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("story", story);
                if (getActivity().getIntent().getBundleExtra("bundle") != null) {
                    Account account = (Account) getActivity().getIntent().getBundleExtra("bundle").getSerializable("account");
                    bundle.putSerializable("account", account);
                    Log.e("username", account.getUserName());
                    Log.e("password", account.getPassWord());
                }
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }));

        return view;
    }

    private List<Story> list;
    private DatabaseAdapter adapter;
    private SQLiteDatabase database;
    private View view;
    private ListStoryAdapter listStoryAdapter;
    private RecyclerView recyclerView;
}
