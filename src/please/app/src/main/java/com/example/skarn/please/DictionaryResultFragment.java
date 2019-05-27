package com.example.skarn.please;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

public class DictionaryResultFragment extends Fragment {
    public DictionaryResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout=(FrameLayout) inflater.inflate(R.layout.fragment_dictionary_result,container,false);
        String args = getArguments().getString("id");

        Toast.makeText(getContext(),args, Toast.LENGTH_SHORT).show();

        WebView mWebVIew = (WebView)layout.findViewById(R.id.webView);

        WebSettings mWebVIewSettings = mWebVIew.getSettings();
        mWebVIewSettings.setJavaScriptEnabled(true);
        mWebVIew.loadUrl("http://sldict.korean.go.kr/front/sign/signContentsView.do?current_pos_index=0&origin_no="+args);
        return layout;
    }
}
