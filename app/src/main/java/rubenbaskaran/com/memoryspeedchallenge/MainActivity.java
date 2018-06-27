package rubenbaskaran.com.memoryspeedchallenge;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity
{
    View root;
    ArrayList<Integer> route;
    Button startGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(root);
        startGameBtn = findViewById(R.id.startGameBtn);
        EnableGridButtons(false);
    }

    public void onClick(View view)
    {
        ResetGridButtonsColor();
        AsyncCreateRoute asyncCreateRoute = new AsyncCreateRoute();
        asyncCreateRoute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onGridButtonClick(View view)
    {
        if (Integer.parseInt(view.getTag().toString()) == route.get(0))
        {
            route.remove(0);
            view.setBackground(getDrawable(R.drawable.grid_button_right_answer));
            if (route.isEmpty())
            {
                Toast.makeText(this, "Congratulations! Level completed!", Toast.LENGTH_LONG).show();
                EnableGridButtons(false);
                startGameBtn.setEnabled(true);
            }
        }
        else
        {
            view.setBackground(getDrawable(R.drawable.grid_button_wrong_answer));
            Toast.makeText(this, "Sorry! You failed!", Toast.LENGTH_LONG).show();
            EnableGridButtons(false);
            startGameBtn.setEnabled(true);
        }
    }

    private class AsyncCreateRoute extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            GameAlgorithm gameAlgorithm = new GameAlgorithm();
            route = gameAlgorithm.GenerateRoute(10);
            startGameBtn.setEnabled(false);
            EnableGridButtons(false);
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            for (int item : route)
            {
                Object[] output = {true, item};
                publishProgress(output);
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCreateRoute");
                }
                output[0] = false;
                publishProgress(output);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            Button btn = root.findViewWithTag(String.valueOf(values[1]));

            if (((boolean) values[0]))
            {
                btn.setBackgroundColor(Color.BLACK);
            }
            else
            {
                btn.setBackground(getDrawable(R.drawable.grid_button_background));
            }
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            EnableGridButtons(true);
        }
    }

    private void EnableGridButtons(boolean input)
    {
        root.findViewWithTag("1").setEnabled(input);
        root.findViewWithTag("2").setEnabled(input);
        root.findViewWithTag("3").setEnabled(input);
        root.findViewWithTag("4").setEnabled(input);
        root.findViewWithTag("5").setEnabled(input);
        root.findViewWithTag("6").setEnabled(input);
        root.findViewWithTag("7").setEnabled(input);
        root.findViewWithTag("8").setEnabled(input);
        root.findViewWithTag("9").setEnabled(input);
        root.findViewWithTag("10").setEnabled(input);
        root.findViewWithTag("11").setEnabled(input);
        root.findViewWithTag("12").setEnabled(input);
        root.findViewWithTag("13").setEnabled(input);
        root.findViewWithTag("14").setEnabled(input);
        root.findViewWithTag("15").setEnabled(input);
        root.findViewWithTag("16").setEnabled(input);
        root.findViewWithTag("17").setEnabled(input);
        root.findViewWithTag("18").setEnabled(input);
        root.findViewWithTag("19").setEnabled(input);
        root.findViewWithTag("20").setEnabled(input);
        root.findViewWithTag("21").setEnabled(input);
        root.findViewWithTag("22").setEnabled(input);
        root.findViewWithTag("23").setEnabled(input);
        root.findViewWithTag("24").setEnabled(input);
        root.findViewWithTag("25").setEnabled(input);
    }

    private void ResetGridButtonsColor()
    {
        root.findViewWithTag("1").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("2").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("3").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("4").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("5").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("6").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("7").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("8").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("9").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("10").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("11").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("12").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("13").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("14").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("15").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("16").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("17").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("18").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("19").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("20").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("21").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("22").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("23").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("24").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("25").setBackground(getDrawable(R.drawable.grid_button_background));
    }
}