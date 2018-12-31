package com.example.android.sfwhf1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class GameScreenActivity extends ActionBarActivity implements DifficultyDialog
        .OptionsFragmentInterface {

    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDifficultyDialog();
    }

    private void showDifficultyDialog() {
        DifficultyDialog difficultySelect = new DifficultyDialog();
        difficultySelect.show(getSupportFragmentManager(), DifficultyDialog.TAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            getGameBoard(difficulty);
            return true;
        }

        return false;
    }

    private void getGameBoard(String tag) {
        Fragment fragment = null;
        if (EasyBoardFragment.TAG.equals(tag)) {
            fragment = new EasyBoardFragment();
        } else if (MediumBoardFragment.TAG.equals(tag)) {
            fragment = new MediumBoardFragment();
        } else if (HardBoardFragment.TAG.equals(tag)) {
            fragment = new HardBoardFragment();
        }

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.gameScreenContainer, fragment, tag);
            trans.commit();
        }
    }





    @Override
    public void onOptionsFragmentResult(String difficulty) {
        if (getString(R.string.easy_text).equals(difficulty)) {

/**

 AlertDialog.Builder alert = new AlertDialog.Builder(
 GameScreenActivity.this);
 alert.setTitle("Alert Message");
 alert.setMessage("Must complete game within 30 seconds to gain a point");
 //if yes....
 alert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {


 public void onClick(DialogInterface dialog, int which) {

 dialog.dismiss();

 }
 });
 //if no....
 alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {


 public void onClick(DialogInterface dialog, int which) {

 dialog.dismiss();
 }
 });

 alert.show();
 */

            this.difficulty = EasyBoardFragment.TAG;

        } else if (getString(R.string.medium_text).equals(difficulty)) {
            this.difficulty = MediumBoardFragment.TAG;
        } else if (getString(R.string.hard_text).equals(difficulty)) {
            this.difficulty = HardBoardFragment.TAG;
        } else {
            this.difficulty = EasyBoardFragment.TAG;
        }
        getGameBoard(this.difficulty);
    }



    public void showEndGameNavDialog(String time) {
        EndGameNavDialog navDialog = EndGameNavDialog.newInstance(time);
        navDialog.show(getSupportFragmentManager(), EndGameNavDialog.TAG);
    }
}
