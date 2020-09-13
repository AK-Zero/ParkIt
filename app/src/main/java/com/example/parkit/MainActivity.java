package com.example.parkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements viewInt{

    ImageButton redo , peek;
    myView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        redo = findViewById(R.id.redo);
        peek = findViewById(R.id.peek);
        gameView = findViewById(R.id.game);
        gameView.setActivity(this);

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.redo();
            }
        });
        peek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.peeker();
            }
        });
    }

    @Override
    public void setscore(int score) {
        DialogFragment dialogFragment = new ScoreDialog(score);
        dialogFragment.show(getSupportFragmentManager() , "dialog");
    }
}
