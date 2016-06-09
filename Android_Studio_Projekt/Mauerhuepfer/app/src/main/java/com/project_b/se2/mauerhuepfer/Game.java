package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anita on 03.05.2016.
 */
public class Game {
    //TODO check which methods should be private/public (at the end, maybe?).

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
    List<Block> possibleDestinationBlocks;
    private int startColPos;
    private int startRowPos;

    /**
     * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
     * and keeps the alpha intact.
     */
    private static final float[] NEGATIVE_FILTER = { //TODO find a more suitable filter than just a negative.
            -1.0f, 0, 0, 0, 255, // red
            0, -1.0f, 0, 0, 255, // green
            0, 0, -1.0f, 0, 255, // blue
            0, 0, 0, 1.0f, 0  // alpha
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
        this.possibleDestinationBlocks = new ArrayList<Block>();
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
                    //System.out.println("Clicked player view at " + event.getX() + "|" + event.getY()); //TODO delete this debug info before launch.
                    for (Player player : players) { //TODO handle the difference between own figures and non-selectable figures of other players
                        Figure[] figures = player.getFigures();
                        for (Figure fig : figures) {
                            if (fig.getImage().getBounds().contains((int) event.getX(), (int) event.getY())) {
                                if (selectedFigure != null) {
                                    selectedFigure.getImage().setColorFilter(null); //Revert old selected figure
                                }
                                selectedFigure = fig;
                                selectedFigure.getImage().setColorFilter(new ColorMatrixColorFilter(NEGATIVE_FILTER)); //Change new selected figure
                                playerView.invalidate();
                                calculatePossibleMoves();
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });

        gameBoardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //System.out.println("Clicked game board view at " + event.getX() + "|" + event.getY()); //TODO delete this debug info before launch.
                    if (!possibleDestinationBlocks.isEmpty()){
                        for (Block block : possibleDestinationBlocks) {
                            if (block.getImage().getBounds().contains((int) event.getX(), (int) event.getY())){
                                moveSelectedFigureAndTidyUp(block.getColPos(), block.getRowPos());
                                return true;
                            }
                        }
                    }
                }
                return false;
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
        currentBlock.setColPos(col);
        currentBlock.setRowPos(row);

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
                int wallNumber = getRandomNumberBetweenMinMax(1,6);
                currentBlock.setWallNumber(wallNumber);
                switch (wallNumber){
                    case 1:  drawable = resources.getDrawable(R.drawable.wall_1); break;
                    case 2:  drawable = resources.getDrawable(R.drawable.wall_2); break;
                    case 3:  drawable = resources.getDrawable(R.drawable.wall_3); break;
                    case 4:  drawable = resources.getDrawable(R.drawable.wall_4); break;
                    case 5:  drawable = resources.getDrawable(R.drawable.wall_5); break;
                    case 6:  drawable = resources.getDrawable(R.drawable.wall_6); break;
                    default: drawable = resources.getDrawable(R.drawable.empty);
                }
                break;
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

        Drawable clone = drawable.getConstantState().newDrawable().mutate(); //Deep copy to avoid blocks sharing the same image.
        int lengthPos = col * unit;
        int heightPos = row * unit;
        clone.setBounds(heightPos, lengthPos, (heightPos + unit), (lengthPos + unit));
        currentBlock.setImage(clone);
    }

    public void initializePlayers() {
        players = new Player[numberOfPlayers];
        // TODO Maybe let the player choose their own colour?
        for (int colour = 0; colour < numberOfPlayers; colour++) {
            players[colour] = new Player(context, colour);
        }
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
        //TODO Do something meaningful here. (currently used for testing ideas).
    }

    public boolean moveFigureForward(Figure figure) {
        int direction = gameBoard[figure.getColPos()][figure.getRowPos()].getNextBlock();
        switch (direction) {
            case UP: figure.walkUp(); break;
            case RIGHT: figure.walkRight(); break;
            case DOWN: figure.walkDown(); break;
            case LEFT: figure.walkLeft(); break;
            case BASE: return false;
            case GOAL: break; //TODO handle this case.
            case START: figure.setPos(startColPos, startRowPos); break;
        }
        return true;
    }

    public boolean moveFigureBackward(Figure figure) {
        int direction = gameBoard[figure.getColPos()][figure.getRowPos()].getPreviousBlock();
        switch (direction) {
            case UP: figure.walkUp(); break;
            case RIGHT: figure.walkRight(); break;
            case DOWN: figure.walkDown(); break;
            case LEFT: figure.walkLeft(); break;
            case BASE: return false;
            case GOAL: break; //TODO handle this case.
            case START: figure.setPos(startColPos, startRowPos); break;
        }
        return true;
    }

    public void setSelectedDiceNumber(int selectedDiceNumber) {
        //TODO find a way to stop dices from being "used up" if they are only selected, but not used to move.
        this.selectedDiceNumber = selectedDiceNumber;
        calculatePossibleMoves();
    }


    public void calculatePossibleMoves() {
        if (selectedFigure != null && selectedDiceNumber != -1) {// TODO maybe add an "else" branch which gives some sort of indication to the user?
            Figure ghostFig = new Figure(-1);                                           //Create new invisible ghost figure
            ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());    //Place the ghost figure on the selected figure.
            clearPossibleDestinationBlocks();                                           //Clear the list to remove any blocks from previous uses.


            //Check way forward.  //TODO Handle behavior around GOAL area better.
            int blocksMoved;                                                                                            //Number of actually traversed blocks.
            for (blocksMoved = 0; blocksMoved < selectedDiceNumber && moveFigureForward(ghostFig); blocksMoved++) {}    //Move ghost figure forward for the amount on selected dice.
            if (blocksMoved == selectedDiceNumber) {                                                                    //Check if all of the possible moves were used.
                possibleDestinationBlocks.add(gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()]);                   //Add ghost figures position as new possible destination block.
            }
            ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());                                    //Reset ghost figures position.


            //Check way backward.
            for (blocksMoved = 0; blocksMoved < selectedDiceNumber && moveFigureBackward(ghostFig); blocksMoved++) {}   //Move ghost figure forward for the amount on selected dice.
            if (blocksMoved == selectedDiceNumber) {                                                                    //Check if all of the possible moves were used.
                possibleDestinationBlocks.add(gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()]);                   //Add ghost figures position as new possible destination block.
            }
            ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());                                    //Reset ghost figures position.


            //Check way up.
            if (ghostFig.getColPos() - 1 > 0) {                                                             //Check if there is a block above.
                ghostFig.walkUp();                                                                          //Walk up to potentially find a wall block.
                if (gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()].getWallNumber() == selectedDiceNumber) {
                    ghostFig.walkUp();                                                                      //If the block is a wall block and the number fits the dice, then "jump" over the wall.
                    possibleDestinationBlocks.add(gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()]);   //Add ghost figures position as new possible destination block.
                }
                ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());                    //Reset ghost figures position.
            }

            //Check way down.
            if (ghostFig.getColPos() + 1 < gameBoard.length) {                                              //Check if there is a block below.
                ghostFig.walkDown();                                                                        //Walk down to potentially find a wall block.
                if (ghostFig.getColPos() < gameBoard.length && gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()].getWallNumber() == selectedDiceNumber) {
                    ghostFig.walkDown();                                                                    //If the block is a wall block and the number fits the dice, then "jump" over the wall.
                    possibleDestinationBlocks.add(gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()]);   //Add ghost figures position as new possible destination block.
                }
                ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());                    //Reset ghost figures position.
            }

            //Highlight possible destination blocks
            for (Block block : possibleDestinationBlocks) {
                block.getImage().setColorFilter(new ColorMatrixColorFilter(NEGATIVE_FILTER)); //TODO maybe use a more suitable filter than negative?
            }
            gameBoardView.invalidate();
        }
    }

    public void moveSelectedFigureAndTidyUp(int col, int row){
        selectedFigure.setPos(col, row);
        //System.out.println("selected figure: " + selectedFigure.getOwnerID() + " | " + selectedFigure.getColPos() + " | " + selectedFigure.getRowPos()); //TODO delete this debug info before launch.
        clearPossibleDestinationBlocks();
        selectedDiceNumber = -1;

        playerView.invalidate(); //TODO Decide where this should be called.
        gameBoardView.invalidate();
        //TODO decide if players have to use both dice on the same figure (ask Anita?).
    }

    public void clearPossibleDestinationBlocks(){
        for (Block block : possibleDestinationBlocks) {
            block.getImage().setColorFilter(null);
        }
        possibleDestinationBlocks.clear();
    }

    /**
     * @param min lowest integer allowed.
     * @param max highest integer allowed.
     * @return random integer  in the interval [min,max].
     */
    private int getRandomNumberBetweenMinMax (int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
