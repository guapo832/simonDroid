package edu.umsl.yerby.simondroid.models;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyerby on 2/6/2016.
 */
public class Game {


    private static final int DELAY_DECAY = 30; //speed increases each round
    private static final int DELAY_MIN = 40; //delay minimum.
    private static final String USER_TURN_TOAST  = "Your turn";
    private static final String SIMON_TURN_TOST = "Simon Says...";
    private static final String YOU_LOST = "You lost in Round %d";
    private static final String ROUND_TEXT = "Round\n%d";
    private static final String RESTART_TEXT = "Click to Restart";
    private static final int SHOWBUTTON = 0;
    private static final int WAITSHOW = 1;
    private static final int DELAY=240;
    private static final String COLORIDS = "ColorIDSaveState";
    private static final String ROUND_SEQUENCE = "RoundSequence";
    private static final java.lang.String DIFFICULTY = "Difficulty";
    private static final String STATUS = "status";
    private static final java.lang.String DELAYKWD ="Delay";
    private static final java.lang.String DELAYDECAYKWD = "DelayDekayKwd";
    private static final java.lang.String CURRENTROUND = "CurrentRound";
    private static final java.lang.String HIGHSCORE ="HighScore" ;
    private static final java.lang.String SEQUENCEPOS = "SEquencepos";
    private static final String GAMEBOARD_SAVED_STATE = "edu.umsl.gyerby.simon.GAME_BOARD_SAVED_STATE" ;
    private static final String BUTTONSTATE ="BTNSTATE" ;
    //private int difficulty;
    public enum status {
        SHOWSEQUENCE,
        LISTENING,
        LOST,
        WAIT
    }
    public enum buttonState {
        PRESSED,
        RELEASED
    }
    public enum difficultyType{
        EASY,
        MEDIUM,
        HARD
    }
    public status gameStatus;
    private int[] mColorIDs;
    private List<Integer> mRoundSequence;
    private int mSequencePosition,mCurrentRound,mHighScore;
    private difficultyType mDifficulty;
    public int delay;
    private int delaydecay;
    private static final int LONG_DELAY = 3500;

    /***
     * constructor  using the color IDS of the buttons.
     * @param colorIDs
     */
    public Game(int[] colorIDs) {
        this.mColorIDs = colorIDs.clone();
        mSequencePosition = 0;
        mHighScore = 0;
        mRoundSequence = new ArrayList<Integer>();
        this.delay = DELAY;
        this.setDifficulty(difficultyType.EASY);
    }
    //hide from public
    private Game(){}


    public void setmButtonState(buttonState mButtonState) {
        this.mButtonState = mButtonState;
    }

    /***
     * for handling screen rotation
     * @param savedInstanceState
     */
    public void loadGameState(Bundle savedInstanceState) {
        this.gameStatus = status.WAIT;
        Bundle bundle = savedInstanceState.getBundle(GAMEBOARD_SAVED_STATE);
        int[] colorIDs = bundle.getIntArray(COLORIDS);
        ArrayList<Integer> roundSeqList = bundle.getIntegerArrayList(ROUND_SEQUENCE);
        difficultyType difficulty = (difficultyType) bundle.getSerializable(DIFFICULTY);

        status gameStatus = (status) bundle.getSerializable(STATUS);
       this.gameStatus = gameStatus;
        Log.d("LOADSTATE", "Game Status: " + gameStatus.toString());
        this.setRoundSequence(roundSeqList);
        this.setDifficulty(difficulty);
        this.setDelay(bundle.getInt(DELAYKWD));
        this.setmButtonState((buttonState) bundle.getSerializable(BUTTONSTATE));
        this.setDelaydecay(bundle.getInt(DELAYDECAYKWD));
        this.setCurrentRound(bundle.getInt(CURRENTROUND));
        this.setHighScore(bundle.getInt(HIGHSCORE));
        this.setSequencePosition(bundle.getInt(SEQUENCEPOS));
        Log.d("LOADSTATE", String.valueOf(this.getSequencePosition()));
        this.setButtonHandler();


    }

    /***
     * for handling screen rotations
     * @param state
     */
    public void saveGameState(Bundle state) {
        Bundle bundle = new Bundle();
        bundle.putIntArray(COLORIDS, this.mColorIDs);
        bundle.putIntegerArrayList(ROUND_SEQUENCE, (ArrayList<Integer>) this.mRoundSequence);
        bundle.putSerializable(DIFFICULTY, this.getDifficulty());
        bundle.putSerializable(STATUS, this.gameStatus);
        bundle.putInt(DELAYKWD, getDelay());
        bundle.putInt(DELAYDECAYKWD, this.getDelaydecay());
        bundle.putSerializable(BUTTONSTATE, this.mButtonState);
        bundle.putInt(CURRENTROUND, this.getCurrentRound());
        bundle.putInt(HIGHSCORE, this.getHighScore());
        bundle.putInt(SEQUENCEPOS,this.getSequencePosition());
        Log.d("SAVESTATE", String.valueOf(this.getSequencePosition()));
        state.putBundle(GAMEBOARD_SAVED_STATE, bundle);

    }
    public int getDelaydecay() {
        return delaydecay;
    }
    public void setDelaydecay(int delaydecay) {
        this.delaydecay = delaydecay;
    }
    public int getCurrentRound() {
        return mCurrentRound;
    }
    public void setCurrentRound(int mCurrentRound) {
        this.mCurrentRound = mCurrentRound;
    }
    public int getHighScore() {
        return mHighScore;
    }
    public void setHighScore(int mHighScore) {
        this.mHighScore = mHighScore;
    }
    public int getSequencePosition() {
        return mSequencePosition;
    }
    public void setSequencePosition(int mSequencePosition) {
        this.mSequencePosition = mSequencePosition;
    }
    public int getDelay() {
        return this.delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    private Listener mListener;
    private ButtonHandler mButtonHandler;
    private buttonState mButtonState;
    public difficultyType getDifficulty() {
        return mDifficulty;
    }
    public void setDifficulty(difficultyType mDifficulty) {
        this.mDifficulty = mDifficulty;
    }

    /***
     *
     * @param roundSequence
     *
     * setter for the sequence of colors.
     */
    public void setRoundSequence(List<Integer> roundSequence){
        this.mRoundSequence =  roundSequence;
    }

    /***
     * allows talk back to the calling activity
     */
    public interface Listener{
        void buttonLit(int buttonID);
        void buttonNormal(int buttonID);
        void setButtonState(int buttonID,boolean active);
        void sendTextToast(String msg);
        void setHighScore(int score);
        void setMainButtonText(String txt);
    }

    //add random color to sequence
    public void addColorToSequence(){
        int idx  = (int) Math.floor(Math.random() * mColorIDs.length);
        mRoundSequence.add(mColorIDs[idx]);
    }


    public boolean isNextChoiceCorrect(int buttonSelected){
        return (mRoundSequence.get(mSequencePosition) == buttonSelected);
    }


    /***
     *  add listener to model so that model can talk back to Controller.
     * @param listener
     */
    public void addListener(Listener listener) {

        this.mListener = listener;
    }

    public void setButtonHandler() {
        this.mButtonHandler = new ButtonHandler();
        setDelay(this.getDifficulty());
    }

    /***
     * a new Game is Started
     */
    public void startGame(){
        mButtonHandler = new ButtonHandler();
        mRoundSequence.clear();
        mCurrentRound = 1;
        setDelay(this.getDifficulty());
        delay = DELAY;
        addRound();

    }

    /***
     * Add another color to round and then play that round for user.
     */
    private void addRound() {
        mSequencePosition = 0;
        this.addColorToSequence();
        gameStatus = status.SHOWSEQUENCE;
        mButtonState = buttonState.RELEASED;
        mListener.sendTextToast(SIMON_TURN_TOST);
        mListener.setMainButtonText(String.format(ROUND_TEXT, mCurrentRound));
        updateDelayed();
    }

    /***
     * this is how I handle the asynchronous lights on
     * simons turn.
     */
    class ButtonHandler extends Handler{
        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWBUTTON:
                  Game.this.update();
                    break;
                case WAITSHOW:

                break;


            }
        }

        /***
         *
         * @param delayMillis
         */
        public void sleep(long delayMillis) {
            this.removeMessages(SHOWBUTTON);
           	sendMessageDelayed(obtainMessage(SHOWBUTTON), delay);
        }
    }

    /***
     * allows update to game with a longer delay.
     */
public void updateDelayed(){
    mButtonHandler.postDelayed(new Runnable() {
        public void run() {
            Game.this.update();
        }
    }, LONG_DELAY ); //LONG DELAY Wasn't enough
}
    /***
     * play sequence of colors.  then wait for user response.
     */

    /***
     * controls whether to display the light sequence or wait for user input.
     */
    public void update() {

          switch (gameStatus) {
              case SHOWSEQUENCE:
                  switch (mButtonState) {
                      case PRESSED:
                          mListener.buttonNormal(mRoundSequence.get(mSequencePosition));
                          mSequencePosition ++;
                          mButtonState = buttonState.RELEASED;
                          break;
                      case RELEASED:
                          mListener.buttonLit(mRoundSequence.get(mSequencePosition));
                          mButtonState = buttonState.PRESSED;
                          break;
                  }

                  if(mSequencePosition>=mRoundSequence.size()){
                      gameStatus = status.LISTENING;
                      mSequencePosition =0;


                  }

                  mButtonHandler.sleep(LONG_DELAY);
                  break;
              case LISTENING:
                  mButtonHandler.postDelayed(new Runnable() {
                      public void run() {
                          mListener.sendTextToast(USER_TURN_TOAST);
                          mListener.setMainButtonText(String.format(ROUND_TEXT,mCurrentRound));
                      }
                  }, delay);



          }




    }

    /***
     *
     * @param btnID
     *
     * When user clicks  a  color, we must make sure color clicked is the correct color in the expected sequence.
     */
    public void checkColorClicked(int btnID) {

       if(btnID == mRoundSequence.get(mSequencePosition)) {
          mSequencePosition++;
           if(mSequencePosition == mRoundSequence.size()){
               if(mCurrentRound>mHighScore) mHighScore = mCurrentRound;
               mCurrentRound++;
               mListener.setHighScore(mHighScore);
               addRound();
               delay =delay>DELAY_MIN?delay - delaydecay:delay; //simon's buttons get faster each round;
           }
       } else {
            endGame();
        }
    }

    /***
     * Give the user  a chance to restart the game.
     */
    private void endGame() {
        mListener.sendTextToast(String.format(YOU_LOST,mCurrentRound));
        mCurrentRound=1;
        this.gameStatus = status.LOST;
        mListener.setMainButtonText(RESTART_TEXT);

    }

    /***
     *
     * @param difficulty
     *
     * This is a method that allows me to make the difficulty level by varying the delay as  the rounds progress.
     */
private void setDelay(difficultyType difficulty) {
  switch(difficulty) {
      case EASY:
          this.delaydecay = (int)Math.floor(DELAY_DECAY * .8);
          //delay = DELAY - 100;
          break;
      case MEDIUM:
          this.delaydecay = DELAY_DECAY;
          //delay = DELAY ;
          break;
      case HARD:
          this.delaydecay = (int)Math.ceil(DELAY_DECAY * 1.20);
          //delay = DELAY -100;
          break;
  }
}


}
