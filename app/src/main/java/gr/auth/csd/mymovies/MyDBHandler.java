package gr.auth.csd.mymovies;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the sal database queries
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Collections.db";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MOVIE_TITLE = "movietitle";
    private static final String COLUMN_POSTER_URL = "posterurl";
    private static final String COLUMN_RELEASE_DATE = "releasedate";
    private static final String COLUMN_AVERAGE_VOTE = "averagevote";
    private static final String COLUMN_DESCRIPTION = "description";

    //Constructor
    public MyDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                "Favorites" + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_TITLE + " TEXT," +
                COLUMN_POSTER_URL + " TEXT," +
                COLUMN_RELEASE_DATE + " TEXT," +
                COLUMN_AVERAGE_VOTE + " DOUBLE," +
                COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);

        String CREATE_SEEN_TABLE = "CREATE TABLE " +
                "Seen" + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_TITLE + " TEXT," +
                COLUMN_POSTER_URL + " TEXT," +
                COLUMN_RELEASE_DATE + " TEXT," +
                COLUMN_AVERAGE_VOTE + " DOUBLE," +
                COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_SEEN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }
        c.close();

        // call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }

        onCreate(db);
    }

    //Create a new table
    public void createCollection(String tableName){
        final SQLiteDatabase db = getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE " +
                tableName + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_TITLE + " TEXT," +
                COLUMN_POSTER_URL + " TEXT," +
                COLUMN_RELEASE_DATE + " TEXT," +
                COLUMN_AVERAGE_VOTE + " DOUBLE," +
                COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        db.close();
    }

    public void deleteCollection(String tableName){
        final SQLiteDatabase db = getWritableDatabase();
        String dropQuery = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(dropQuery);
        db.close();
    }

    public ArrayList<Movie> getCollection(String tableName){
        final SQLiteDatabase db = getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM " + tableName;
        if(db!=null){
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.getCount() == 0){
                db.close();
                return movies;
            }
            while (cursor.moveToNext()){
                Movie movie = new Movie(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4),cursor.getString(5));
                movies.add(movie);
            }

            cursor.close();
            db.close();
            return movies;

        }else{

            return null;
        }



    }

    public ArrayList<String> getCollectionNames(){
        final SQLiteDatabase db = getReadableDatabase();

        // query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }
        c.close();
        db.close();
        tables.remove("android_metadata");
        return tables;
    }



    public void addMovie(String collectionName, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_POSTER_URL, movie.getPosterUrl());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_AVERAGE_VOTE, movie.getAverageVote());
        values.put(COLUMN_DESCRIPTION, movie.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(collectionName, null, values);
        db.close();
    }

    public void deleteMovie(String collectionName, Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(collectionName, "_id=?", new String[]{Integer.toString(movie.getId())});
        System.out.println(result + "  " + movie.getId());
        db.close();
    }

}