package com.example.skarn.please;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String[] dataid= new String[10];
    String[] dataname = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav=findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StudyFragment()).commit();
//        if (isNetworkAvailable()){
//            String url = "http://117.16.244.58:3000/process/allword";
//            MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask();
//            downloadTask.execute(url);
//        }
//        else{
//            Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
//        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null; //처음에 선택된 액티비티 x

                    switch(item.getItemId()){
                        case R.id.navigation_study:
                            selectedFragment=new StudyFragment();
                            break;
                        case R.id.navigation_dictionary:
                            selectedFragment=new DictionaryFragment();
                            Bundle bundle = new Bundle(1);
                            bundle.putStringArray("dataname", dataname);
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment=new NotificationsFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    private boolean isNetworkAvailable(){
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            available = true;
        return available;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String str;
        String receiveMsg=null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStreamReader tmp = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuffer buffer = new StringBuffer();
            while ((str = reader.readLine()) != null){
                buffer.append(str);
            }
            receiveMsg = buffer.toString();
            Log.i("receiveMsg", receiveMsg);
            reader.close();

        }catch (Exception e){
            Log.d("Exception download url", e.toString());
        }finally {

        }
        return receiveMsg;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String s = null;

        @Override
        protected String doInBackground(String... url) {
            try {
                s = downloadUrl(url[0]);
            } catch (Exception e){
                Log.d("Background Task", e.toString());
            }
            return s;
        }

        @Override
        protected void onPostExecute(String jsonString) {

            String test = "";
            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = new JSONArray(jsonString);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject dataJsonObject = jsonArray.getJSONObject(i);

//                    test += dataJsonObject.getString("id");
                    dataname[i] = dataJsonObject.getString("word");
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            Toast.makeText(getBaseContext(), test, Toast.LENGTH_SHORT).show();
        }
    }
}
