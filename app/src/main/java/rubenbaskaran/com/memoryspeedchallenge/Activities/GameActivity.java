package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    int currentLevel, routeLength, startPosition, score, counter, gameLength = 30;
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
        counterTextView = findViewById(R.id.counterTextView);
        counterTextView.setText(String.valueOf(gameLength) + " sec");
        levelTextView = findViewById(R.id.levelTextView);

        SetLevelAndRank();
        SetScore();
    }
    //endregion

    //region Game
    public void HandleUserInput(View view)
    {
        if (Integer.parseInt(view.getTag().toString()) == route.get(0))
        {
            route.remove(0);
            view.setBackground(getDrawable(R.drawable.grid_button_right_answer));
            view.setEnabled(false);
            if (route.isEmpty())
            {
                ShowRightAnswerIcon(true);
                EnableGridButtons(false);
                startGameBtn.setEnabled(true);
                score += 100;
                scoreTextView.setText(String.valueOf(score) + "p (" + "min. " + LevelingSystem.GetMinimumScore(currentLevel) + "p)");
                StartNewRoundOrShowResults();
            }
        }
        else
        {
            ShowRightAnswerIcon(false);
            EnableGridButtons(false);
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
                IncrementCurrentLevel();
                ShowDialog("Congratulations! Level completed!", "Next level");
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
                WaitOneSecond();
                onProgressUpdate(String.valueOf(android.R.color.transparent));
            }
            else
            {
                onProgressUpdate(String.valueOf(R.drawable.wrong_answer));
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
            counter = gameLength;
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
            for (int item : route)
            {
                if (StopAllThreads)
                {
                    break;
                }
                Object[] output = {true, item};
                publishProgress(output);
                try
                {
                    Thread.sleep(intervalTime);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCreateRoute");
                }
                output[0] = false;
                publishProgress(output);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            Button btn = root.findViewWithTag(String.valueOf(values[1]));

            if (((boolean) values[0]))
            {
                btn.setBackgroundColor(Color.BLACK);
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
        scoreTextView.setText(String.valueOf(score) + "p (" + "min. " + LevelingSystem.GetMinimumScore(currentLevel) + "p)");
    }

    private void SetLevelAndRank()
    {
        currentLevel = LevelingSystem.GetCurrentLevel(this);
        currentRank = LevelingSystem.GetCurrentRank(currentLevel);
        int levelToDisplay = (currentLevel == 26) ? 25 : currentLevel;
        levelTextView.setText("Level: " + String.valueOf(levelToDisplay) + "/25");
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
        SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
        sharedPreferences.edit().putInt("currentlevel", currentLevel + 1).apply();
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
}