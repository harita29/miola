package com.example.android.sfwhf1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Magisus on 4/12/2015.
 */
public class EndGameNavDialog extends DialogFragment
{

    public static final String TAG = "EndGameNavDialog";

    private static final String TIME_ARG = "TIME";
    private Calendar date = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    private String currentDate = dateFormat.format(date.getTime());
    //Firebase
    private String uID;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseOrigin;

    TextView points;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You win!");
        builder.setCancelable(false);

        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        builder.setMessage("Your time:\n" + getArguments().getString(TIME_ARG));

        builder.setPositiveButton(getActivity().getString(R.string.play_again),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EndGameNavDialog.this.startActivity(new Intent(getActivity(), GameScreenActivity.class));
                    }
                });
        builder.setNegativeButton(getActivity().getString(R.string.main_menu), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EndGameNavDialog.this.startActivity(new Intent(getActivity(), MainMenuActivity.class));
            }

        });


        return builder.create();


    }

    static EndGameNavDialog newInstance(String time){
        EndGameNavDialog dialog = new EndGameNavDialog();

        Bundle args = new Bundle();
        args.putString(TIME_ARG, time);
        dialog.setArguments(args);
        return dialog;

    }
}
