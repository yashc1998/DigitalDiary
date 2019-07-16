package com.yashc.digitaldiary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amitshekhar.DebugDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yashc.digitaldiary.adapter.NotesAdapter;
import com.yashc.digitaldiary.db.DatabaseClient;
import com.yashc.digitaldiary.modal.Note;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mNotesRecyclerView;
    private NotesAdapter mNotesAdapter = null;
    private FloatingActionButton mAddNoteBtn;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("SomethingSomething", DebugDB.getAddressLog());

        Toolbar mMainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mMainToolbar);

        mAddNoteBtn = findViewById(R.id.btn_add_note);
        mAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Title");
                alertDialog.setMessage("Enter the title");

                final EditText titleEditText = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                titleEditText.setLayoutParams(lp);
                alertDialog.setView(titleEditText);

                alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        title = titleEditText.getText().toString();
                        if (title.equalsIgnoreCase("")) {
                            Toast.makeText(MainActivity.this, "Please add a title", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        } else {
                            Intent intent = new Intent(MainActivity.this, NotesDetail.class);
                            Note note = new Note(UUID.randomUUID().toString(), title, "", "");
                            intent.putExtra("EXTRA_NOTE", note);
                            intent.putExtra("EXTRA_IS_NEW", true);
                            startActivity(intent);
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
        });

        if (mMainToolbar != null) {
            mMainToolbar.setTitle("Digital Diary");
            mMainToolbar.setTitleTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getSupportActionBar()).setElevation(5.0f);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMainToolbar.setElevation(5.0f);
            }
        }

        //Initialising the recycler view
        mNotesRecyclerView = findViewById(R.id.notes_rv);

        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mNotesRecyclerView.setHasFixedSize(true);
    }

    private void getAllNotes() {

        @SuppressLint("StaticFieldLeak")
        class GetNotes extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return DatabaseClient.getDatabaseInstance(MainActivity.this)
                        .getAppDatabase()
                        .noteDao()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                mNotesAdapter = new NotesAdapter(notes, MainActivity.this);
                mNotesRecyclerView.setAdapter(mNotesAdapter);
            }
        }

        GetNotes getNotes = new GetNotes();
        getNotes.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllNotes();
    }
}
