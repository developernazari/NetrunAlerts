package netrun.alert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebsiteCheck extends Service {
    Context context = this;
    public WebsiteCheck() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("","yessssss");
        ping();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void ping() {
        try {
            //Your code here or call a method
            Log.d("Checked","yesss");
            new myAsyncTask().execute("https://netrun.ir");
            new myAsyncTask().execute("https://voipshop.ir");
            new myAsyncTask().execute("https://adminkala.com");
        } catch (Exception e) {
            Log.e("Error", "In onStartCommand");
            e.printStackTrace();
        }
        scheduleNext();
    }

    private void scheduleNext() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() { ping(); }
        }, 120000);
    }
    public class myAsyncTask extends AsyncTask<String, Void, Void> {
        public String html;

        protected Void doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection response = null;
            try {
                response = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            html = "";
            InputStream in = null;
            try {
                in = response.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;

            try {
                while((line = reader.readLine()) != null){
                    str.append(line);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            html = str.toString();
            Log.d("LogggggG"," "+html);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(html.contains("نتران")||html.contains("ویپ")||html.contains("ادمین")){
                Toast.makeText(context, "Website is up and running", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "Website is Down!!!", Toast.LENGTH_LONG).show();
            }
        }


    }

}
