package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class WebApiManager
{
    static class GetAllHighscoresAsync extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            HttpURLConnection httpURLConnection = null;
            try
            {
                URL parsedUrl = new URL("https://enigmatic-forest-25408.herokuapp.com/highscores");
                httpURLConnection = (HttpURLConnection) parsedUrl.openConnection();
                httpURLConnection.connect();
                int status = httpURLConnection.getResponseCode();
                switch (status)
                {
                    case 200:
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        int data = inputStreamReader.read();
                        String result = "";

                        while (data != -1)
                        {
                            char current = (char) data;
                            result += current;
                            data = inputStreamReader.read();
                        }
                        return result;

                    default:
                        return "Error. Couldn't get all highscores. HTTP status code: " + status;
                }
            }
            catch (Exception e)
            {
                Log.e("Error", "Couldn't create HTTP connection. Error message: " + e);
            }
            finally
            {
                if (httpURLConnection != null)
                {
                    try
                    {
                        httpURLConnection.disconnect();
                    }
                    catch (Exception e)
                    {
                        Log.e("Error", "Couldn't close HTTP connection. Error message: " + e);
                    }
                }
            }

            return null;
        }
    }

    static class SaveNewHighscoreAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            return null;
        }
    }
}