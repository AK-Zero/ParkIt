package com.example.parkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ScoreDialog extends DialogFragment {

    int score;
    ScoreDialog(int score){
        this.score = score;
    }

    TextView scoretv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.score_dialog , container , false);

        scoretv = v.findViewById(R.id.score);
        if(score==-5){
            scoretv.setText("Buses in your path!!!");
        }
        else {
            scoretv.setText("You collected " + score + " coins!!!");
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 700;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

}
