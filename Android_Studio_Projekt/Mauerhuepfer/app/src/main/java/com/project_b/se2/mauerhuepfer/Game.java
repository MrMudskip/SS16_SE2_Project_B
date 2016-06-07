package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Anita on 03.05.2016.
 */
public class Game {

    //Player colours
    static final int RED = 0;
    static final int GREEN = 1;
    static final int YELLOW = 2;
    static final int BLACK = 3;

    //Directions
    static final int UP = 0;
    static final int RIGHT = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;
    static final int BASE = 4;
    static final int GOAL = 5;
    static final int START = 6;

    //Block types
    static final int S = 0;
    static final int HR = 1;
    static final int HL = 2;
    static final int EU = 3;
    static final int ER = 4;
    static final int ED = 5;
    static final int EL = 6;
    static final int V = 7;
    static final int F = 8;
    static final int W = 9;
    static final int N = 10;
    static final int BR = 11;
    static final int BG = 12;
    static final int BY = 13;
    static final int BB = 14;
    static final int GR = 15;
    static final int GG = 16;
    static final int GY = 17;
    static final int GB = 18;

    //Measurement variables
    static int unit;

    // Other variables
    private Context context;
    private Resources resources;
    private CustomGameBoardView gameBoardView;
    private CustomPlayerView playerView;
    private int numberOfPlayers;
    private Player[] players;
    private Figure selectedFigure;
    private int selectedDiceNumber;
    private int startColPos;
    private int startRowPos;

    /**
     * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
     * and keeps the alpha intact.
     */
    private static final float[] NEGATIVE_FILTER = {
            -1.0f,     0,     0,    0, 255, // red
                0, -1.0f,     0,    0, 255, // green
                0,     0, -1.0f,    0, 255, // blue
                0,     0,     0, 1.0f,   0  // alpha
    };


    /**
     * 2D array containing all the blocks that form the game board.   // TODO find a solution for different wall numbers.
     */
    private Block[][] gameBoard = {
            {new Block(GB), new Block(GB), new Block(GB), new Block(GB), new Block(N), new Block(GG), new Block(GG), new Block(GG), new Block(GG)},
            {new Block(GY), new Block(GY), new Block(GY), new Block(GY), new Block(N), new Block(GR), new Block(GR), new Block(GR), new Block(GR)},
            {new Block(ER), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(F)},
            {new Block(V), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W)},
            {new Block(EU), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(ED)},
            {new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(V)},
            {new Block(ER), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(EL)},
            {new Block(V), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W)},
            {new Block(EU), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(ED)},
            {new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(V)},
            {new Block(ER), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(HR), new Block(EL)},
            {new Block(V), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W), new Block(W)},
            {new Block(EU), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(HL), new Block(S)},
            {new Block(BB), new Block(BB), new Block(BY), new Block(BY), new Block(N), new Block(BG), new Block(BG), new Block(BR), new Block(BR)},
            {new Block(BB), new Block(BB), new Block(BY), new Block(BY), new Block(N), new Block(BG), new Block(BG), new Block(BR), new Block(BR)},
    };


    public Game(Context context, int numberOfPlayers) {
        //Initialise variables
        this.context = context;
        this.resources = this.context.getResources();
        this.numberOfPlayers = numberOfPlayers;
        this.selectedFigure = null;
        this.selectedDiceNumber = -1;
        this.startColPos = -1;
        this.startRowPos = -1;

        //Calculate measurement unit
        unit = (int) ((this.resources.getDisplayMetrics().heightPixels / gameBoard.length) * 0.8); // TODO: Find a way to get view size instead of screen size, so the scaling with 0.8 isn't necessary.

        //Set up game logic
        initializeGameBoard();
        initializePlayers();

        //Set up game views
        gameBoardView = (CustomGameBoardView) ((Activity) context).findViewById(R.id.CustomGameBoardView);
        gameBoardView.setGameBoard(gameBoard);
        playerView = (CustomPlayerView) ((Activity) context).findViewById(R.id.CustomPlayerView);
        playerView.setPlayers(players);

        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for (Player player : players) {
                        Figure[] figures = player.getFigures();
                        for (Figure fig : figures) {
                            if (fig.getImage().getBounds().contains((int) event.getX(), (int) event.getY())) {
                                if (selectedFigure != null) {
                                    selectedFigure.getImage().setColorFilter(null); //Revert old selected figure
                                }
                                selectedFigure = fig;
                                selectedFigure.getImage().setColorFilter(new ColorMatrixColorFilter(NEGATIVE_FILTER)); //Change new selected figure
                                playerView.invalidate();
                            }
                        }
                    }
                }
                return true;
            }
        });

    }

    public boolean initializeGameBoard() {
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                setBlockParametersByType(gameBoard, col, row);
            }
        }
        return true;
    }


    public void setBlockParametersByType(Block[][] gameBoard, int col, int row) {
        //Create variables
        Block currentBlock = gameBoard[col][row];

        //Set images
        Drawable drawable;
        switch (currentBlock.getType()) {
            case S:
                drawable = resources.getDrawable(R.drawable.circle_white_l_arrow);
                currentBlock.setNextBlock(LEFT);
                currentBlock.setPreviousBlock(BASE);
                startColPos = col;
                startRowPos = row;
                break;
            case HR:
                drawable = resources.getDrawable(R.drawable.circle_white_rl);
                currentBlock.setNextBlock(RIGHT);
                currentBlock.setPreviousBlock(LEFT);
                break;
            case HL:
                drawable = resources.getDrawable(R.drawable.circle_white_rl);
                currentBlock.setNextBlock(LEFT);
                currentBlock.setPreviousBlock(RIGHT);
                break;
            case EU:
                drawable = resources.getDrawable(R.drawable.circle_white_ur);
                currentBlock.setNextBlock(UP);
                currentBlock.setPreviousBlock(RIGHT);
                break;
            case ER:
                drawable = resources.getDrawable(R.drawable.circle_white_rd);
                currentBlock.setNextBlock(RIGHT);
                currentBlock.setPreviousBlock(DOWN);
                break;
            case ED:
                drawable = resources.getDrawable(R.drawable.circle_white_dl);
                currentBlock.setNextBlock(LEFT);
                currentBlock.setPreviousBlock(DOWN);
                break;
            case EL:
                drawable = resources.getDrawable(R.drawable.circle_white_ul);
                currentBlock.setNextBlock(UP);
                currentBlock.setPreviousBlock(LEFT);
                break;
            case V:
                drawable = resources.getDrawable(R.drawable.circle_white_ud);
                currentBlock.setNextBlock(UP);
                currentBlock.setPreviousBlock(DOWN);
                break;
            case F:
                drawable = resources.getDrawable(R.drawable.circle_blue_l);
                currentBlock.setNextBlock(GOAL);
                currentBlock.setPreviousBlock(LEFT);
                break;
            case W:
                drawable = resources.getDrawable(R.drawable.wall_6);
                break; //TODO handle different wall numbers
            case N:
                drawable = resources.getDrawable(R.drawable.empty);
                break;
            case BR:
                drawable = resources.getDrawable(R.drawable.circle_red);
                currentBlock.setNextBlock(START);
                break;
            case BG:
                drawable = resources.getDrawable(R.drawable.circle_green);
                currentBlock.setNextBlock(START);
                break;
            case BY:
                drawable = resources.getDrawable(R.drawable.circle_yellow);
                currentBlock.setNextBlock(START);
                break;
            case BB:
                drawable = resources.getDrawable(R.drawable.circle_black);
                currentBlock.setNextBlock(START);
                break;
            case GR:
                drawable = resources.getDrawable(R.drawable.circle_red);
                break;
            case GG:
                drawable = resources.getDrawable(R.drawable.circle_green);
                break;
            case GY:
                drawable = resources.getDrawable(R.drawable.circle_yellow);
                break;
            case GB:
                drawable = resources.getDrawable(R.drawable.circle_black);
                break;
            default:
                drawable = resources.getDrawable(R.drawable.empty);
        }
        int lengthPos = col * unit;
        int heightPos = row * unit;
        drawable.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        currentBlock.setImage(drawable);
    }

    public void initializePlayers() {
        players = new Player[numberOfPlayers];
        // TODO Maybe let the player choose their own colour?
        for (int colour = 0; colour < numberOfPlayers; colour++) {
            players[colour] = new Player(context, colour);
        }

        //TODO Figure out why this is not working.

        players[RED].getFigures()[0].setPos(13, 7);
        players[RED].getFigures()[1].setPos(13, 8);
        players[RED].getFigures()[2].setPos(14, 7);
        players[RED].getFigures()[3].setPos(14, 8);
        players[GREEN].getFigures()[0].setPos(13, 5);
        players[GREEN].getFigures()[1].setPos(13, 6);
        players[GREEN].getFigures()[2].setPos(14, 5);
        players[GREEN].getFigures()[3].setPos(14, 6);
        players[YELLOW].getFigures()[0].setPos(13, 2);
        players[YELLOW].getFigures()[1].setPos(13, 3);
        players[YELLOW].getFigures()[2].setPos(14, 2);
        players[YELLOW].getFigures()[3].setPos(14, 3);
        players[BLACK].getFigures()[0].setPos(13, 0);
        players[BLACK].getFigures()[1].setPos(13, 1);
        players[BLACK].getFigures()[2].setPos(14, 0);
        players[BLACK].getFigures()[3].setPos(14, 1);
    }

    public void rollDice() { //TODO Think of a better name for this method.
        // TODO Do something meaningful here. (currently used for testing ideas).
    }

    public void moveFigureForward(Figure figure){
        int direction = gameBoard[figure.getColPos()][figure.getRowPos()].getNextBlock();
        switch (direction){
            case UP: figure.walkUp(); break;
            case RIGHT: figure.walkRight(); break;
            case DOWN: figure.walkDown(); break;
            case LEFT: figure.walkLeft(); break;
            case BASE: break; //TODO handle this case.
            case GOAL: break; //TODO handle this case.
            case START: figure.setPos(startColPos, startRowPos); break;
        }
        playerView.invalidate(); //TODO Decide where this should be called.
    }

    public void setSelectedDiceNumber(int selectedDiceNumber) {
        //this.selectedFigure = players[RED].getFigures()[0]; // TODO find out which figure was clicked.
        this.selectedDiceNumber = selectedDiceNumber;

        for (int i = 0; i < selectedDiceNumber; i++) {
            moveFigureForward(selectedFigure);
        }
    }


}
