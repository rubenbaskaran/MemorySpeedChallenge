package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.GameAlgorithm;
import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.LevelingSystem;
import rubenbaskaran.com.memoryspeedchallenge.R;

public class GameActivity extends Activity
{
    //region Properties
    View root;
    ArrayList<Integer> route;
    Button startGameBtn;
    int currentLevel, routeLength, generatedRouteLength, startPosition, score, counter, gameLength = 30;
    String currentRank;
    long intervalTime;
    TextView scoreTextView, counterTextView, levelTextView;
    Boolean StopAllThreads = false;
    //endregion

    //region Constructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_game, null);
        setContentView(root);
        startGameBtn = findViewById(R.id.startGameBtn);
        EnableGridButtons(false);

        scoreTextView = findViewById(R.id.scoreTextView);
        levelTextView = findViewById(R.id.levelTextView);

        SetLevelAndRank();
        SetScore();

        counterTextView = findViewById(R.id.counterTextView);
        String gameLengthToShow = currentLevel == 11 ? "60 sec" : String.valueOf(gameLength) + " sec";
        counterTextView.setText(gameLengthToShow);
    }
    //endregion

    //region Game
    public void HandleUserInput(View view)
    {
        if (Integer.parseInt(view.getTag().toString()) == route.get(0))
        {
            route.remove(0);
            if (!(Integer.parseInt(view.getTag().toString()) == startPosition))
            {
                view.setBackground(getDrawable(R.drawable.grid_button_right_answer));
            }
            view.setEnabled(false);
            if (route.isEmpty())
            {
                EnableGridButtons(false);
                ShowRightAnswerIcon(true);
                startGameBtn.setEnabled(true);
                score += 10 * generatedRouteLength;
                String outputMessage = currentLevel == 11 ? "Points: " + String.valueOf(score) : "Points: " + String.valueOf(score) + "/" + LevelingSystem.GetMinimumScore(currentLevel);
                scoreTextView.setText(outputMessage);
                StartNewRoundOrShowResults();
            }
        }
        else
        {
            EnableGridButtons(false);
            ShowRightAnswerIcon(false);
            startGameBtn.setEnabled(true);
            StartNewRoundOrShowResults();
        }
    }

    private void StartNewRoundOrShowResults()
    {
        if (!StopAllThreads)
        {
            StartNewRound();
        }
        else
        {
            if (score >= LevelingSystem.GetMinimumScore(currentLevel))
            {
                HighscoreActivity.SetHighscoreAndHighscoreLevel(score, currentLevel, this);
                IncrementCurrentLevel();
                String dialogMessage;
                String positiveButtonText;

                if (currentLevel == 10)
                {
                    dialogMessage = "Congratulations! \nYou've completed the whole game \nTry breaking the world record in 60 seconds";
                    positiveButtonText = "Play bonus level";
                }
                else if (currentLevel == 11)
                {
                    dialogMessage = "You scored " + score + " Points";
                    positiveButtonText = "Play again";
                }
                else
                {
                    dialogMessage = "Congratulations! \nLevel " + currentLevel + " Completed";
                    positiveButtonText = "Next level";
                }

                ShowDialog(dialogMessage, positiveButtonText);
            }
            else
            {
                ShowDialog("Sorry! You didn't get enough point!", "Retry");
            }
        }
    }
    //endregion

    //region AsyncTasks
    private class AsyncShowRightAnswerIcon extends AsyncTask<String, String, Void>
    {
        @Override
        protected Void doInBackground(String... strings)
        {
            if (strings[0].equals("true"))
            {
                onProgressUpdate(String.valueOf(R.drawable.correct_answer));
                PlaySound(R.raw.correct);
                WaitOneSecond();
                onProgressUpdate(String.valueOf(android.R.color.transparent));
            }
            else
            {
                onProgressUpdate(String.valueOf(R.drawable.wrong_answer));
                PlaySound(R.raw.wrong);
                WaitOneSecond();
                onProgressUpdate(String.valueOf(android.R.color.transparent));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final String... values)
        {
            super.onProgressUpdate(values);
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ImageView resultImageView = findViewById(R.id.resultImageView);
                    resultImageView.setImageResource(Integer.parseInt(values[0]));
                }
            });
        }
    }

    private class AsyncCounter extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            StopAllThreads = false;
            counter = currentLevel == 11 ? 60 : gameLength;
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            publishProgress();
            while (counter > 0 && !StopAllThreads)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCounter");
                }
                counter--;
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            counterTextView.setText(String.valueOf(counter) + " sec");
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if (!StopAllThreads)
            {
                StopAllThreads = true;
                ResetGridButtonsColor();
                EnableGridButtons(false);
                StartNewRoundOrShowResults();
            }
        }
    }

    private class AsyncCreateRoute extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            PrepareInterface();
            SetDifficulty(currentLevel);
            GenerateRoute(routeLength);
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            int currentPosition = 0;

            for (int item : route)
            {
                if (StopAllThreads)
                {
                    break;
                }
                int direction = GetDirection(item, currentPosition);
                Object[] output = {true, item, direction};
                publishProgress(output);
                try
                {
                    Thread.sleep(intervalTime);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCreateRoute");
                }
                if (item != startPosition)
                {
                    output[0] = false;
                    publishProgress(output);
                }

                currentPosition = item;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            boolean showDirectionImage = (boolean) values[0];
            int currentPosition = (int) values[1];
            int directionImageId = (int) values[2];

            Button btn = root.findViewWithTag(String.valueOf(currentPosition));
            if (currentPosition == startPosition)
            {
                btn.setBackground(getDrawable(R.drawable.start_position));
            }
            else if (showDirectionImage)
            {
                btn.setBackground(getDrawable(directionImageId));
            }
            else
            {
                btn.setBackground(getDrawable(R.drawable.grid_button_background));
            }
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if (!StopAllThreads)
            {
                EnableGridButtons(true);
                View _startPosition = root.findViewWithTag(String.valueOf(startPosition));
                HandleUserInput(_startPosition);
            }
        }
    }

    private void WaitOneSecond()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            Log.e("Error", "Error in WaitOneSecond()");
        }
    }
    //endregion

    //region Userinterface
    private void ShowRightAnswerIcon(Boolean rightAnswer)
    {
        AsyncShowRightAnswerIcon asyncShowRightAnswerIcon = new AsyncShowRightAnswerIcon();
        asyncShowRightAnswerIcon.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(rightAnswer));
    }

    private void ShowDialog(String message, String buttonText)
    {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        WaitOneSecond();
                        StartNewGame(null);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                    }
                })
                .show();
    }

    private void SetScore()
    {
        score = 0;
        String outputMessage = currentLevel == 11 ? "Points: " + String.valueOf(score) : "Points: " + String.valueOf(score) + "/" + LevelingSystem.GetMinimumScore(currentLevel);
        scoreTextView.setText(outputMessage);
    }

    private void SetLevelAndRank()
    {
        currentLevel = LevelingSystem.GetCurrentLevel(this);
        currentRank = LevelingSystem.GetCurrentRank(currentLevel);
        String outputMessage = currentLevel == 11 ? "Level: 10/10" : "Level: " + String.valueOf(currentLevel) + "/10";
        levelTextView.setText(outputMessage);
    }

    private void EnableGridButtons(boolean input)
    {
        root.findViewWithTag("1").setEnabled(input);
        root.findViewWithTag("2").setEnabled(input);
        root.findViewWithTag("3").setEnabled(input);
        root.findViewWithTag("4").setEnabled(input);
        root.findViewWithTag("5").setEnabled(input);
        root.findViewWithTag("6").setEnabled(input);
        root.findViewWithTag("7").setEnabled(input);
        root.findViewWithTag("8").setEnabled(input);
        root.findViewWithTag("9").setEnabled(input);
        root.findViewWithTag("10").setEnabled(input);
        root.findViewWithTag("11").setEnabled(input);
        root.findViewWithTag("12").setEnabled(input);
        root.findViewWithTag("13").setEnabled(input);
        root.findViewWithTag("14").setEnabled(input);
        root.findViewWithTag("15").setEnabled(input);
        root.findViewWithTag("16").setEnabled(input);
        root.findViewWithTag("17").setEnabled(input);
        root.findViewWithTag("18").setEnabled(input);
        root.findViewWithTag("19").setEnabled(input);
        root.findViewWithTag("20").setEnabled(input);
        root.findViewWithTag("21").setEnabled(input);
        root.findViewWithTag("22").setEnabled(input);
        root.findViewWithTag("23").setEnabled(input);
        root.findViewWithTag("24").setEnabled(input);
        root.findViewWithTag("25").setEnabled(input);
    }

    private void ResetGridButtonsColor()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                root.findViewWithTag("1").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("2").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("3").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("4").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("5").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("6").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("7").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("8").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("9").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("10").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("11").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("12").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("13").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("14").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("15").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("16").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("17").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("18").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("19").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("20").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("21").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("22").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("23").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("24").setBackground(getDrawable(R.drawable.grid_button_background));
                root.findViewWithTag("25").setBackground(getDrawable(R.drawable.grid_button_background));
            }
        });
    }

    private void PrepareInterface()
    {
        ResetGridButtonsColor();
        startGameBtn.setEnabled(false);
        EnableGridButtons(false);
    }
    //endregion

    //region Helpers
    private int GetDirection(int newPosition, int previousPosition)
    {
        String direction;

        if (newPosition == previousPosition - 1)
        {
            direction = "left";
        }
        else if (newPosition == previousPosition + 1)
        {
            direction = "right";
        }
        else if (newPosition < previousPosition)
        {
            direction = "up";
        }
        else
        {
            direction = "down";
        }

        return getApplicationContext().getResources().getIdentifier(direction, "drawable", getApplicationContext().getPackageName());
    }

    public void StartNewGame(View view)
    {
        StopAllThreads = false;
        SetLevelAndRank();
        SetScore();
        StartCounter();
        StartNewRound();
    }

    private void IncrementCurrentLevel()
    {
        if (currentLevel < 11)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
            sharedPreferences.edit().putInt("currentlevel", currentLevel + 1).apply();
        }
    }

    private void StartCounter()
    {
        AsyncCounter asyncCounter = new AsyncCounter();
        asyncCounter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void StartNewRound()
    {
        AsyncCreateRoute asyncCreateRoute = new AsyncCreateRoute();
        asyncCreateRoute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void SetDifficulty(int currentLevel)
    {
        routeLength = LevelingSystem.GetRouteLength(currentLevel);
        intervalTime = LevelingSystem.GetIntervalTime(currentLevel);
    }

    private void GenerateRoute(int routeLength)
    {
        GameAlgorithm gameAlgorithm = new GameAlgorithm();
        route = gameAlgorithm.GenerateRoute(routeLength);
        generatedRouteLength = route.size() - 1;
        startPosition = route.get(0);
    }
    //endregion

    //region Lifecycle
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        StopAllThreads = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        StopAllThreads = false;
    }
    //endregion

    //region Sounds
    private void PlaySound(int soundFileId)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
        Boolean soundOn = sharedPreferences.getBoolean("sound", true);

        if (soundOn)
        {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), soundFileId);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                }
            });
        }
    }
    //endregion
}