package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.LevelingSystem;
import rubenbaskaran.com.memoryspeedchallenge.R;

import static rubenbaskaran.com.memoryspeedchallenge.Activities.WebApiManager.GetAllHighscores;
import static rubenbaskaran.com.memoryspeedchallenge.Activities.WebApiManager.SaveNewHighscore;

public class HomeActivity extends Activity
{
    TextView currentLevelTextView, currentRankTextView;
    int currentLevel;
    String currentRank;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currentLevelTextView = findViewById(R.id.currentLevelTextView);
        currentRankTextView = findViewById(R.id.currentRankTextView);
        SetLevelAndRank();

        GetAllHighscores();
        SaveNewHighscore("testfromandroid", 201);
    }

    public void OpenGameActivity(View view)
    {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SetLevelAndRank();
    }

    private void SetLevelAndRank()
    {
        currentLevel = LevelingSystem.GetCurrentLevel(this);
        currentRank = LevelingSystem.GetCurrentRank(currentLevel);
        String outputMessage;

        if (currentLevel == 11)
        {
            outputMessage = "Level: Bonus level";
        }
        else if (currentLevel == 10)
        {
            outputMessage = "Level: 10/10 [Complete level 10 to unlock bonus level]";
        }
        else
        {
            outputMessage = "Level: " + String.valueOf(currentLevel) + "/10";
        }

        currentLevelTextView.setText(outputMessage);
        currentRankTextView.setText("Rank: " + currentRank);
    }

    public void OpenHighscoreActivity(View view)
    {
        Intent i = new Intent(this, HighscoreActivity.class);
        startActivity(i);
    }

    public void OpenSettingsActivity(View view)
    {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}
