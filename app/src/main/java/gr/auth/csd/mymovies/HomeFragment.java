package gr.auth.csd.mymovies;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a Fragment that is used as home and the user can see recommended movies
 */

public class HomeFragment extends Fragment implements MovieApiManager.OnMoviesLoadedListener{
    private MovieAdapter movieAdapterPopular;
    private MovieAdapter movieAdapterTopRated;
    private MovieAdapter movieAdapterUpcoming;
    private final List<Movie> popularMovies = new ArrayList<>();
    private final List<Movie> topRatedMovies = new ArrayList<>();
    private final List<Movie> upcomingMovies = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch popular movies
        RecyclerView recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapterPopular = new MovieAdapter(R.layout.item_movie_small);
        recyclerViewPopular.setAdapter(movieAdapterPopular);
        MovieApiManager.fetchPopularMovies(new MovieApiManager.OnMoviesLoadedListener() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                popularMovies.addAll(movies);
                movieAdapterPopular.setMovies(popularMovies, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        showMovieDetails(movie);
                    }
                });
            }
            @Override
            public void onError(String errorMessage) {
                Log.e("HomeFragment", errorMessage);
            }
        });

        // Fetch top rated movies
        RecyclerView recyclerViewTopRated = view.findViewById(R.id.recyclerViewTopRated);
        recyclerViewTopRated.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapterTopRated = new MovieAdapter(R.layout.item_movie_small);
        recyclerViewTopRated.setAdapter(movieAdapterTopRated);
        MovieApiManager.fetchTopRatedMovies(new MovieApiManager.OnMoviesLoadedListener() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                topRatedMovies.addAll(movies);
                movieAdapterTopRated.setMovies(topRatedMovies, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        showMovieDetails(movie);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("HomeFragment", errorMessage);
            }
        });

        // Fetch upcoming movies
        RecyclerView recyclerViewUpcoming = view.findViewById(R.id.recyclerViewUpcoming);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapterUpcoming = new MovieAdapter(R.layout.item_movie_small);
        recyclerViewUpcoming.setAdapter(movieAdapterUpcoming);
        MovieApiManager.fetchUpcomingMovies(new MovieApiManager.OnMoviesLoadedListener() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                upcomingMovies.addAll(movies);
                movieAdapterUpcoming.setMovies(upcomingMovies, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        showMovieDetails(movie);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("HomeFragment", errorMessage);
            }
        });

    }
    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        movieAdapterPopular.setMovies(movies,new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                showMovieDetails(movie);
            }
        });
    }
    @Override
    public void onError(String errorMessage) {
        // Handle error when fetching movies
        Log.e("MainActivity", errorMessage);
    }
    private void showMovieDetails(Movie movie) {
        // Start MovieDetailsActivity and pass the movie object
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

}