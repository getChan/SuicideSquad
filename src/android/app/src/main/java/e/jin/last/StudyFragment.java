package e.jin.last;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.w("first click","click!");
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyMainFragment()).commit();
                        }
                    });
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyMainFragment()).commit();
                        }
                    });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyMainFragment()).commit();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyMainFragment()).commit();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyMainFragment()).commit();
            }
        });

        return v;
    }
}


   /* public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StudyMainFragment.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StudyMainFragment.class);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StudyMainFragment.class);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StudyMainFragment.class);
                startActivity(intent);
            }
        });*/








