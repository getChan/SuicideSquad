package e.jin.last;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class DictionaryFragment extends Fragment {

    public DictionaryFragment(){

    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.fragment_dictionary,container,false);

        String[] list_menu={"a","b","c"};

        ListView listview=(ListView)layout.findViewById(R.id.list_menu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,list_menu
        );
        listview.setAdapter(listViewAdapter);

        return layout;

        }
}
