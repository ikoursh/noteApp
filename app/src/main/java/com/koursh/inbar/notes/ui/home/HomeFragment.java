package com.koursh.inbar.notes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koursh.inbar.notes.Database.Note;
import com.koursh.inbar.notes.Database.PublicDatabase;
import com.koursh.inbar.notes.NoteActivity;
import com.koursh.inbar.notes.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        final PublicDatabase database = PublicDatabase.getPublicDatabaseInstance(root.getContext());
        List<Note> notes = database.getAllNotes();
        String[] titles = new String[notes.size()];
        long[] ids = new long[titles.length];
        for (int i = 0; i < notes.size(); i++) {
            titles[i] = notes.get(i).title;
            ids[i] = notes.get(i).nid;
        }
        mAdapter = new MyAdapter(titles, ids);
        recyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), NoteActivity.class);
                intent.putExtra("id", database.insert(new Note()));
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        PublicDatabase database = PublicDatabase.getPublicDatabaseInstance(getContext()); //TODO: make func
        List<Note> notes = database.getAllNotes();
        String[] titles = new String[notes.size()];
        long[] ids = new long[titles.length];
        for (int i = 0; i < notes.size(); i++) {
            titles[i] = notes.get(i).title;
            ids[i] = notes.get(i).nid;
        }

        mAdapter.update(titles, ids);
        mAdapter.notifyDataSetChanged();
    }
}