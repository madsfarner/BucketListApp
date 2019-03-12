package com.example.bucketlist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Idea.class}, version = 1, exportSchema = false)
public abstract class IdeaRoomDatabase extends RoomDatabase {

    private final static String NAME_DATABASE = "idea_database";
    public abstract IdeaDao ideaDao();

    private static volatile IdeaRoomDatabase INSTANCE;

    static IdeaRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (IdeaRoomDatabase.class) {
                if ( INSTANCE == null ){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IdeaRoomDatabase.class, NAME_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
