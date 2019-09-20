package com.hariz.noah.Network.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hariz.noah.Model.MovieModel;
import com.hariz.noah.Model.TvModel;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_DATE;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_JENIS;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_MOVIEID;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_PLOT_SYNOPSIS;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_POSTER_PATH;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_TITLE;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.COLUMN_USERRATING;
import static com.hariz.noah.Network.Database.DatabaseContract.FavColumns.TABLE_NAME;

public class FavHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private Context context;
    private static FavHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavHelper(Context context) {
        this.context = context;
    }

    public FavHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public static FavHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    public Long addFavorite(MovieModel movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_MOVIEID, movie.getId());
        initialValues.put(COLUMN_TITLE, movie.getTitle());
        initialValues.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        initialValues.put(COLUMN_USERRATING, movie.getVoteAverage());
        initialValues.put(COLUMN_PLOT_SYNOPSIS, movie.getOverview());
        initialValues.put(COLUMN_DATE, movie.getReleaseDate());
        initialValues.put(COLUMN_JENIS, "Movie");

        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public Long addFavoriteTV(TvModel movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_MOVIEID, movie.getId());
        initialValues.put(COLUMN_TITLE, movie.getOriginalName());
        initialValues.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        initialValues.put(COLUMN_USERRATING, movie.getVoteAverage());
        initialValues.put(COLUMN_PLOT_SYNOPSIS, movie.getOverview());
        initialValues.put(COLUMN_DATE, movie.getFirstAirDate());
        initialValues.put(COLUMN_JENIS, "TV");

        return database.insert(DATABASE_TABLE, null, initialValues);
    }
    public int deleteFav(int id) {
        return database.delete(DATABASE_TABLE, COLUMN_MOVIEID + " = " + id, null);
    }


    public List<MovieModel> query() {
        String[] columns = {
                _ID,
                COLUMN_MOVIEID,
                COLUMN_TITLE,
                COLUMN_USERRATING,
                COLUMN_POSTER_PATH,
                COLUMN_PLOT_SYNOPSIS

        };
        String sortOrder =
                _ID + " ASC";
        List<MovieModel> favoriteList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                MovieModel movie = new MovieModel();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return favoriteList;
    }

    public List<TvModel> query2() {
        String[] columns = {
                _ID,
                COLUMN_MOVIEID,
                COLUMN_TITLE,
                COLUMN_USERRATING,
                COLUMN_POSTER_PATH,
                COLUMN_PLOT_SYNOPSIS,
                COLUMN_JENIS

        };
        String sortOrder =
                DatabaseContract.FavColumns._ID + " ASC";
        List<TvModel> favoriteList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                TvModel movie = new TvModel();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))));
                movie.setOriginalName(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return favoriteList;
    }

}
