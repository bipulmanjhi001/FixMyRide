package com.ust.fixmyride.brand;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ust.fixmyride.R;
import com.ust.fixmyride.home.HomeActivity;
import com.ust.fixmyride.model.BrandItems;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;
import com.ust.fixmyride.pickmodel.Pick_Your_Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bipul on 09-09-2016.
 */
public class Pick_Your_Brand extends AppCompatActivity {
    ListItemsAdapter adapter;
    int i;
    ArrayList<BrandItems> brandtitems;
    ListView listView;
    com.ust.fixmyride.model.CircularImageView pickyourbrandfab;
    private ArrayList<HashMap<String, String>> list;
    private CoordinatorLayout coordinatorlayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_your_brand);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView=(ListView)findViewById(R.id.bandlist);
        listView.setDivider(null);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.pick_your_brand_coordinatorlayout);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check for Internet status
        if (isInternetPresent && gpsEnabled) {
            brandtitems = new ArrayList<BrandItems>();
            new BrandLayerCall().execute();
            adapter = new ListItemsAdapter(this, R.layout.brandlist, brandtitems);
            listView.setAdapter(adapter);
        }else{
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(coordinatorlayout, "Enable GPS and Internet!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(14);

            snackbarActionTextView.setTextColor(Color.RED);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
        pickyourbrandfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.pickyourbrandfab);
        pickyourbrandfab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ForAllDrawerFragment frag = new ForAllDrawerFragment();
                frag.show(ft, "txn_tag");
                return false;
            }
        });
    }
    //============= Listview Adapter Implementation;================//

    private class ListItemsAdapter extends ArrayAdapter<BrandItems> implements Filterable {
        ArrayList<BrandItems> branditems;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;

        public ListItemsAdapter(Context context, int resource, ArrayList<BrandItems> objects) {
            super(context, resource, objects);

            vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            branditems = objects;
        }

        @Override
        public int getCount() {
            return branditems.size();
        }

        @Override
        public BrandItems getItem(int position) {
            return branditems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                //add all items here
                holder.brandname= (TextView) v.findViewById(R.id.brandname);
                holder.radioButton=(RadioButton)v.findViewById(R.id.checkbrand);

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();// like a fragment class all items added to viewholder
            }
            holder.brandname.setText(brandtitems.get(position).getBrand_name());
            holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent intent=new Intent(Pick_Your_Brand.this,Pick_Your_Model.class);
                    startActivity(intent);
                    Pick_Your_Brand.this.finish();
                }
            });
            return v;
        }

    }

    private class ViewHolder{
        public TextView brandname;
        public RadioButton radioButton;
    }

    //---------------------------------------------------------Fatching data ----------------------------

    class BrandLayerCall extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... para) {

            for (i = 0; i < 50; i++) {
                BrandItems recentbrandItems= new BrandItems();
                recentbrandItems.setBrand_name("HYUNDAI");
                brandtitems.add(recentbrandItems);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}