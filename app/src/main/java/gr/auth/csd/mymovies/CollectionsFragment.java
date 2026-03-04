package gr.auth.csd.mymovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a Fragment that shows the collections and allows the user to add and delete collections
 */
public class CollectionsFragment extends Fragment implements MovieApiManager.OnMoviesLoadedListener, AddCollectionDialog.AddCollectionDialogListener {

    private Collections collections;
    private CollectionNamesAdapter collectionNamesAdapter;
    ArrayList<String> collectionNames;

    private MovieAdapter movieAdapter;
    public CollectionsFragment(){}// Required empty public constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set Add Collection
        ImageView addCollection = view.findViewById(R.id.add_collection);
        addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogToAddCollection();
            }
        });
        //set Delete Collection
        ImageView deleteCollection = view.findViewById(R.id.delete_collection);
        deleteCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogToDeleteCollection();
            }
        });

        //Create Collections - Button Menu
        RecyclerView recyclerViewCollectionNames = view.findViewById(R.id.collection_names_recycle_view);
        collections=MainActivity.getCollections();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        collectionNames = collections.getNames();
        collectionNamesAdapter = new CollectionNamesAdapter(collectionNames);
        recyclerViewCollectionNames.setLayoutManager(linearLayoutManager);
        recyclerViewCollectionNames.setAdapter(collectionNamesAdapter);

        //Movie List - Recycle View
        RecyclerView recyclerViewCollectionMovies = view.findViewById(R.id.recyclerViewShowCollectionMovies);
        recyclerViewCollectionMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieAdapter = new MovieAdapter(R.layout.item_movie_big);
        recyclerViewCollectionMovies.setAdapter(movieAdapter);


        onMoviesLoaded(new ArrayList<>(collections.getCollection("Favorites")));
    }

    public void openDialogToAddCollection(){
        AddCollectionDialog addCollectionDialog = new AddCollectionDialog();
        addCollectionDialog.setTargetFragment(CollectionsFragment.this,1);
        addCollectionDialog.show(getFragmentManager(), "Create Collection");
    }

    /**
     * Get the name from the AddCollection Dialog
     */
    @Override
    public void getNewCollectionName(String name) {
        if(name.isEmpty()){
            Toast.makeText(getActivity(),"Give a name",Toast.LENGTH_SHORT).show();
        }else if (name.contains(" ")) {
            Toast.makeText(getActivity(),"Please give a name without space",Toast.LENGTH_SHORT).show();
        }else{
            if(collections.addCollection(name)) {
                collectionNames.add(name);
                collectionNamesAdapter.notifyItemInserted(collectionNames.size()-1);
                Toast.makeText(getActivity(),"Collection " + name + " created",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),"Collection " + name + " already exists",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openDialogToDeleteCollection(){

        ArrayList<String> collectionsToDelete = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Collections");
        CharSequence[] csCollections = this.collectionNames.toArray(new CharSequence[this.collectionNames.size()]);

        builder.setMultiChoiceItems(csCollections, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    collectionsToDelete.add(collectionNames.get(which));
                } else {
                    collectionsToDelete.remove(collectionNames.get(which));
                }
            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (String collectionName : collectionsToDelete) {
                    if(collectionName.equals("Favorites") || collectionName.equals("Seen")){
                        Toast.makeText(getActivity(),collectionName + " cannot be deleted",Toast.LENGTH_SHORT).show();
                    }else {
                        collections.deleteCollection(collectionName);
                        int position=collectionNames.indexOf(collectionName);
                        collectionNames.remove(collectionName);
                        collectionNamesAdapter.notifyItemRemoved(position);
                        Toast.makeText(getActivity(),collectionName + " deleted",Toast.LENGTH_SHORT).show();
                    }
                }

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

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        movieAdapter.setMovies(movies,new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                showMovieDetails(movie);
            }
        });
    }

    private void showMovieDetails(Movie movie) {
        // Start MovieDetailsActivity and pass the movie object
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onError(String errorMessage) {
        Log.e("Collections Fragment", errorMessage);
    }


    /**
     * setup the Button Menu for collections
     */
    private class CollectionNamesAdapter extends RecyclerView.Adapter<CollectionNamesAdapter.MyHolder>{

        ArrayList<String> collectionsNames;
        public CollectionNamesAdapter(ArrayList<String> collectionsNames) {
            this.collectionsNames=collectionsNames;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.item_rv_collection_names,parent,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.collectionName.setText(collectionsNames.get(position));
            holder.collectionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMoviesLoaded(new ArrayList<>(collections.getCollection(holder.collectionName.getText().toString())));
                }
            });
        }

        @Override
        public int getItemCount() {
            return collectionsNames.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            Button collectionName;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                collectionName=itemView.findViewById(R.id.button_collection_name);
            }
        }
    }
}