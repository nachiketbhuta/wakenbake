package nshmadhani.com.wakenbake.main_screens.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class ErrorDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Error!")
                    .setMessage("An error occurred.")
                    .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
