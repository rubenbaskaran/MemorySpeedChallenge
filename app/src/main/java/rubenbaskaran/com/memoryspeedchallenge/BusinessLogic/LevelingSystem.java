package rubenbaskaran.com.memoryspeedchallenge.BusinessLogic;

import android.content.Context;
import android.content.SharedPreferences;

public class LevelingSystem
{
    public static int GetCurrentLevel(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", context.MODE_PRIVATE);
        int currentLevel = sharedPreferences.getInt("currentlevel", 1);

        if (currentLevel == 1)
        {
            sharedPreferences.edit().putInt("currentLevel", currentLevel).apply();
        }

        return currentLevel;
    }

    public static String GetCurrentRank(int currentLevel)
    {
        if (currentLevel > 0 && currentLevel < 4)
        {
            return "Beginner";
        }
        else if (currentLevel > 3 && currentLevel < 7)
        {
            return "Experienced";
        }
        else if (currentLevel > 6 && currentLevel < 11)
        {
            return "Highly intelligent";
        }
        else if (currentLevel == 11)
        {
            return "Genius";
        }
        else
        {
            return "Error";
        }
    }

    public static int GetRouteLength(int currentLevel)
    {
        switch (currentLevel)
        {
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 6;
            case 4:
                return 7;
            case 5:
                return 8;
            case 6:
                return 9;
            case 7:
                return 10;
            case 8:
                return 11;
            case 9:
                return 12;
            case 10:
            case 11:
                return 13;
            default:
                return 4;
        }
    }

    public static long GetIntervalTime(int currentLevel)
    {
        switch (currentLevel)
        {
            case 1:
                return 500;
            case 2:
                return 450;
            case 3:
                return 400;
            case 4:
                return 350;
            case 5:
                return 300;
            case 6:
                return 250;
            case 7:
                return 200;
            case 8:
                return 150;
            case 9:
            case 10:
                return 100;
            case 11:
                return 200;
            default:
                return 500;
        }
    }

    public static int GetMinimumScore(int currentLevel)
    {
        switch (currentLevel)
        {
            case 1:
                return 150;
            case 2:
                return 200;
            case 3:
                return 250;
            case 4:
                return 300;
            case 5:
                return 350;
            case 6:
                return 400;
            case 7:
                return 450;
            case 8:
                return 500;
            case 9:
                return 550;
            case 10:
            case 11:
                return 600;
            default:
                return 150;
        }
    }
}