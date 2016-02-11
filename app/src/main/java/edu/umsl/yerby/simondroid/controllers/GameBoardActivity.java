package edu.umsl.yerby.simondroid.controllers;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.umsl.yerby.simondroid.R;
import edu.umsl.yerby.simondroid.models.Game;

public class GameBoardActivity extends AppCompatActivity implements Game.Listener, View.OnTouchListener{

    private static final String GAME_HIGH_SCORE = "edu.umsl.yerby.simondroid.gameboard.highscore";
    private static final int BLUE = 1;
    private static final int RED = 4;
    private static final int YELLOW = 2;
    private static final int GREEN = 3;
    private SoundPool soundPool;
    private int speakerStreamID;
    private int[] soundIDs = new int[5];
    Button greenBtn,yellowBtn,redBtn,blueBtn;
private MediaPlayer mPlayer;
    Game mGameModel;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(4);
soundPool = builder.build();

        soundIDs[BLUE] = soundPool.load(this,R.raw.blue_long,1);
        soundIDs[GREEN] = soundPool.load(this,R.raw.green_long,1);
        soundIDs[YELLOW] = soundPool.load(this,R.raw.yellow_long,1);
        soundIDs[RED] = soundPool.load(this, R.raw.red_long, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        mGameModel = new Game(new int[]{R.id.buttonblue,R.id.buttongreen,R.id.buttonred,R.id.buttonyellow});
        mGameModel.addListener(this);
        mGameModel.startGame();

        blueBtn =(Button) findViewById(R.id.buttonblue);
        greenBtn =(Button) findViewById(R.id.buttongreen);
        yellowBtn =(Button) findViewById(R.id.buttonyellow);
        redBtn =(Button) findViewById(R.id.buttonred);

        blueBtn.setOnTouchListener(this);
        redBtn.setOnTouchListener(this);
        greenBtn.setOnTouchListener(this);
        yellowBtn.setOnTouchListener(this);




    }


    @Override
    public void buttonLit(int buttonID) {
        if(speakerStreamID!=0) soundPool.stop(speakerStreamID);
        Button btn = (Button)findViewById(buttonID);
        switch(buttonID){
            case R.id.buttonred:
                btn.setBackgroundResource(R.drawable.buttonredpressed);
                break;
            case R.id.buttonblue:
                btn.setBackgroundResource(R.drawable.buttonbluepressed);
                break;
            case R.id.buttongreen:
                btn.setBackgroundResource(R.drawable.buttongreenpressed);
                break;
            case R.id.buttonyellow:
                btn.setBackgroundResource(R.drawable.buttonyellowpressed);
                break;
        }

        playStream(buttonID);

    }

    @Override
    public void buttonNormal(int buttonID) {
        Button btn = (Button)findViewById(buttonID);
        switch(buttonID){
            case R.id.buttonred:
                btn.setBackgroundResource(R.drawable.buttonred);
                break;
            case R.id.buttonblue:
                btn.setBackgroundResource(R.drawable.buttonblue);
                break;
            case R.id.buttongreen:
                btn.setBackgroundResource(R.drawable.buttongreen);
                break;
            case R.id.buttonyellow:
                btn.setBackgroundResource(R.drawable.buttonyellow);
                break;
        }
        if(speakerStreamID!=0){
            soundPool.stop(speakerStreamID);
            speakerStreamID = 0;
        }

    }

    public void mainSimonButtonClicked(View view){
        if(mGameModel.gameStatus == Game.status.LOST){
            mGameModel.startGame();
        }
    }

    @Override
    public void setButtonState(int buttonID,boolean active) {
        Button btn = (Button)findViewById(buttonID);
        btn.setActivated(active);
    }

    @Override
    public void setMainButtonText(String msg){
        Button centerBtn = (Button)findViewById(R.id.center_button);
        centerBtn.setText(msg);
    }

    @Override
    public void sendTextToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setHighScore(int score) {
        Intent data = new Intent();
        data.putExtra(GAME_HIGH_SCORE,score);
        setResult(RESULT_OK, data);
    }

    public static int getHighScore(Intent data) {
        return data.getIntExtra(GAME_HIGH_SCORE,0);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int buttonId = v.getId();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                buttonLit(buttonId);
                break;
            case MotionEvent.ACTION_UP:
                buttonNormal(buttonId);
                Game.status status = mGameModel.gameStatus;
                if(status == Game.status.LISTENING){
                    //   generateTone(440, 9000);
                    mGameModel.checkColorClicked(buttonId);
                }
                break;
        }
        return true;
    }

    private void playStream(int buttonId) {
        int colorId = GREEN;
        switch(buttonId){
            case R.id.buttonblue:
                colorId = BLUE;
                break;
            case R.id.buttongreen:
                colorId = GREEN;
                break;
            case R.id.buttonred:
                colorId = RED;
                break;
            case R.id.buttonyellow:
                colorId = YELLOW;
                break;
        }
        speakerStreamID = soundPool.play(colorId,1.0f,1.0f,0,0,1.0f);
    }
}
