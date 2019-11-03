 package com.dtag.notesapp.persistence;

 import android.content.Context;
 import android.os.AsyncTask;

 import androidx.annotation.NonNull;
 import androidx.room.Database;
 import androidx.room.Room;
 import androidx.room.RoomDatabase;
 import androidx.sqlite.db.SupportSQLiteDatabase;

 @Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public static NoteDataBase instance;

    public static synchronized NoteDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class, "note_db")
                    .fallbackToDestructiveMigration()
                      .addCallback(callback)
                    .build();
        }
        return instance;
    }

     //  this call back is used to populate static content inside DB.
      static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
  
          @Override
          public void onCreate(@NonNull SupportSQLiteDatabase db) {
              super.onCreate(db);
              {
                  new AsyncTask<Void, Void, Void>() {
                      @Override
                      protected Void doInBackground(Void... voids) {
                          instance.noteDao().insert(new Note("Title 1", "Desc 1", 1));
                          instance.noteDao().insert(new Note("Title 2", "Desc 2", 2));
                          instance.noteDao().insert(new Note("Title 3", "Desc 3", 3));
                          instance.noteDao().insert(new Note("Title 4", "Desc 4", 4));
                          instance.noteDao().insert(new Note("Title 5", "Desc 5", 5));
                          instance.noteDao().insert(new Note("Title 6", "Desc 6", 6));
                          instance.noteDao().insert(new Note("Title 7", "Desc 7", 7));
                          instance.noteDao().insert(new Note("Title 8", "Desc 8", 8));
                          instance.noteDao().insert(new Note("Title 2", "Desc 2", 2));
                          instance.noteDao().insert(new Note("Title 3", "Desc 3", 3));
                          instance.noteDao().insert(new Note("Title 4", "Desc 4", 4));
                          instance.noteDao().insert(new Note("Title 5", "Desc 5", 5));
                          instance.noteDao().insert(new Note("Title 6", "Desc 6", 6));
                          instance.noteDao().insert(new Note("Title 7", "Desc 7", 7));
                          instance.noteDao().insert(new Note("Title 8", "Desc 8", 8));
                          instance.noteDao().insert(new Note("Title 1", "Desc 1", 1));
                          instance.noteDao().insert(new Note("Title 2", "Desc 2", 2));
                          instance.noteDao().insert(new Note("Title 3", "Desc 3", 3));
                          instance.noteDao().insert(new Note("Title 4", "Desc 4", 4));
                          instance.noteDao().insert(new Note("Title 5", "Desc 5", 5));
                          instance.noteDao().insert(new Note("Title 6", "Desc 6", 6));
                          instance.noteDao().insert(new Note("Title 7", "Desc 7", 7));
                          instance.noteDao().insert(new Note("Title 8", "Desc 8", 8));
                          instance.noteDao().insert(new Note("Title 2", "Desc 2", 2));
                          instance.noteDao().insert(new Note("Title 3", "Desc 3", 3));
                          instance.noteDao().insert(new Note("Title 4", "Desc 4", 4));
                          instance.noteDao().insert(new Note("Title 5", "Desc 5", 5));
                          instance.noteDao().insert(new Note("Title 6", "Desc 6", 6));
                          instance.noteDao().insert(new Note("Title 7", "Desc 7", 7));
                          instance.noteDao().insert(new Note("Title 8", "Desc 8", 8));

                          return null;
                      }
                  }.execute();
              }
  
          }
      };


}