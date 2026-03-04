package gr.auth.csd.mymovies;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Class that creates a new Activity with movie details
 */
public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    private Movie movie;

    private Collections collections;

    private ArrayList<String> collectionNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeHelper.applyTheme(this);  // Apply the theme
        setContentView(R.layout.activity_movie_details);


        //for toolbar back button
        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setOnClickListener(v -> onBackPressed());

        //Set image and texts
        ImageView posterImageView = findViewById(R.id.posterImageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView releaseDateTextView = findViewById(R.id.releaseDateTextView);
        TextView averageVoteTextView = findViewById(R.id.averageVoteTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (movie != null ) {

            titleTextView.setText(movie.getTitle());
            releaseDateTextView.setText(movie.getReleaseDate());
            averageVoteTextView.setText(String.valueOf(movie.getAverageVote()));
            descriptionTextView.setText(movie.getDescription());

            Glide.with(this)
                    .load(movie.getPosterUrl())
                    .into(posterImageView);
        }
        collections = MainActivity.getCollections();
        collectionNames = collections.getNames();

        //Set collections button
        ImageView iconAddMovieToCollections = findViewById(R.id.add_movie_to_collections);
        iconAddMovieToCollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createCollectionsDialog();
            }
        });

    }

    private void createCollectionsDialog() {
        ArrayList<String> isInCollections = collections.isInCollections(movie);
        boolean[] checkedCollections = new boolean[collectionNames.size()];
        int counter = 0;
        for (String collection : collectionNames) {
            checkedCollections[counter++]= isInCollections.contains(collection);
        }
        //ArrayList<String> collectionsMovie = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Collections");
        CharSequence[] csCollections = this.collectionNames.toArray(new CharSequence[this.collectionNames.size()]);

        builder.setMultiChoiceItems(csCollections, checkedCollections, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                checkedCollections[which]= isChecked;
            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<String> collectionsMovie = new ArrayList<>();
                int counter=0;
                for (boolean checkedCollection : checkedCollections) {
                    if (checkedCollection){
                        collectionsMovie.add(collectionNames.get(counter));

                    }
                    counter++;
                }
                collections.addMovieToCollections(movie,collectionsMovie);
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.create();
        builder.show();
    }


}
