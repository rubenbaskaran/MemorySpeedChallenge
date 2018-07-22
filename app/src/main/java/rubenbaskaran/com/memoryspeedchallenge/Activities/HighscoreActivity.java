package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import rubenbaskaran.com.memoryspeedchallenge.R;

public class HighscoreActivity extends Activity
{
    TextView highscoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        highscoreTextView = findViewById(R.id.highscoreTextView);
        highscoreTextView.setText("Your highscore: " + String.valueOf(GetHighscore(this)) + " (level " + String.valueOf(GetHighscoreLevel(this)) + ")");
    }

    private int GetHighscore(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", context.MODE_PRIVATE);
        return sharedPreferences.getInt("highscore", 0);
    }

    private int GetHighscoreLevel(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", context.MODE_PRIVATE);
        return sharedPreferences.getInt("highscorelevel", 1);
    }

    public static boolean SetHighscoreAndHighscoreLevel(int newScore, int highscoreLevel, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", context.MODE_PRIVATE);
        int oldScore = sharedPreferences.getInt("highscore", 0);

        if (newScore > oldScore)
        {
            sharedPreferences.edit().putInt("highscore", newScore).apply();
            sharedPreferences.edit().putInt("highscorelevel", highscoreLevel).apply();
            return true;
        }

        return false;
    }
}
