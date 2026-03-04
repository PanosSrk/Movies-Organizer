package gr.auth.csd.mymovies;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that fetch the data from the api of movieDB
 */
public class MovieApiManager {
    private static final String API_KEY = "b9e44d1ed295fbcdf73ce8f21e8b0803";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static void fetchPopularMovies(OnMoviesLoadedListener listener) {
        String popularMoviesUrl = BASE_URL + "movie/popular?api_key=" + API_KEY;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute(popularMoviesUrl);
    }

    public static void fetchTopRatedMovies(OnMoviesLoadedListener listener) {
        String topRatedMoviesUrl = BASE_URL + "movie/top_rated?api_key=" + API_KEY;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute(topRatedMoviesUrl);
    }

    public static void fetchUpcomingMovies(OnMoviesLoadedListener listener) {
        String upcomingMoviesUrl = BASE_URL + "movie/upcoming?api_key=" + API_KEY;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute(upcomingMoviesUrl);
    }

    public static void fetchMoviesByGenre(int genreId, OnMoviesLoadedListener listener) {
        String genreMoviesUrl = BASE_URL + "discover/movie?api_key=" + API_KEY + "&with_genres=" + genreId;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute(genreMoviesUrl);
    }


    public static void searchMovies(String query, OnMoviesLoadedListener listener) {
        String searchUrl = BASE_URL + "search/movie?api_key=" + API_KEY + "&query=" + query;
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute(searchUrl);
    }

    private static class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final OnMoviesLoadedListener listener;


        public FetchMoviesTask(OnMoviesLoadedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            String urlString = urls[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<Movie> movies;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                String movieJsonString = buffer.toString();
                movies = extractMoviesFromJson(movieJsonString);

            } catch (IOException | JSONException e) {
                Log.e("MovieApiManager", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("MovieApiManager", "Error closing stream", e);
                    }
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                listener.onMoviesLoaded(movies);
            } else {
                listener.onError("Failed to fetch movie data");
            }
        }

        private List<Movie> extractMoviesFromJson(String movieJsonString) throws JSONException {
            final String MOVIE_RESULTS = "results";


            JSONObject movieJson = new JSONObject(movieJsonString);
            JSONArray movieResults = movieJson.getJSONArray(MOVIE_RESULTS);

            List<Movie> movies = new ArrayList<>();

            for (int i = 0; i < movieResults.length(); i++) {
                JSONObject movieObject = movieResults.getJSONObject(i);
                int id = movieObject.getInt("id");
                String title = movieObject.getString("title");
                String posterPath = movieObject.getString("poster_path");
                String releaseDate = movieObject.getString("release_date");
                double averageVote = movieObject.getDouble("vote_average");
                BigDecimal bd = new BigDecimal(averageVote).setScale(2, RoundingMode.HALF_EVEN);
                averageVote = bd.doubleValue();
                String description = movieObject.getString("overview");

                // Build the full poster URL
                String posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

                try {
                    releaseDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Movie movie = new Movie(id,title, posterUrl, releaseDate, averageVote, description);
                movies.add(movie);
            }

            return movies;
        }
    }

    public interface OnMoviesLoadedListener {
        void onMoviesLoaded(List<Movie> movies);
        void onError(String errorMessage);
    }
}
