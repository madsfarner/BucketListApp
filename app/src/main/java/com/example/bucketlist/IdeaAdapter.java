package com.example.bucketlist;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.ViewHolder> {

    private List<Idea> ideaList;
    private CheckButtonListener checkButtonListener;

    public IdeaAdapter(List<Idea> ideaList, CheckButtonListener cbl){
        this.checkButtonListener = cbl;
        this.ideaList = ideaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view  = inflater.inflate(R.layout.list_item, viewGroup, false);
        IdeaAdapter.ViewHolder viewHolder = new IdeaAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Idea idea = ideaList.get(i);
        viewHolder.textView_desc.setText(idea.getDescription());
        viewHolder.textView_name.setText(idea.getTitle());
        viewHolder.checkBox.setChecked(idea.isChecked());
        setTextDeco(idea.isChecked(), viewHolder);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 ideaList.get(viewHolder.getAdapterPosition()).setChecked(isChecked);
                 setTextDeco(isChecked, viewHolder);
                 checkButtonListener.onCheckClick(ideaList.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    public interface CheckButtonListener {
        void onCheckClick(Idea idea);
    }

    @Override
    public int getItemCount() {
        return ideaList.size();
    }

    private void setTextDeco(boolean isChecked, ViewHolder holder) {
        if(isChecked) {
            holder.textView_desc.setPaintFlags(holder.textView_desc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_name.setPaintFlags(holder.textView_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textView_desc.setPaintFlags(holder.textView_desc.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_name.setPaintFlags(holder.textView_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_desc;
        TextView textView_name;
        CheckBox checkBox;

        public ViewHolder(View view){
            super(view);
            textView_desc = view.findViewById(R.id.textView_desc);
            textView_name = view.findViewById(R.id.textView_title);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}
