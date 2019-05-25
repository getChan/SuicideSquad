package com.example.skarn.please;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DictionaryFragment extends Fragment {

    public String url = "http://117.16.244.58:3000/process/word";
    public DictionaryFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()){

            DictionaryFragment.DownloadTask downloadTask = new DictionaryFragment.DownloadTask();
            downloadTask.execute(url);
        }
        else{
            Toast.makeText(getContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
        }

    }
// TODO : search 버튼 누르면 서버로 request 후 response 받은 json 파싱 후 listview로 띄움
// TODO : listview 누르면 해당 id값 bundle로 넘겨주어 해당하는 수화 영상 띄운다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.fragment_dictionary,container,false);
        // 통신해서 수어 id랑 이름 불러오기

        String[] list_menu={"a","b","c"};
        ListView listview=(ListView)layout.findViewById(R.id.list_menu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,list_menu
        );
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DictionaryResultFragment dictionaryResultFragment= new DictionaryResultFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("dataid", "id");
                dictionaryResultFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.dictionaryfrag, dictionaryResultFragment).commit();

            }
        });
        return layout;
    }

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

            Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();
        }
    }



}
