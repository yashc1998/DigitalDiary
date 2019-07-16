package com.yashc.digitaldiary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.yashc.digitaldiary.modal.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
