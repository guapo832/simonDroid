package edu.umsl.yerby.simondroid.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.umsl.yerby.simondroid.R;
import edu.umsl.yerby.simondroid.models.Game;

public class GameBoardActivity extends AppCompatActivity implements Game.Listener {
    Button greenBtn,yellowBtn,redBtn,blueBtn;
    Game mGameModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        //greenBtn = (Button)findViewById(R.id.buttongreen);
        //yellowBtn = (Button)findViewById(R.id.buttonyellow);
        //redBtn = (Button)findViewById(R.id.buttonred);
        //blueBtn = (Button)findViewById(R.id.buttonblue);

        mGameModel = new Game(new int[]{R.id.buttonblue,R.id.buttongreen,R.id.buttonred,R.id.buttonyellow});
        mGameModel.addListener(this);
        mGameModel.startGame();
    }


    @Override
    public void buttonLit(int buttonID) {
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
    }

    public void colorBtn_Click(View view){
        int btnID = view.getId();
        Game.status status = mGameModel.gameStatus;
        if(status == Game.status.LISTENING){
            mGameModel.checkColorClicked(btnID);
        }
    }

    @Override
    public void setButtonState(int buttonID,boolean active) {
        Button btn = (Button)findViewById(buttonID);
        btn.setActivated(active);
    }

    @Override
    public void sendTextToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
