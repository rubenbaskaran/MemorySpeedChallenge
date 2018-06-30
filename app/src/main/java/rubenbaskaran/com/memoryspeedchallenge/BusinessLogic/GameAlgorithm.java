package rubenbaskaran.com.memoryspeedchallenge.BusinessLogic;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameAlgorithm
{
    //region Properties
    private HashMap<String, int[]> hashMap = new HashMap<>();
    private int[] cell1 = {2, 6};
    private int[] cell2 = {1, 3, 7};
    private int[] cell3 = {2, 4, 8};
    private int[] cell4 = {3, 5, 9};
    private int[] cell5 = {4, 10};
    private int[] cell6 = {1, 7, 11};
    private int[] cell7 = {2, 6, 8, 12};
    private int[] cell8 = {3, 7, 9, 13};
    private int[] cell9 = {4, 8, 10, 14};
    private int[] cell10 = {5, 9, 15};
    private int[] cell11 = {6, 12, 16};
    private int[] cell12 = {7, 11, 13, 17};
    private int[] cell13 = {8, 12, 14, 18};
    private int[] cell14 = {9, 13, 15, 19};
    private int[] cell15 = {10, 14, 20};
    private int[] cell16 = {11, 17, 21};
    private int[] cell17 = {12, 16, 18, 22};
    private int[] cell18 = {13, 17, 19, 23};
    private int[] cell19 = {14, 18, 20, 24};
    private int[] cell20 = {15, 19, 25};
    private int[] cell21 = {16, 22};
    private int[] cell22 = {17, 21, 23};
    private int[] cell23 = {18, 22, 24};
    private int[] cell24 = {19, 23, 25};
    private int[] cell25 = {20, 24};
    //endregion

    //region Constructors
    public GameAlgorithm()
    {
        hashMap.put("cell1", cell1);
        hashMap.put("cell2", cell2);
        hashMap.put("cell3", cell3);
        hashMap.put("cell4", cell4);
        hashMap.put("cell5", cell5);
        hashMap.put("cell6", cell6);
        hashMap.put("cell7", cell7);
        hashMap.put("cell8", cell8);
        hashMap.put("cell9", cell9);
        hashMap.put("cell10", cell10);
        hashMap.put("cell11", cell11);
        hashMap.put("cell12", cell12);
        hashMap.put("cell13", cell13);
        hashMap.put("cell14", cell14);
        hashMap.put("cell15", cell15);
        hashMap.put("cell16", cell16);
        hashMap.put("cell17", cell17);
        hashMap.put("cell18", cell18);
        hashMap.put("cell19", cell19);
        hashMap.put("cell20", cell20);
        hashMap.put("cell21", cell21);
        hashMap.put("cell22", cell22);
        hashMap.put("cell23", cell23);
        hashMap.put("cell24", cell24);
        hashMap.put("cell25", cell25);
    }
    //endregion

    public ArrayList<Integer> GenerateRoute(int length)
    {
        ArrayList<Integer> route = new ArrayList<>();
        Random randomGenerator = new Random();

        // Generate random start position and add it to route
        int cellNumber = randomGenerator.nextInt(25) + 1;
        route.add(cellNumber);

        for (int i = 0; i < length - 1; i++)
        {
            // Get neighbour cells
            String cellName = "cell" + cellNumber;
            int[] neighbourCells = hashMap.get(cellName);

            if (AmIStuck(route, neighbourCells))
            {
                Log.e("AmIStuck", "Yes, you are stuck!");
                break;
            }

            // Choose random neighbour cell that hasn't been visited yet
            int index;
            do
            {
                index = randomGenerator.nextInt(neighbourCells.length);
            } while (route.contains(neighbourCells[index]));

            // Get new position and add it to path
            cellNumber = neighbourCells[index];
            route.add(cellNumber);
        }

        Log.e("Route positions", route.toString());
        return route;
    }

    private boolean AmIStuck(ArrayList<Integer> route, int[] neighbourCells)
    {
        for (int item : neighbourCells)
        {
            if (!route.contains(item))
            {
                return false;
            }
        }

        return true;
    }
}
