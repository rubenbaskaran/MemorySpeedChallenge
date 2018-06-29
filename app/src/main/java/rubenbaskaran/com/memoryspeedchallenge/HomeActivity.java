package rubenbaskaran.com.memoryspeedchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // TODO: Get current level from SharedPreference
        // TODO: If level is null then set new SharedPreference "level" to "1"
        // TODO: Set level textview
        // TODO: Calculate rank based on level
        // TODO: Set rank text view
    }

    public void OpenGameActivity(View view)
    {
        Intent i = new Intent(this, GameActivity.class);
        // TODO: i.putExtra(level)
        startActivity(i);
    }
}
