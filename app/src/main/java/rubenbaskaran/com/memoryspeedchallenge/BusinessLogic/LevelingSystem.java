package rubenbaskaran.com.memoryspeedchallenge.BusinessLogic;

import android.content.Context;
import android.content.SharedPreferences;

public class LevelingSystem
{
    public static int GetCurrentLevel(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rubenbaskaran.com.memoryspeedchallenge", context.MODE_PRIVATE);
        int currentLevel = sharedPreferences.getInt("currentlevel", 0);

        if (currentLevel == 0)
        {
            sharedPreferences.edit().putInt("currentLevel", currentLevel).apply();
        }

        return currentLevel;
    }

    public static String GetCurrentRank(int currentLevel)
    {
        if (currentLevel >= 0 && currentLevel < 6)
        {
            return "Beginner";
        }
        else if (currentLevel > 5 && currentLevel < 11)
        {
            return "Experienced";
        }
        else if (currentLevel > 10 && currentLevel < 16)
        {
            return "Highly intelligent";
        }
        else if (currentLevel > 15 && currentLevel < 21)
        {
            return "Master";
        }
        else if (currentLevel > 20 && currentLevel <= 25)
        {
            return "Genius";
        }
        else if (currentLevel == 26)
        {
            return "God";
        }
        else
        {
            return "Error";
        }
    }

    public static int GetRouteLength(int currentLevel)
    {
        if (currentLevel >= 0 && currentLevel < 6)
        {
            return 6;
        }
        else if (currentLevel > 5 && currentLevel < 11)
        {
            return 8;
        }
        else if (currentLevel > 10 && currentLevel < 16)
        {
            return 10;
        }
        else if (currentLevel > 15 && currentLevel < 21)
        {
            return 12;
        }
        else if (currentLevel > 20 && currentLevel < 25)
        {
            return 14;
        }
        else
        {
            return 16;
        }
    }

    public static long GetIntervalTime(int currentLevel)
    {
        switch (currentLevel)
        {
            case 0:
                return 1000;
            case 1:
                return 900;
            case 2:
                return 800;
            case 3:
                return 700;
            case 4:
                return 600;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return 500;
            case 11:
                return 450;
            case 12:
                return 400;
            case 13:
                return 350;
            case 14:
                return 300;
            case 15:
                return 250;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                return 200;
            case 21:
            case 22:
            case 23:
            case 24:
                return 150;
            case 25:
            case 26:
                return 100;
            default:
                return 2000;
        }
    }
}