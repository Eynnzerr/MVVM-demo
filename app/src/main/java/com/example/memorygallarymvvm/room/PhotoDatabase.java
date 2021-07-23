package com.example.memorygallarymvvm.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.memorygallarymvvm.bean.PhotoItem;

@Database(entities = {PhotoItem.class}, version = 3, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "memory_database";
    private volatile static PhotoDatabase INSTANCE;
    public static PhotoDatabase getDataBase(Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (PhotoDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),PhotoDatabase.class,DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract PhotoDao getPhotoDao();

    static final Migration migration_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE PhotoItem ADD COLUMN createdDate LONG");
            database.execSQL("ALTER TABLE PhotoItem ADD COLUMN modifiedDate LONG");
        }
    };
}
