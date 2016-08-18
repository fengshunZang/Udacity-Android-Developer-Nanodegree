package com.zangfengshun.popmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zangfengshun.popmovies.item.MovieItem;

import java.util.ArrayList;

/**
 * Created by Zang on 2016-08-17.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    //Define the version and name of the database;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PopMovieApp.db";

    //Define constants and SQL statements.
    private static final String TEXT_TYPE = " TEXT";
    //private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            MovieDbContractor.MovieEntry.TABLE_NAME + " (" +
            MovieDbContractor.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_TITLE + TEXT_TYPE + COMMA_SEP +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_AVERAGE_VOTE + TEXT_TYPE + COMMA_SEP +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_PLOT_SYNOPSIS + TEXT_TYPE + COMMA_SEP +
            MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_ID + TEXT_TYPE + " );";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + MovieDbContractor.MovieEntry.TABLE_NAME + ";";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    //This method is used to insert movie data into the database.
    public void addMovieEntry(MovieItem movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_IMAGE_PATH, movie.getMovieImagePath());
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_AVERAGE_VOTE, movie.getVoteAverage());
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_PLOT_SYNOPSIS, movie.getPlotSynopsis());
        contentValues.put(MovieDbContractor.MovieEntry.COLUMN_NAME_MOVIE_ID, movie.getID());

        //insert single entry into database.
        db.insert(MovieDbContractor.MovieEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    //This method is used to query all favorite movie items from database.
    public ArrayList<MovieItem> queryFavoriteMovieEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MovieItem> moviesList = new ArrayList<>();

        final String SQL_QUERY_ALL_ENTRIES = "SELECT * FROM " + MovieDbContractor.MovieEntry.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(SQL_QUERY_ALL_ENTRIES, null);

        if (cursor.moveToFirst()) {
            do {
                MovieItem movieItem = new MovieItem(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6));
                moviesList.add(movieItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return moviesList;
    }

    //This method is used to delete the whole table.
    public void deleteDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MovieDbContractor.MovieEntry.TABLE_NAME + ";");
        db.close();
    }
}
