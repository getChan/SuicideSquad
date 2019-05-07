package e.jin.last;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DictionaryFragment extends Fragment {
    String[] dataid= new String[9];
    String[] dataname = new String[9];

    public DictionaryFragment(){

    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.fragment_dictionary,container,false);
        // 통신해서 수어 id랑 이름 불러오기

        if (isNetworkAvailable()){
            String url = "http://10.92.159.82:3000/process/allword";
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
        else{
            Toast.makeText(getContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
        }

        String[] list_menu={"a","b","c"};

        Toast.makeText(getContext(),dataname[0],Toast.LENGTH_LONG).show();


        ListView listview=(ListView)layout.findViewById(R.id.list_menu);


        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,list_menu
        );
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"이벤트 발생", Toast.LENGTH_SHORT).show();
                DictionaryFragment dictionaryFragment = new DictionaryFragment();
                Bundle bundle = new Bundle(2);

//                bundle.putString("dataid", dataid);

//                bundle.putString("dataname", dataname);
                dictionaryFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DictionaryResultFragment()).commit();

            }
        });

        return layout;

    }
    private boolean isNetworkAvailable(){
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            available = true;
        return available;
    }

    private String downloadUrl(String strUrl) throws IOException{
        String s = null;
        byte[] buffer = new byte[1000];
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();
            iStream.read(buffer);
            s = new String(buffer);
        }catch (Exception e){
            Log.d("Exception download url", e.toString());
        }finally {
            iStream.close();
        }
        return s;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String>{
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
            try {
//                JSONObject wrapObject = new JSONObject(jsonString);
                JSONArray jsonArray = new JSONArray(jsonString);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject dataJsonObject = jsonArray.getJSONObject(i);

                    dataid[i] = dataJsonObject.getString("id");
                    dataname[i] = dataJsonObject.getString("word");
                }
            } catch (JSONException e){
                e.printStackTrace();
            }


            Toast.makeText(getContext(), "response sucsex", Toast.LENGTH_SHORT).show();
        }
    }

}
