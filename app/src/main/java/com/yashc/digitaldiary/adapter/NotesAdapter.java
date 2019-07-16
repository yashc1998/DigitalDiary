package com.yashc.digitaldiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yashc.digitaldiary.NotesDetail;
import com.yashc.digitaldiary.R;
import com.yashc.digitaldiary.modal.Note;

import java.util.Date;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private List<Note> mNotesList;
    private Context mCtx;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public NotesAdapter(List<Note> mNotesList, Context mCtx) {
        this.mNotesList = mNotesList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        Note note = mNotesList.get(position);
        holder.notesTitle.setText(note.getTitle());

        SpannableString string = new SpannableString(getTimeAgo(Long.parseLong(note.getLastModified())));
        string.setSpan(new StyleSpan(Typeface.ITALIC), 0, string.length(), 0);


        holder.lastModified.setText("Last Modified: " + string);
    }

    private static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    class NotesHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView notesTitle;
        TextView lastModified;


        NotesHolder(View itemView) {
            super(itemView);

            notesTitle = itemView.findViewById(R.id.item_note_title);
            lastModified = itemView.findViewById(R.id.item_note_last_modified);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note note = mNotesList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, NotesDetail.class);
            intent.putExtra("EXTRA_NOTE", note);
            intent.putExtra("EXTRA_IS_NEW", false);
            mCtx.startActivity(intent);
        }


        @Override
        public boolean onLongClick(View view) {
            Note note = mNotesList.get(getAdapterPosition());
            Toast.makeText(mCtx, note.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
