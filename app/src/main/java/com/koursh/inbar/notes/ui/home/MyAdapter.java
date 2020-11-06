package com.koursh.inbar.notes.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.koursh.inbar.notes.Database.PublicDatabase;
import com.koursh.inbar.notes.NoteActivity;
import com.koursh.inbar.notes.R;

import java.util.LinkedList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    final PublicDatabase database;
    final View view;
    private LinkedList<String> titles;
    private LinkedList<Long> ids;
    private long pending_delete = -1;
    private String pending_delete_title = null;

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        return new MyViewHolder(v);
    }

    private int pos = -1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(LinkedList<String> titles, LinkedList<Long> ids, PublicDatabase database, View view) {
        update(titles, ids);
        this.database = database;
        this.view = view;
    }

    public void update(LinkedList<String> titles, LinkedList<Long> ids) {
        this.titles = titles;
        this.ids = ids;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(titles.get(position));
        holder.bindListener(ids.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void deleteItem(int position) {
        if (pending_delete != -1) {
            database.delete(pending_delete);
        }
        pending_delete = ids.get(position);
        pending_delete_title = titles.get(position);
        pos = position;
        titles.remove(position);
        ids.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(view, "Note deleted succesfully",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids.add(pos,
                        pending_delete);
                titles.add(pos, pending_delete_title);
                notifyItemInserted(pos);
                pending_delete = -1;
            }
        });
        snackbar.show();

        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (pending_delete != -1) {
                    database.delete(pending_delete);
                }
            }
        });

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final View master;
        // each data item is just a string in this case
        public TextView title;

        public MyViewHolder(View v) {
            super(v);
            master = v;
            title = v.findViewById(R.id.title);
        }

        public void bindListener(final long id) {
            master.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NoteActivity.class);
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


}

