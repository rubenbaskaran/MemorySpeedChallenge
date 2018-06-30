package rubenbaskaran.com.memoryspeedchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        currentLevel = LevelingSystem.GetCurrentLevel(this);
        int levelToDisplay = (currentLevel == 26) ? 25 : currentLevel;
        currentRank = LevelingSystem.GetCurrentRank(currentLevel);

        currentLevelTextView = findViewById(R.id.currentLevelTextView);
        currentLevelTextView.setText("Level: " + String.valueOf(levelToDisplay));

        currentRankTextView = findViewById(R.id.currentRankTextView);
        currentRankTextView.setText("Rank: " + currentRank);
    }

    public void OpenGameActivity(View view)
    {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("currentLevel", currentLevel);
        i.putExtra("currentRank", currentRank);
        startActivity(i);
    }
}
