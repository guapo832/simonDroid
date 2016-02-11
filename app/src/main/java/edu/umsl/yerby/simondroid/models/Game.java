package edu.umsl.yerby.simondroid.models;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyerby on 2/6/2016.
 */
public class Game {


    public enum status {
    SHOWSEQUENCE,
    LISTENING,
        LOST
}
   public enum buttonState {
        PRESSED,
        RELEASED
    }

    
    public status gameStatus;
    private int[] mColorIDs;
    private List<Integer> mRoundSequence;
    private int mSequencePosition,mCurrentRound,mHighScore;
    private Listener mListener;
    private ButtonHandler mButtonHandler;
    private buttonState mButtonState;
    private static final int SHOWBUTTON = 0;
    private static final int WAITSHOW = 1;
    private static final String USER_TURN_TOAST  = "Your turn";
    private static final String SIMON_TURN_TOST = "Simon Says...";
    private static final String YOU_LOST = "You lost in Round %d";
    private static final String ROUND_TEXT = "Round\n%d";
    private static final String RESTART_TEXT = "Click to Restart";
    private static final int DELAY=250;
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
    }

    //hide from public
    private Game(){}

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

    /***
     * a new Game is Started
     */
    public void startGame(){
        mButtonHandler = new ButtonHandler();
        mRoundSequence.clear();
        mCurrentRound = 1;
        addRound();
    }

    private void addRound() {
        mSequencePosition = 0;
      //  for(int i =0 ; i<mColorIDs.length; i++){
        //    mListener.setButtonState(mColorIDs[i],false);
        //}
        this.addColorToSequence();
        gameStatus = status.SHOWSEQUENCE;
        mButtonState = buttonState.RELEASED;
        mListener.sendTextToast(SIMON_TURN_TOST);
        mListener.setMainButtonText(String.format(ROUND_TEXT,mCurrentRound));
        mButtonHandler.postDelayed(new Runnable() {
            public void run() {
                Game.this.update();
            }
        }, LONG_DELAY ); //LONG DELAY Wasn't enough
    }

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

        public void sleep(long delayMillis) {
            this.removeMessages(SHOWBUTTON);
           	sendMessageDelayed(obtainMessage(SHOWBUTTON), DELAY);
        }
    }


    /***
     * play sequence of colors.  then wait for user response.
     */
    private void update() {

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
                  }, DELAY);



          }




    }
    public void checkColorClicked(int btnID) {

       if(btnID == mRoundSequence.get(mSequencePosition)) {
          mSequencePosition++;
           if(mSequencePosition == mRoundSequence.size()){
               if(mCurrentRound>mHighScore) mHighScore = mCurrentRound;
               mCurrentRound++;
               mListener.setHighScore(mHighScore);
               addRound();
           }
       } else {
            endGame();
        }
    }

    private void endGame() {
        mListener.sendTextToast(String.format(YOU_LOST,mCurrentRound));
        mCurrentRound=1;
        this.gameStatus = status.LOST;
        mListener.setMainButtonText(RESTART_TEXT);

    }




}
