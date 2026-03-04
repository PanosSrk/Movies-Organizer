package gr.auth.csd.mymovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Class for a Dialog that creates a new Collection
 */
public class AddCollectionDialog extends AppCompatDialogFragment {
    private EditText collectionName;
    private AddCollectionDialogListener addCollectionDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_new_collection,null);
        collectionName = view.findViewById(R.id.editTextNewCollectionName);

        builder.setView(view).setTitle("Create Collection")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //it closes the dialog
            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String name = collectionName.getText().toString();
                addCollectionDialogListener.getNewCollectionName(name);
            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addCollectionDialogListener = (AddCollectionDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement AddCollectionDialogListener");
        }
    }
    /**
     * Interface of a method to get the user input at the class of use
     */
    public interface AddCollectionDialogListener{
        void getNewCollectionName(String name);

    }
}
