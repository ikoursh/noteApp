package com.koursh.inbar.notes.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.koursh.inbar.notes.NoteActivity;
import com.koursh.inbar.notes.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] titles;
    private long[] ids;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] titles, long[] ids) {
        update(titles, ids);
    }

    public void update(String[] titles, long[] ids) {
        this.titles = titles;
        this.ids = ids;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
//        ...
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(titles[position]);
        holder.bindListener(ids[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titles.length;
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

