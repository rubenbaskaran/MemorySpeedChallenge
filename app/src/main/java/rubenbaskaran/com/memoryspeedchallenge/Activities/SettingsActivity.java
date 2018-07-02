package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import rubenbaskaran.com.memoryspeedchallenge.R;

public class SettingsActivity extends Activity
{
    Switch soundSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        soundSwitch = findViewById(R.id.soundSwitch);
        SetSoundSwitchValue();
        soundSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("sound", isChecked).apply();
        }
    };

    private void SetSoundSwitchValue()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
        Boolean soundOn = sharedPreferences.getBoolean("sound", true);
        soundSwitch.setChecked(soundOn);
    }
}