package nshmadhani.com.wakenbake.main_screens.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;

/**
 * Created by Nachiket on 04-Feb-18.
 */

public class NoInternetConnectionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection")
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

}
