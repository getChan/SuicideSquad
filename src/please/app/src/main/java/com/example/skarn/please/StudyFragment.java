package com.example.skarn.please;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class StudyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_study, container, false);

        getFragmentManager().beginTransaction().add(R.id.study, new StudyFragment());

        ImageButton btn1 = (ImageButton) v.findViewById(R.id.button1);
        ImageButton btn2 = (ImageButton) v.findViewById(R.id.button2);
        ImageButton btn3 = (ImageButton) v.findViewById(R.id.button3);
        ImageButton btn4 = (ImageButton) v.findViewById(R.id.button4);
        ImageButton btn5 = (ImageButton) v.findViewById(R.id.button5);

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), StudyMainActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}

