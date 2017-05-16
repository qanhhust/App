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
public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        getActivity().setTitle("Danh sách yêu thích");

        adapter = new DatabaseAdapter(view.getContext());
        adapter.open();
        database = adapter.getDatabase();

        bundle = getActivity().getIntent().getBundleExtra("bundle");
        Account account = (Account) bundle.getSerializable("account");
        String query = "select kimdung.* " +
                        "from kimdung, favorite " +
                        "where favorite.stID = kimdung.stID " +
                        "and favorite.username = ?";
        String[] selectionArgs = new String[] { account.getUserName() };
        Cursor cursor = database.rawQuery(query, selectionArgs);
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

        recyclerView = (RecyclerView) view.findViewById(R.id.ff_list_favorite);
        listStoryAdapter = new ListStoryAdapter(view.getContext(), list);
        recyclerView.setAdapter(listStoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Story story = list.get(position);
                Bundle b = new Bundle();
                b.putSerializable("story", story);
                if (getActivity().getIntent().getBundleExtra("bundle") != null) {
                    Account account = (Account) getActivity().getIntent().getBundleExtra("bundle").getSerializable("account");
                    b.putSerializable("account", account);
                    Log.e("username", account.getUserName());
                    Log.e("password", account.getPassWord());
                }
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("bundle", b);
                startActivityForResult(intent, 1);
            }
        }));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            if (data != null) {
                boolean like = data.getBooleanExtra("like", true);
                if (!like) {
                    int id = data.getIntExtra("id", 0);
                    for (Story s : list) {
                        if (s.getStId() == id) {
                            list.remove(s);
                        }
                    }
                    listStoryAdapter = new ListStoryAdapter(view.getContext(), list);
                    recyclerView.setAdapter(listStoryAdapter);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private Bundle bundle;
    private List<Story> list;
    private DatabaseAdapter adapter;
    private SQLiteDatabase database;
    private View view;
    private ListStoryAdapter listStoryAdapter;
    private RecyclerView recyclerView;

}
