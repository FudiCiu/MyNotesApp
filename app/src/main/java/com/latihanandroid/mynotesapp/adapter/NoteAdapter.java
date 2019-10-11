package com.latihanandroid.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.latihanandroid.mynotesapp.CustomOnItemClickListener;
import com.latihanandroid.mynotesapp.NoteAddUpdateActivity;
import com.latihanandroid.mynotesapp.R;
import com.latihanandroid.mynotesapp.db.DatabaseContract;
import com.latihanandroid.mynotesapp.entity.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> listNote=new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNote() {
        return listNote;
    }

    public void setListNote(ArrayList<Note> listNote) {
        if (this.listNote.size()>0){
            this.listNote.clear();
        }
        this.listNote.addAll(listNote);
        notifyDataSetChanged();
    }
    public void addItem(Note note){
        this.listNote.add(note);
        notifyItemInserted(this.listNote.size()-1);
    }

    public void updateItem(int position,Note note){
        this.listNote.set(position,note);
        notifyItemChanged(position,note);
    }

    public void removeItem(int position){
        this.listNote.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,this.listNote.size());
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note,viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        noteViewHolder.tvTitle.setText(listNote.get(position).getTitle());
        noteViewHolder.tvDescription.setText(listNote.get(position).getDescription());
        noteViewHolder.tvDate.setText(listNote.get(position).getDate());
        noteViewHolder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent= new Intent(activity, NoteAddUpdateActivity.class);
                Uri uri=Uri.parse(DatabaseContract.NoteColumns.CONTENT_URI+"/"+getListNote().get(position).getId());
                intent.setData(uri);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION,position);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE,listNote.get(position));
                activity.startActivityForResult(intent,NoteAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvTitle,tvDescription;
        CardView cvNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tv_item_date);
            tvTitle=itemView.findViewById(R.id.tv_item_title);
            tvDescription=itemView.findViewById(R.id.tv_item_description);
            cvNote=itemView.findViewById(R.id.cv_item_note);

        }
    }
}
