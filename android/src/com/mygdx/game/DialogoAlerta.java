package com.mygdx.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogoAlerta extends DialogFragment {



    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("Hubo un problema con la utentificacion por lo que se va a cerrar el programa")
                .setTitle("Información")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getActivity().finish();
                    }
                });

        return builder.create();
    }

}
