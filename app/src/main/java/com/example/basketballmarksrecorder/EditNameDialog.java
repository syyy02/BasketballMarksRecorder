package com.example.basketballmarksrecorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditNameDialog extends AppCompatDialogFragment {
    private EditText ETeditTeamA, ETeditTeamB;
    private EditNameDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view).setTitle("Team Name").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String teamNameA = ETeditTeamA.getText().toString();
                String teamNameB = ETeditTeamB.getText().toString();
                listener.applyTexts(teamNameA, teamNameB);

            }
        });
        ETeditTeamA = view.findViewById(R.id.ETeditTeamA);
        ETeditTeamB = view.findViewById(R.id.ETeditTeamB);
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (EditNameDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement EditnameDialogListener");
        }

    }

    public interface EditNameDialogListener{
        void applyTexts(String teamNameA, String teamNameB);
    }
}
