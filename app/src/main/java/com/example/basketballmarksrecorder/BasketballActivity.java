package com.example.basketballmarksrecorder;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class BasketballActivity extends AppCompatActivity implements EditNameDialog.EditNameDialogListener {
    TextView team_a, team_b;
    ImageView IVEditName;
    //provide -1 incase input error
    String [] marks = {"-1","1","2","3"};
    ArrayAdapter<String> adapterMarks;
    AutoCompleteTextView autoCompleteTextView_a, autoCompleteTextView_b;
    int teamA = 0;
    int teamB = 0;

    EditText mEditTextInput;
    TextView mTextViewCountDown;
    Button mButtonSet;
    Button mButtonStartPause;
    Button mButtonReset;
    ImageView IVShare;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    boolean clicked = false;
    String sent ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball);

        team_a = findViewById(R.id.name_team_a);
        team_b = findViewById(R.id.name_team_b);


        IVShare = findViewById(R.id.IVShare);
        IVShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        autoCompleteTextView_a = findViewById(R.id.autocompleteTextView_a);
        adapterMarks= new ArrayAdapter<String>(this,R.layout.list_item, marks);
        autoCompleteTextView_a.setAdapter(adapterMarks);
        autoCompleteTextView_a.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mark = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(BasketballActivity.this, "Marks: "+ mark, Toast.LENGTH_SHORT).show();
                teamA += Integer.parseInt(mark);
                TextView textView = findViewById(R.id.score_board_team_A);
                textView.setText("" +teamA);
            }
        });
        autoCompleteTextView_b = findViewById(R.id.autocompleteTextView_b);
        autoCompleteTextView_b.setAdapter(adapterMarks);
        autoCompleteTextView_b.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mark = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(BasketballActivity.this, "Marks: "+ mark, Toast.LENGTH_SHORT).show();
                teamB += Integer.parseInt(mark);
                TextView textView = findViewById(R.id.score_board_team_B);
                textView.setText("" +teamB);
            }
        });


        IVEditName = (ImageView) findViewById(R.id.IVEditName);
        IVEditName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDialog();


            }
        });
        IVShare = findViewById(R.id.IVShare);
        IVShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView_a = findViewById(R.id.score_board_team_A);
                TextView textView_b = findViewById(R.id.score_board_team_B);
                sent = team_a.getText().toString() + " "  + " vs " + team_b.getText().toString() + "\n " + textView_a.getText().toString() + " - "+textView_b.getText().toString();
                shareTextOnly(sent);
            }
        });

        mEditTextInput = findViewById(R.id.ETInput);
        mTextViewCountDown = findViewById(R.id.TVCountdown);

        mButtonSet = findViewById(R.id.BtnSet);
        mButtonStartPause = (Button)findViewById(R.id.BtnStartPause);
        mButtonReset = findViewById(R.id.BtnReset);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(BasketballActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(BasketballActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                mEditTextInput.setVisibility(View.VISIBLE);
                mButtonSet.setVisibility(View.VISIBLE);
                resetTimer();
            }
        });


    }
    public void openDialog(){
        EditNameDialog editNameDialogDialog = new EditNameDialog();
        editNameDialogDialog.show(getSupportFragmentManager(),"Edit Name Dialog");

    }
    private void shareTextOnly(String titlee) {
        String sharebody = titlee;

        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND
        Intent intentt = new Intent(Intent.ACTION_SEND);

        // setting type of data shared as text
        intentt.setType("text/plain");
        intentt.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // Adding the text to share using putExtra
        intentt.putExtra(Intent.EXTRA_TEXT, sharebody);
        startActivity(Intent.createChooser(intentt, "Share Via"));
    }

    @Override
    public void applyTexts(String teamNameA, String teamNameB) {
        team_a.setText(teamNameA);
        team_b.setText(teamNameB);

    }
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            if(mTimeLeftInMillis == mStartTimeInMillis){
                mEditTextInput.setVisibility(View.VISIBLE);
                mButtonSet.setVisibility(View.VISIBLE);
            }else{
                mEditTextInput.setVisibility(View.INVISIBLE);
                mButtonSet.setVisibility(View.INVISIBLE);
            }


            mButtonStartPause.setText("Start");


            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

}
