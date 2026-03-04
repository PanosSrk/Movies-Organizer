package gr.auth.csd.mymovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to add the movies in recycle view
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener listener;
    private final int itemLayout;

    public MovieAdapter(int itemLayout) {
        this.itemLayout=itemLayout;
        this.movies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies,OnItemClickListener listener) {
        this.movies = movies;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie,listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView posterImageView;
        private final TextView titleTextView;
        private final TextView releaseDateTextView;
        private final TextView averageVoteTextView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            averageVoteTextView = itemView.findViewById(R.id.averageVoteTextView);
        }

        void bind(final Movie movie, final OnItemClickListener listener) {
            titleTextView.setText(movie.getTitle());
            String release = "Release Date: " + movie.getReleaseDate();
            String voting = "Average Vote: " + movie.getAverageVote();
            releaseDateTextView.setText(release);
            averageVoteTextView.setText(voting);

            Glide.with(itemView.getContext())
                    .load(movie.getPosterUrl())
                    .into(posterImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
}
