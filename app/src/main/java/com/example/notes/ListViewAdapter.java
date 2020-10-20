package com.example.notes;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Notes> objects;
    int res;
    ArrayList<Notes> notesList;
    LayoutInflater inflater;

    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<Notes> objects) {
        this.context = context;
        this.objects = objects;
        res = resource;
        inflater = LayoutInflater.from(context);
        this.notesList = new ArrayList<>();
    }

    public static class ViewHolder{
        TextView list_text, date;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Nullable
    @Override
    public Notes getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(res, null);

            holder.list_text = convertView.findViewById(R.id.list_text);
            holder.date = convertView.findViewById(R.id.date);

            notesList.clear();
            notesList.addAll(objects);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.list_text.setText(objects.get(position).getName());
        holder.date.setText(Html.fromHtml(objects.get(position).getDescription()));

        return convertView;
    }

    public void filter(String query){
        query = query.toLowerCase().trim();
        objects.clear();
        if(query.length() == 0)
        {
            objects.addAll(notesList);
        }
        else
        {
            for(Notes chapter : notesList){
                if(chapter.getName().toLowerCase().contains(query) || chapter.getDescription().toLowerCase().contains(query)){
                    objects.add(chapter);
                }
            }
        }
        notifyDataSetChanged();
    }
}
