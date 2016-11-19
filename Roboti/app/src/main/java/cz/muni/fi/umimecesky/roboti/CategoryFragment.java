package cz.muni.fi.umimecesky.roboti;


import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends ListFragment {

    private View rootView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        return rootView;
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        List<Category> listContact = getCategoryList();
        ListView lv = (ListView) getActivity().findViewById(R.id.categoryList);
        lv.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listContact));

        return rootView;
    }
*/

    private List<Category> getCategoryList() {

        ArrayList<Category> contactlist = new ArrayList<>();

        contactlist.add(new Category("Vybrane slová po B"));
        contactlist.add(new Category("Vybrane slová po P"));
        contactlist.add(new Category("Vybrane slová po L"));


        return contactlist;
    }
}

