package rubenbaskaran.com.memoryspeedchallenge;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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
    }

    public void onClick(View view)
    {
        AsyncCreateRoute asyncCreateRoute = new AsyncCreateRoute();
        asyncCreateRoute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            startGameBtn.setEnabled(true);
        }
    }
}