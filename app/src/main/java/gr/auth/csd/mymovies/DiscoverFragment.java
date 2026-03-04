package gr.auth.csd.mymovies;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This class is a Fragment that allows the user to discover new movies with search, categories and sort
 */
public class DiscoverFragment extends Fragment implements MovieApiManager.OnMoviesLoadedListener{

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private List<Movie> movies;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Search
        SearchView searchView = view.findViewById(R.id.searchViewDiscover);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchTerm) {
                searchTerm = searchTerm.trim();
                if (!searchTerm.isEmpty()) {
                    MovieApiManager.searchMovies(searchTerm, DiscoverFragment.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //Button Sort
        Button buttonSort = view.findViewById(R.id.button_sort);
        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortMethods();
            }
        });

        //Button Categories
        Button buttonCategories = view.findViewById(R.id.button_categories);
        buttonCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategories();
            }
        });

        //Movie List - Recycle View
        recyclerView = view.findViewById(R.id.recyclerViewDiscover);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieAdapter = new MovieAdapter(R.layout.item_movie_big);
        recyclerView.setAdapter(movieAdapter);
        MovieApiManager.fetchPopularMovies(DiscoverFragment.this);

    }

    private void showCategories() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layour_categories);


        TextView categoryAction = dialog.findViewById(R.id.category_action);
        categoryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 28; //ID for Action
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });


        TextView categoryComedy = dialog.findViewById(R.id.category_comedy);
        categoryComedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 35; //ID for comedy
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        TextView categoryThriller = dialog.findViewById(R.id.category_thriller);
        categoryThriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 53; //ID for thriller
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        TextView categoryMystery = dialog.findViewById(R.id.category_mystery);
        categoryMystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 9648; //ID for mystery
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        TextView categoryDrama = dialog.findViewById(R.id.category_drama);
        categoryDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 18; //ID for drama
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        TextView categoryFantasy = dialog.findViewById(R.id.category_fantasy);
        categoryFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 14; //ID for fantasy
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        TextView categoryRomance = dialog.findViewById(R.id.category_romance);
        categoryRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int actionGenreId = 10749; //ID for romance
                MovieApiManager.fetchMoviesByGenre(actionGenreId, DiscoverFragment.this);
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    private void showSortMethods() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layour_sort);

        TextView sortByName = dialog.findViewById(R.id.sort_method_name);
        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortMoviesBy("Name");
            }
        });

        TextView sortByNewest = dialog.findViewById(R.id.sort_method_newest);
        sortByNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortMoviesBy("Newest");
            }
        });

        TextView sortByOldest = dialog.findViewById(R.id.sort_method_oldest);
        sortByOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortMoviesBy("Oldest");
            }
        });

        TextView sortByScore = dialog.findViewById(R.id.sort_method_score);
        sortByScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sortMoviesBy("Score");
            }
        });


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        this.movies=movies;
        movieAdapter.setMovies(movies,new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                showMovieDetails(movie);
            }
        });
        // Scroll to the top of the RecyclerView
        recyclerView.scrollToPosition(0);
    }

    public void sortMoviesBy(String sortType){
        Collections.sort(movies, new Comparator<Movie>() {

            @Override
            public int compare(Movie movie1, Movie movie2) {

                if(sortType.equals("Name")){
                    return movie1.getTitle().compareTo(movie2.getTitle());
                }
                if(sortType.equals("Score")){
                    return Double.compare(movie1.getAverageVote(),movie2.getAverageVote())*(-1);
                }
                Date date1;
                Date date2;
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy").parse(movie1.getReleaseDate());
                    date2 = new SimpleDateFormat("dd-MM-yyyy").parse(movie2.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -1;
                }
                if(sortType.equals("Newest")){

                    return date1.compareTo(date2)*(-1);
                }
                if(sortType.equals("Oldest")){
                    return (date1.compareTo(date2));
                }

                return 0;
            }
        });
        onMoviesLoaded(movies);
    }



    private void showMovieDetails(Movie movie) {
        // Start MovieDetailsActivity and pass the movie object
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle error when fetching movies
        Log.e("Discover Fragment", errorMessage);
    }

}