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
        if (currentLevel > 0 && currentLevel < 6)
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
                return 4;
            case 3:
                return 5;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 7;
            case 9:
                return 8;
            case 10:
                return 8;
            case 11:
                return 9;
            case 12:
                return 9;
            case 13:
                return 10;
            case 14:
                return 10;
            case 15:
                return 11;
            case 16:
                return 11;
            case 17:
                return 12;
            case 18:
                return 12;
            case 29:
                return 13;
            case 20:
                return 13;
            default:
                return 2;
        }
    }

    public static long GetIntervalTime(int currentLevel)
    {
        switch (currentLevel)
        {
            case 1:
                return 500;
            case 2:
                return 480;
            case 3:
                return 460;
            case 4:
                return 440;
            case 5:
                return 420;
            case 6:
                return 400;
            case 7:
                return 380;
            case 8:
                return 360;
            case 9:
                return 340;
            case 10:
                return 320;
            case 11:
                return 300;
            case 12:
                return 280;
            case 13:
                return 260;
            case 14:
                return 240;
            case 15:
                return 220;
            case 16:
                return 200;
            case 17:
                return 180;
            case 18:
                return 160;
            case 19:
                return 140;
            case 20:
                return 120;
            default:
                return 1000;
        }
    }

    public static int GetMinimumScore(int currentLevel)
    {
        switch (currentLevel)
        {
            case 1:
                return 50;
            case 2:
                return 100;
            case 3:
                return 150;
            case 4:
                return 200;
            case 5:
                return 250;
            case 6:
                return 300;
            case 7:
                return 350;
            case 8:
                return 400;
            case 9:
                return 450;
            case 10:
                return 500;
            case 11:
                return 550;
            case 12:
                return 600;
            case 13:
                return 650;
            case 14:
                return 700;
            case 15:
                return 750;
            case 16:
                return 800;
            case 17:
                return 850;
            case 18:
                return 900;
            case 19:
                return 950;
            case 20:
                return 1000;
            default:
                return 0;
        }
    }
}