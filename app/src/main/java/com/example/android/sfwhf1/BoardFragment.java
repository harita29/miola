package com.example.android.sfwhf1;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by Magisus on 4/7/2015.
 */
public class BoardFragment extends Fragment {

    public static final String DIFF_KEY = "difficulty";

    private List<Integer> availableCardFaces;

    private Random rand;

    private ToggleButton activeCard;

    private Drawable cardBack;
    private Score.Difficulty difficulty;

    private int pairsFound;
    private int pairCount;

    private  Chronometer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rand = new Random();
        initializeCards();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.board_fragment, container, false);

        timer = (Chronometer) rootView.findViewById(R.id.timer);

        return rootView;
    }

    protected void setCardBack(Drawable cardBack) {
        this.cardBack = cardBack;
    }

    protected void setPairCount(int pairCount) {
        this.pairCount = pairCount;
    }

    private void initializeCards() {
        availableCardFaces = new ArrayList<>();
        availableCardFaces.add(R.drawable.apple200x200);
        availableCardFaces.add(R.drawable.banana200x200);
        availableCardFaces.add(R.drawable.bento200x200);
        availableCardFaces.add(R.drawable.buget200x200);
        availableCardFaces.add(R.drawable.cookie200x200);
        availableCardFaces.add(R.drawable.coffee200x200);
        availableCardFaces.add(R.drawable.corn200x200);
        availableCardFaces.add(R.drawable.cupcake200x200);
        availableCardFaces.add(R.drawable.fastfood200x200);
        availableCardFaces.add(R.drawable.garlic200x200);
        availableCardFaces.add(R.drawable.greentea200x200);
        availableCardFaces.add(R.drawable.hotdog200x200);
        availableCardFaces.add(R.drawable.kappamaki200x200);
        availableCardFaces.add(R.drawable.kiwi200x200);
        availableCardFaces.add(R.drawable.kudamono200x200);
        availableCardFaces.add(R.drawable.lemon200x200);
        availableCardFaces.add(R.drawable.macaron200x200);
        availableCardFaces.add(R.drawable.milktea200x200);
        availableCardFaces.add(R.drawable.miso200x200);
        availableCardFaces.add(R.drawable.misosoup200x200);
        availableCardFaces.add(R.drawable.nappa200x200);
        availableCardFaces.add(R.drawable.odango200x200);
        availableCardFaces.add(R.drawable.omurice200x200);
        availableCardFaces.add(R.drawable.onion200x200);
        availableCardFaces.add(R.drawable.onigirisake200x200);
        availableCardFaces.add(R.drawable.orange200x200);
        availableCardFaces.add(R.drawable.oshiruko200x200);
        availableCardFaces.add(R.drawable.parsimon200x200);
        availableCardFaces.add(R.drawable.pancake200x200);
        availableCardFaces.add(R.drawable.pancake2200x200);
        availableCardFaces.add(R.drawable.pumpkin200x200);
        availableCardFaces.add(R.drawable.pizza200x200);
        availableCardFaces.add(R.drawable.radish200x200);
        availableCardFaces.add(R.drawable.ramen200x200);
        availableCardFaces.add(R.drawable.salmon200x200);
        availableCardFaces.add(R.drawable.sandwich200x200);
        availableCardFaces.add(R.drawable.shabushabu200x200);
        availableCardFaces.add(R.drawable.veggystick200x200);
        availableCardFaces.add(R.drawable.watermelon200x200);
        availableCardFaces.add(R.drawable.yosenabe200x200);
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.stop();
    }

    protected void setDifficulty(Score.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Return a card image that has not yet been added to the deck, removing it from the pool of
     * available cards.
     */
    private int getAvailableCardFace() {
        return availableCardFaces.remove(rand.nextInt(availableCardFaces.size()));
    }

    /**
     * Create the pool of card faces, randomly selected. Each one is added twice to ensure
     * exactly two copies of each card appear on the board
     */
    protected List<Integer> selectCardPool(int uniqueCardCount) {
        List<Integer> cardFaces = new ArrayList<>();
        for (int i = 0; i < uniqueCardCount; i++) {
            int cardFace = getAvailableCardFace();
            cardFaces.add(cardFace);
            cardFaces.add(cardFace);
        }
        Collections.shuffle(cardFaces);
        return cardFaces;
    }

    protected int calculateCardWidth(int columns, double scalingFactor) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (int) (size.x / (columns + scalingFactor));
    }

    protected void addButtonsToGrid(GridLayout grid, int columns, int rows, int cardWidth) {
        final List<Integer> cardFaces = selectCardPool(pairCount);
        for (int i = 0; i < columns * rows; i++) {
            final ToggleButton button = new ToggleButton(getActivity());
            //Wanted to use a style here but couldn't get it working
            button.setTextOff("");
            button.setTextOn("");
            button.setBackground(cardBack);
            button.setChecked(false);
            button.setId(i);
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        button.setBackground(getActivity().getResources().getDrawable(cardFaces
                                .get(button.getId())));
                        checkAgainstActiveCard(button);
                    } else {
                        button.setBackground(cardBack);
                        button.setEnabled(true);
                    }
                }
            });
            grid.addView(button, cardWidth, cardWidth);
        }
    }

    protected void checkAgainstActiveCard(final ToggleButton newFlip) {
        if (activeCard != null) {
            //Found a pair
            if (activeCard.getBackground().getConstantState().equals(newFlip.getBackground()
                    .getConstantState())) {
                activeCard.setEnabled(false);
                newFlip.setEnabled(false);
                activeCard = null;
                pairsFound++;
                if (pairsFound == pairCount) {
                    endGame();
                }
            } else { //Did not find a pair
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activeCard.toggle();
                        activeCard.setEnabled(true);
                        activeCard = null;
                        newFlip.setChecked(false);
                        newFlip.setBackground(cardBack);
                        newFlip.setEnabled(true);
                    }
                }, 350);
            }
        } else { //First card to be flipped
            activeCard = newFlip;
            activeCard.setEnabled(false);
        }
    }


    private void endGame() {
        timer.stop();

        ((GameScreenActivity) getActivity()).showEndGameNavDialog(timer.getText().toString());
    }
}
