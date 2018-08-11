package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.LevelingSystem;
import rubenbaskaran.com.memoryspeedchallenge.R;

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
        SaveNewHighscore();
    }

    private void GetAllHighscores()
    {
        WebApiManager.GetAllHighscoresAsync getAllHighscoresAsync = new WebApiManager.GetAllHighscoresAsync();

        try
        {
            String allHighscores = getAllHighscoresAsync.execute().get();
            PrintReceivedMessages(allHighscores);
        }
        catch (Exception e)
        {
            Log.e("Error", "Couldn't get all highscores. Error message: " + e);
        }

    }

    private void PrintReceivedMessages(String receivedMessages)
    {
        try
        {
            JSONObject json = new JSONObject(receivedMessages);
            JSONArray info = json.getJSONArray("highscores");

            for (int i = 0; i < info.length(); i++)
            {
                JSONObject jsonObject = info.getJSONObject(i);
                String username = jsonObject.getString("username");
                String score = jsonObject.getString("score");
                Log.e("Highscore", "Username: " + username + ". Score: " + score);
            }
        }
        catch (Exception e)
        {
            Log.e("Error", "Couldn't convert string to json. Error message: " + e);
        }
    }

    private void SaveNewHighscore()
    {
        WebApiManager.SaveNewHighscoreAsync getAllHighscoresAsync = new WebApiManager.SaveNewHighscoreAsync();
        getAllHighscoresAsync.execute();
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
