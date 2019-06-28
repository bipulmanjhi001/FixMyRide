package com.ust.fixmyride.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ust.fixmyride.R;
import com.ust.fixmyride.pickmodel.Pick_Your_Model;
import com.ust.fixmyride.pickmoreinfo.Pick_More_Info;

/**
 * Created by Bipul on 05-12-2016.
 */
public class VersionsList extends DialogFragment implements
        AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
        ListView mylist;
      SwipeRefreshLayout swipeRefreshLayout;
        String[] listitems = {"RL",
            "Coupe",
            "Sedan",
            "R",
            "Convertible",
            "Convertible",
            "V8",
            "W12",
            "V8",
            "V8S",
            "Speed",
            "V8",
            "V8S",
            "Speed",
            "V8",
            "W12"
    };
          @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.versionlist, null, false);
        mylist = (ListView) view.findViewById(R.id.versionpicker);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout_variant);
        return view;
        }

        @Override
       public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        R.layout.versionnames,R.id.versions, listitems);

        mylist.setAdapter(adapter);
        mylist.setDivider(null);
        mylist.setOnItemClickListener(this);

        }

       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
        Object item =parent.getItemAtPosition(position);
        String str=item.toString();
           Intent intent=new Intent(getActivity(),Pick_More_Info.class);
           startActivity(intent);
           dismiss();
        }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {

            }
        };
    }
}
