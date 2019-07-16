package com.yashc.digitaldiary.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yashc.digitaldiary.modal.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Update
    void updateNote(Note note);

    @Insert
    void addNote(Note... notes);

    @Delete
    void deleteNote(Note note);
}
