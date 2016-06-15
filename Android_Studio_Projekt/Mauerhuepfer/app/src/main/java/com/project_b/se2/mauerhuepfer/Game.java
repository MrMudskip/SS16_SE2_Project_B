package com.project_b.se2.mauerhuepfer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.project_b.se2.mauerhuepfer.interfaces.INetworkManager;
import com.project_b.se2.mauerhuepfer.interfaces.IReceiveMessage;

import java.util.ArrayList;
import java.util.List;


public class Game {

    //Colours
    static final int RED = 0;
    static final int GREEN = 1;
    static final int YELLOW = 2;
    static final int BLACK = 3;
    static final int FilterColor = Color.GRAY;
    static final PorterDuff.Mode FilterMode = PorterDuff.Mode.MULTIPLY;

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

    //Game variables
    private int numberOfPlayers;
    private Player[] players;
    private int myPID;
    private int currentPlayerIndex;
    private Figure selectedFigure;
    private int selectedDiceNumber;
    List<Block> possibleDestinationBlocks;
    private int startColPos;
    private int startRowPos;
    private int endColPos;
    private int endRowPos;

    //Other variables
    private Context context;
    private Resources resources;
    private CustomGameBoardView gameBoardView;
    private CustomPlayerView playerView;
    private Dice dice;
    private INetworkManager networkManager;
    private UpdateState update;


    /**
     * 2D array containing all the blocks that form the game board.
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


    public Game(Context context, int numberOfPlayers, INetworkManager manager, final int myPID) {
        //Initialise variables
        this.context = context;
        this.resources = this.context.getResources();
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerIndex = -1;
        this.selectedFigure = null;
        this.selectedDiceNumber = -1;
        this.possibleDestinationBlocks = new ArrayList<Block>();
        this.startColPos = -1;
        this.startRowPos = -1;
        this.endColPos = -1;
        this.endRowPos = -1;
        this.networkManager = manager;
        this.dice = new Dice(context, this);
        this.update = new UpdateState();
        this.myPID = myPID;

        //Calculate measurement unit
        int vertical = (int) ((this.resources.getDisplayMetrics().heightPixels / gameBoard.length) * 0.8);
        int horizontal = (int) ((this.resources.getDisplayMetrics().widthPixels / gameBoard[0].length) * 0.8);
        unit = Math.min(vertical, horizontal); // TODO: Find a way to get view size instead of screen size, so the scaling with 0.8 isn't necessary.

        //Set up game logic
        initializePlayers();
        initialiseGameBoard();

        //Set up game views
        gameBoardView = (CustomGameBoardView) ((Activity) context).findViewById(R.id.CustomGameBoardView); // TODO find a way to make sure the gameBoardView is centered.
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
                                if (fig.getOwner().getPID() == players[currentPlayerIndex].getPID() && fig.getOwner().getPID() == myPID) {
                                    // Only handles taps on current player's figures.
                                    if (selectedFigure != null) {
                                        // Deselect previous selected figure.
                                        selectedFigure.getImage().clearColorFilter();
                                    }
                                    if (fig == selectedFigure) {
                                        // Deselect selected figure.
                                        selectedFigure.getImage().clearColorFilter();
                                        clearPossibleDestinationBlocks();
                                        selectedFigure = null;
                                        playerView.invalidate();
                                    } else {
                                        //TODO Do not allow selection of another figure after at least one dice was used on a figure.
                                        // Select unselected figure.
                                        selectedFigure = fig;
                                        selectedFigure.getImage().setColorFilter(FilterColor, FilterMode); //Change new selected figure // TODO look into this way of colouring
                                        playerView.invalidate();
                                        calculatePossibleMoves();
                                    }
                                    return true;
                                }
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
                    if (!possibleDestinationBlocks.isEmpty()) {
                        for (Block block : possibleDestinationBlocks) {
                            if (block.getImage().getBounds().contains((int) event.getX(), (int) event.getY())) {
                                moveSelectedFigureAndTidyUp(block.getColPos(), block.getRowPos());
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });

        // TODO Use netcode's player selection for this. (@Bernhard)
        // TODO Tell the current player it's his turn. [Maybe @Bernhard?)
        currentPlayerIndex = getRandomNumberBetweenMinMax(0, numberOfPlayers - 1);
    }

    public Dice getDice() {
        return dice;
    }

    public void handleUpdate(UpdateState update) {
        switch (update.getUsage()) {
            case IReceiveMessage.USAGE_NEXTPLAYER:
                increaseCurrentPlayerIndex();
        }
    }

    private boolean initialiseGameBoard() {
        for (int col = 0; col < gameBoard.length; col++) {
            for (int row = 0; row < gameBoard[col].length; row++) {
                setBlockParametersByType(gameBoard, col, row);
            }
        }
        return true;
    }

    private void setBlockParametersByType(Block[][] gameBoard, int col, int row) {
        //Create variables
        Block currentBlock = gameBoard[col][row];

        //Assign block position
        currentBlock.setColPos(col);
        currentBlock.setRowPos(row);

        //Set type specific attributes
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
                endColPos = col;
                endRowPos = row;
                break;
            case W:
                int wallNumber = getRandomNumberBetweenMinMax(1, 6);
                currentBlock.setWallNumber(wallNumber);
                switch (wallNumber) {
                    case 1:
                        drawable = resources.getDrawable(R.drawable.wall_1);
                        break;
                    case 2:
                        drawable = resources.getDrawable(R.drawable.wall_2);
                        break;
                    case 3:
                        drawable = resources.getDrawable(R.drawable.wall_3);
                        break;
                    case 4:
                        drawable = resources.getDrawable(R.drawable.wall_4);
                        break;
                    case 5:
                        drawable = resources.getDrawable(R.drawable.wall_5);
                        break;
                    case 6:
                        drawable = resources.getDrawable(R.drawable.wall_6);
                        break;
                    default:
                        drawable = resources.getDrawable(R.drawable.empty);
                }
                break;
            case N:
                drawable = resources.getDrawable(R.drawable.empty);
                break;
            case BR:
                drawable = resources.getDrawable(R.drawable.circle_red);
                currentBlock.setNextBlock(START);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case BG:
                drawable = resources.getDrawable(R.drawable.circle_green);
                currentBlock.setNextBlock(START);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case BY:
                drawable = resources.getDrawable(R.drawable.circle_yellow);
                currentBlock.setNextBlock(START);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case BB:
                drawable = resources.getDrawable(R.drawable.circle_black);
                currentBlock.setNextBlock(START);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case GR:
                drawable = resources.getDrawable(R.drawable.circle_red);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case GG:
                drawable = resources.getDrawable(R.drawable.circle_green);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case GY:
                drawable = resources.getDrawable(R.drawable.circle_yellow);
                assignBlockPositionToSuitableFigure(currentBlock);
                break;
            case GB:
                drawable = resources.getDrawable(R.drawable.circle_black);
                assignBlockPositionToSuitableFigure(currentBlock);
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

    private void initializePlayers() {
        players = new Player[numberOfPlayers];
        // TODO Maybe let the player choose their own colour?
        for (int colour = RED; colour < numberOfPlayers; colour++) {
            int PID = colour; //TODO This should use the real PID generated by the netcode (@Bernhard).
            players[colour] = new Player(context, PID, colour);
        }
    }

    /**
     * Find a figure of the right colour which has not yet set a base/goal position and assign it a block's position.
     */
    private void assignBlockPositionToSuitableFigure(Block block) {
        int colour = 0;
        boolean isBase = false;
        boolean isGoal = false;
        int col = block.getColPos();
        int row = block.getRowPos();

        //Determine suitable colour
        switch (block.getType()) {
            case BR:
                colour = RED;
                isBase = true;
                break;
            case BG:
                colour = GREEN;
                isBase = true;
                break;
            case BY:
                colour = YELLOW;
                isBase = true;
                break;
            case BB:
                colour = BLACK;
                isBase = true;
                break;
            case GR:
                colour = RED;
                isGoal = true;
                break;
            case GG:
                colour = GREEN;
                isGoal = true;
                break;
            case GY:
                colour = YELLOW;
                isGoal = true;
                break;
            case GB:
                colour = BLACK;
                isGoal = true;
                break;
        }

        if (players.length > colour) {
            boolean valuesAssigned = false;     //Keeps the block from assigning its position to more than one figure.
            for (int i = 0; i < players[colour].getFigures().length && !valuesAssigned; i++) {
                Figure figure = players[colour].getFigures()[i];
                if (isBase) {
                    if (figure.getBaseColPos() < 0) {    //Keeps the block from overriding already assigned figures.
                        figure.setBaseColPos(col);
                        figure.setBaseRowPos(row);
                        figure.setPos(col, row);
                        valuesAssigned = true;
                    }
                } else if (isGoal) {
                    if (figure.getGoalColPos() < 0) {    //Keeps the block from overriding already assigned figures.
                        figure.setGoalColPos(col);
                        figure.setGoalRowPos(row);
                        valuesAssigned = true;
                    }
                }
            }
        }
    }

    public void rollDice() {
        //TODO Do something meaningful here or delete this method. (currently only used as workaround).

    }

    private boolean moveFigureForward(Figure figure) {
        int direction = gameBoard[figure.getColPos()][figure.getRowPos()].getNextBlock();
        switch (direction) {
            case UP:
                figure.walkUp();
                break;
            case RIGHT:
                figure.walkRight();
                break;
            case DOWN:
                figure.walkDown();
                break;
            case LEFT:
                figure.walkLeft();
                break;
            case BASE:
                return false;
            case GOAL:
                return false;
            case START:
                figure.setPos(startColPos, startRowPos);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean moveFigureBackward(Figure figure) {
        int direction = gameBoard[figure.getColPos()][figure.getRowPos()].getPreviousBlock();
        switch (direction) {
            case UP:
                figure.walkUp();
                break;
            case RIGHT:
                figure.walkRight();
                break;
            case DOWN:
                figure.walkDown();
                break;
            case LEFT:
                figure.walkLeft();
                break;
            case BASE:
                return false;
            case GOAL:
                return false;
            case START:
                figure.setPos(startColPos, startRowPos);
                break;
            default:
                return false;
        }
        return true;
    }

    public void setSelectedDiceNumber(int selectedDiceNumber) {
        //TODO find a way to stop dices from being "used up" if they are only selected, but not used to move (@Markus).
        this.selectedDiceNumber = selectedDiceNumber;
        calculatePossibleMoves();
    }


    private void calculatePossibleMoves() {
        if (selectedFigure != null && selectedDiceNumber != -1) {// TODO maybe add an "else" branch which gives some sort of indication to the user?
            Figure ghostFig = new Figure(null);                                         //Create new invisible ghost figure
            ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());    //Place the ghost figure on the selected figure.
            clearPossibleDestinationBlocks();                                           //Clear the list to remove any blocks from previous uses.


            //Check way forward.
            int blocksMoved;                                                                                            //Number of actually traversed blocks.
            for (blocksMoved = 0; blocksMoved < selectedDiceNumber && moveFigureForward(ghostFig); blocksMoved++) {
            }    //Move ghost figure forward for the amount on selected dice.
            if (blocksMoved == selectedDiceNumber) {                                                                    //Check if all of the possible moves were used.
                possibleDestinationBlocks.add(gameBoard[ghostFig.getColPos()][ghostFig.getRowPos()]);                   //Add ghost figures position as new possible destination block.
            }
            ghostFig.setPos(selectedFigure.getColPos(), selectedFigure.getRowPos());                                    //Reset ghost figures position.


            //Check way backward.
            for (blocksMoved = 0; blocksMoved < selectedDiceNumber && moveFigureBackward(ghostFig); blocksMoved++) {
            }   //Move ghost figure forward for the amount on selected dice.
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

            // TODO Do not highlight blocks that are already occupied by another figure of SAME colour.
            //Highlight possible destination blocks
            for (Block block : possibleDestinationBlocks) {
                block.getImage().setColorFilter(FilterColor, FilterMode);
            }
            gameBoardView.invalidate();
        }
    }

    private void moveSelectedFigureAndTidyUp(int col, int row) {
        selectedFigure.setPos(col, row);
        checkAndHandleFigureCollision();
        tryMovingSelectedFigureIntoRespectiveGoal();
        clearPossibleDestinationBlocks();
        selectedDiceNumber = -1;
        clearSelectedDiceImage();
        playerView.invalidate(); //TODO Decide where this should be called.
        gameBoardView.invalidate(); //TODO Decide where this should be called.
        //TODO Make sure both dice are used on the same figure.
    }

    private void clearSelectedDiceImage() {
        if (dice.isDice1Selected()) {
            dice.dice1Used();
        }
        if (dice.isDice2Selected()) {
            dice.dice2Used();
        }
        if (dice.isDice1removed() && dice.isDice2removed()) {
            startNextTurn();
        }
    }

    private void checkAndHandleFigureCollision() {
        int colPos = selectedFigure.getColPos();
        int rowPos = selectedFigure.getRowPos();
        int PID = selectedFigure.getOwner().getPID();
        for (Player player : players) {
            for (Figure figure : player.getFigures()) {
                if (colPos == figure.getColPos() && rowPos == figure.getRowPos() && PID != figure.getOwner().getPID()) {
                    figure.setPos(figure.getBaseColPos(), figure.getBaseRowPos()); //Send figure back to base.
                }
            }
        }
    }

    private void tryMovingSelectedFigureIntoRespectiveGoal() {
        if (selectedFigure.getColPos() == endColPos && selectedFigure.getRowPos() == endRowPos) { //TODO Also make sure that both dice are used.
            selectedFigure.setPos(selectedFigure.getGoalColPos(), selectedFigure.getGoalRowPos());
        }
    }

    private void clearPossibleDestinationBlocks() {
        for (Block block : possibleDestinationBlocks) {
            block.getImage().clearColorFilter();
        }
        possibleDestinationBlocks.clear();
    }

    private void increaseCurrentPlayerIndex() {
        if (currentPlayerIndex + 1 >= numberOfPlayers) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }
    }

    public void startNextTurn() {
        selectedFigure.getImage().clearColorFilter();
        selectedFigure = null;
        increaseCurrentPlayerIndex();

        update.setUsage(IReceiveMessage.USAGE_NEXTPLAYER);
        networkManager.sendMessage(update);

        playerView.invalidate();
        dice.setDice1removed(false);
        dice.setDice2removed(false);
    }

    /**
     * @param min lowest integer allowed.
     * @param max highest integer allowed.
     * @return random integer  in the interval [min,max].
     */
    private int getRandomNumberBetweenMinMax(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}