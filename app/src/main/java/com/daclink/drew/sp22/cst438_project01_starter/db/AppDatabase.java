/**
 * Last Modified: 02/08/2022
 * Abstract:
 */

package com.daclink.drew.sp22.cst438_project01_starter.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.ArrayList;


@Database(entities = {UserEntity.class}, version = 1, exportSchema = false)
@TypeConverters(ArrayListConverter.class)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "AppDatabase.db";
    public static final String USER_TABLE = "userTable";
    public static final String MOVIE_TABLE = "movieTable";

    private static AppDatabase instance;

    public abstract UserDao userDao();
    // public abstract MovieDao movieDao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
