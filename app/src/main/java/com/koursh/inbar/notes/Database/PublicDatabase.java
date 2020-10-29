package com.koursh.inbar.notes.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

public class PublicDatabase {
    private static PublicDatabase singleton;
    private final AppDatabase db;

    private PublicDatabase(Context context) {

        db = Room.databaseBuilder(context,
                AppDatabase.class, "notes").allowMainThreadQueries().addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                db.execSQL("insert into Note(title, text) values(\"demo\", \"demo\")");
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        }).fallbackToDestructiveMigration().build();

    }

    public static PublicDatabase getPublicDatabaseInstance(Context context) {
        if (singleton == null) {
            singleton = new PublicDatabase(context);
        }
        return singleton;
    }

    public List<Note> getAllNotes() {
        return db.noteDao().getAll();
    }

    public Note getNote(long id) {
        return db.noteDao().getNote(id);
    }

    public void update(Note note) {
        db.noteDao().update(note);
    }

    public long insert(Note note) {
        return db.noteDao().insert(note);
    }
}
