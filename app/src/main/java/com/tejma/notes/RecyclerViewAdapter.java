package com.tejma.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    String TAG = "ADAPTER";
    ArrayList<Notes> objects, query_list;
    Context context;
    int res;
    private final onNoteListener mOnNoteListener;
    LayoutInflater inflater;

    public RecyclerViewAdapter(@NonNull Context context, int resource, ArrayList<Notes> objects,  onNoteListener mOnNoteListener) {
        this.context = context;
        this.objects = new ArrayList<>();
        this.objects = objects;
        res = resource;
        inflater = LayoutInflater.from(context);
        this.mOnNoteListener = mOnNoteListener;
        query_list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        query_list.clear();
        query_list.addAll(objects);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.note.setText(objects.get(position).getName());
        holder.date.setText(objects.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void filter(String query){
        query = query.toLowerCase().trim();
        objects.clear();
        if(query.length() == 0)
        {
            objects.addAll(query_list);
        }
        else
        {
            for(Notes chapter : query_list){
                if(chapter.getName().toLowerCase().contains(query)){
                    objects.add(chapter);
                }
            }
        }
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView note, date;
        LinearLayout parent;
        onNoteListener noteListener;

         public ViewHolder(View itemView, onNoteListener onNoteListener){
             super(itemView);
             this.note = itemView.findViewById(R.id.list_text);
             this.date = itemView.findViewById(R.id.date);
             this.noteListener = onNoteListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
             this.parent = (LinearLayout) itemView.findViewById(R.id.parent_layout);
         }

        @Override
        public void onClick(View v) {
            noteListener.onNoteClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            noteListener.onNoteLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onNoteListener{
        void onNoteClick(int position, View v);
        void onNoteLongClick(int position);
    }
}
