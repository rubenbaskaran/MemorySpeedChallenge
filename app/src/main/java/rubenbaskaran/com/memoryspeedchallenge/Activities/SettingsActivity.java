package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.LevelingSystem;
import rubenbaskaran.com.memoryspeedchallenge.R;

public class SettingsActivity extends Activity
{
    Switch soundSwitch;
    Spinner levelSpinner;
    private boolean spinnerAutoCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        soundSwitch = findViewById(R.id.soundSwitch);
        SetSoundSwitchValue();
        soundSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        levelSpinner = findViewById(R.id.levelSpinner);
        SetupSpinner();
        levelSpinner.setOnItemSelectedListener(SpinnerOnClick);
    }

    AdapterView.OnItemSelectedListener SpinnerOnClick = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            if (spinnerAutoCalled)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", MODE_PRIVATE);
                sharedPreferences.edit().putInt("currentlevel", position + 1).apply();
            }
            else
            {
                spinnerAutoCalled = true;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };

    private void SetupSpinner()
    {
        int currentLevel = LevelingSystem.GetCurrentLevel(this);

        ArrayList<String> listOfLevels = new ArrayList<>();
        for (int i = 1; i <= currentLevel; i++)
        {
            listOfLevels.add("Level " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(adapter);
        levelSpinner.setSelection(listOfLevels.size() - 1);
    }

    //region Sound Settings
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
    //endregion
}