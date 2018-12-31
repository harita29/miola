package com.example.android.sfwhf1;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

/**
 * Created by Magisus on 4/6/2015.
 */
public class HardBoardFragment extends BoardFragment {

    public static final String TAG = "HardBoardFragment";
    public static final int GRID_WIDTH = 6;
    public static final int GRID_HEIGHT = 7;
    public static final int PAIR_COUNT = GRID_HEIGHT * GRID_WIDTH / 2;

    private Resources res;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        res = getActivity().getResources();

        RelativeLayout baseLayout = (RelativeLayout) getActivity().findViewById(R.id.bottomLayout);
        int backgroundColor = res.getColor(R.color.grey);
        baseLayout.setBackgroundColor(backgroundColor);
        rootView.setBackgroundColor(backgroundColor);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.blue)));

        GridLayout grid = (GridLayout) rootView.findViewById(R.id.boardLayout);
        grid.setRowCount(GRID_HEIGHT);

        setCardBack(res.getDrawable(R.drawable.darkpurple200x200));
        setPairCount(PAIR_COUNT);
        setDifficulty(Score.Difficulty.HARD);

        Chronometer timer = (Chronometer) rootView.findViewById(R.id.timer);
        timer.setTextSize(25);

        addButtonsToGrid(grid, GRID_WIDTH, GRID_HEIGHT, calculateCardWidth(GRID_WIDTH, 1.7));
        return rootView;
    }
}
