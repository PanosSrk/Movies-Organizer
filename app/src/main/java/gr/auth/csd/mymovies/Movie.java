package gr.auth.csd.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * A class that represents the entity of a movie
 */

public class Movie implements Parcelable {
    private final int id;
    private final String title;
    private final String posterUrl;
    private final String releaseDate;
    private final double averageVote;
    private final String description;
    //For Favorites

    public Movie(int id, String title, String posterUrl, String releaseDate, double averageVote, String description) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
        this.averageVote = averageVote;
        this.description = description;

    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterUrl = in.readString();
        releaseDate = in.readString();
        averageVote = in.readDouble();
        description = in.readString();

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(releaseDate);
        dest.writeDouble(averageVote);
        dest.writeString(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }


}
