package com.example.skarn.please;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class DictionaryFragment extends Fragment {

    private String url = "http://117.16.244.58:3000/process/word";

    private ArrayList<String> list_menu_id = new ArrayList<String>();
    private ArrayList<String> list_menu_name = new ArrayList<String>();

    private ListView listview;
    private ArrayAdapter<String> listViewAdapter;
    private SearchView searchView;

    public DictionaryFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
// TODO : search 버튼 누르면 서버로 request 후 response 받은 json 파싱 후 listview로 띄움
// TODO : listview 누르면 해당 id값 bundle로 넘겨주어 해당하는 수화 영상 띄운다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 레이아웃 초기화
        RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.fragment_dictionary,container,false);

        listview=(ListView)layout.findViewById(R.id.list_menu);
        listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,list_menu_name
        );
        listview.setAdapter(listViewAdapter);

        searchView = layout.findViewById(R.id.search_dic);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // search 리스너
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.d(TAG, "QUERY [{\"wordName\":\""+query+"\"}]");

                // on submit
                JSONArray requestjsonArray = new JSONArray();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("wordName", query);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                requestjsonArray.put(jsonObject);

                Log.d(TAG, "JSON"+requestjsonArray.toString());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, requestjsonArray, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // arraylist 초기화
                        list_menu_id.clear();
                        list_menu_name.clear();
                        // json parse
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // response to arraylist
                                list_menu_id.add(jsonObject.getString("id"));
                                list_menu_name.add(jsonObject.getString("word"));
                            } catch (JSONException e){
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");

                        return super.getHeaders();
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
                // resuest
                queue.add(jsonArrayRequest);

                // refresh listview
                listViewAdapter.notifyDataSetChanged();
                listview.setAdapter(listViewAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DictionaryResultFragment dictionaryResultFragment= new DictionaryResultFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("id", list_menu_id.get(position));
                dictionaryResultFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.dictionaryfrag, dictionaryResultFragment).commit();

            }
        });
    }
}
