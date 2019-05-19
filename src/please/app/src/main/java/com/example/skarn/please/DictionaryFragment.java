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


    public DictionaryFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.fragment_dictionary,container,false);
        // 통신해서 수어 id랑 이름 불러오기

        String[] list_menu={"a","b","c"};
        String dataname[] = getArguments().getStringArray("dataname");

        ListView listview=(ListView)layout.findViewById(R.id.list_menu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,list_menu
        );
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"이벤트 발생", Toast.LENGTH_SHORT).show();
                DictionaryResultFragment dictionaryResultFragment= new DictionaryResultFragment();
                Bundle bundle = new Bundle(1);

                bundle.putString("dataid", "id");

//                bundle.putString("dataname", "name");
                dictionaryResultFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.dictionaryfrag, dictionaryResultFragment).commit();

            }
        });

        return layout;

    }





}
