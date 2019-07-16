package com.yashc.digitaldiary.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mDatabaseInstance;

    private static AppDatabase mAppDatabase;

    public static synchronized DatabaseClient getDatabaseInstance(Context mCtx) {

        if (mDatabaseInstance == null) {
            mAppDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "notes").build();
            mDatabaseInstance = new DatabaseClient();
        }

        return mDatabaseInstance;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
