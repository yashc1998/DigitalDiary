package com.yashc.digitaldiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yashc.digitaldiary.db.DatabaseClient;
import com.yashc.digitaldiary.db.NoteDao;
import com.yashc.digitaldiary.modal.Note;

import java.util.Date;
import java.util.UUID;

public class NotesDetail extends AppCompatActivity {

    private Note mNote = null;
    private EditText mContentEditText;
    String title;
    boolean isNew;
    private NoteDao noteDao;
    private String noteId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        mContentEditText = findViewById(R.id.editable_content);

        Intent intent = getIntent();
        if (intent != null) {
            mNote = (Note) intent.getSerializableExtra("EXTRA_NOTE");
            isNew = intent.getBooleanExtra("EXTRA_IS_NEW", false);

            if (!isNew) {
                noteId = mNote.getId();
            }
        }
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            title = mNote.getTitle();
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (mNote != null) {
            if (mNote.getContent().equalsIgnoreCase("")) {
                mContentEditText.setHint("Type here...");
            } else {
                mContentEditText.setText(mNote.getContent());
                mContentEditText.setSelection(mContentEditText.getText().toString().length());
            }
        } else {
            mContentEditText.setHint("Type here...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        noteDao = DatabaseClient.getDatabaseInstance(NotesDetail.this)
                .getAppDatabase()
                .noteDao();

        int id = item.getItemId();

        switch (id) {

            case R.id.action_detail_edit_title: {
                showEditTitleDialog();
                break;
            }

            case R.id.action_detail_delete: {
                deleteNote(mNote);
                break;
            }

            case R.id.action_detail_done: {
                addNote();
                break;
            }

            case android.R.id.home: {
                addNote();
                onBackPressed();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditTitleDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NotesDetail.this);
        alertDialog.setTitle("Title");
        alertDialog.setMessage("Enter the title");

        final EditText titleEditText = new EditText(NotesDetail.this);
        titleEditText.setText(title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleEditText.setLayoutParams(lp);
        alertDialog.setView(titleEditText);
        titleEditText.setSelection(titleEditText.getText().toString().length());

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (titleEditText.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(NotesDetail.this, "Please add a title", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else {
                    title = titleEditText.getText().toString();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(title);
                    }
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }

    private void deleteNote(final Note mNote) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteNote(mNote);
                Log.e("NotesDetail Something", "Inside delete with content: " + mNote.getContent() + " : " + mNote.getId());
                NotesDetail.this.finish();
            }
        });
    }

    private void addNote() {
        if (!isNew) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mNote = new Note(noteId,
                            title,
                            mContentEditText.getText().toString(),
                            String.valueOf(new Date().getTime())
                    );
                    noteDao.updateNote(
                            mNote
                    );
                    Log.e("NotesDetail Something", "Inside if with content: " + mNote.getContent() + " : " + noteId);
                    NotesDetail.this.finish();
                }
            });
        } else {
            UUID uuid = UUID.randomUUID();
            final String randomUUID = uuid.toString().replaceAll("-", "");
            mNote = new Note(randomUUID, title, mContentEditText.getText().toString(), String.valueOf(new Date().getTime()));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Log.e("NotesDetail Something", "Inside else with uuid: " + randomUUID);
                    Log.e("NotesDetail Something", "Inside else with content: " + mNote.getContent() + " : " + mNote.getId());
                    noteDao.addNote(mNote);
                    NotesDetail.this.finish();
                }
            });
        }
    }
}
