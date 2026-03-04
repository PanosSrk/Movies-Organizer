package gr.auth.csd.mymovies;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the database operations using an MyDBHandler object
 */
public class Collections {

    private final MyDBHandler dbHandler;
    public Collections(Context context) {

        dbHandler = new MyDBHandler(context);
        deleteCollection("android_metadata");

    }


    public List<Movie> getCollection(String name){
        return dbHandler.getCollection(name);
    }

    public boolean addCollection(String name){
        if(getNames().contains(name)) return false;
        dbHandler.createCollection(name);
        return true;
    }

    public boolean deleteCollection(String name){
        if(getNames().contains(name)){
            dbHandler.deleteCollection(name);
            return true;
        }
        return false;
    }

    public ArrayList<String> getNames(){
        return dbHandler.getCollectionNames();
    }

    /**
     * Returns an Arraylist with the names of the collections that have the movie
     */
    public ArrayList<String> isInCollections(Movie movie){
        ArrayList<String> collectionsWithMovie = new ArrayList<>();
        for (String collection : getNames()) {
            if(collection.equals("android_metadata")){
                continue;
            }
            if(getCollection(collection).contains(movie)){
                collectionsWithMovie.add(collection);
            }
        }
        return collectionsWithMovie;
    }

    public void addMovieToCollections(Movie movie, ArrayList<String> collectionsToAdd){
        for (String collection : getNames()) {
            dbHandler.deleteMovie(collection,movie);
        }
        System.out.println("ADD TO " + collectionsToAdd);
        for (String collection : collectionsToAdd) {

            dbHandler.addMovie(collection,movie);
        }
    }
}
