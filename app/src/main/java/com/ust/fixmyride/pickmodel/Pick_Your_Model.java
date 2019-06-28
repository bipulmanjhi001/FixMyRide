package com.ust.fixmyride.pickmodel;

import android.app.FragmentManager;
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
import com.ust.fixmyride.brand.Pick_Your_Brand;
import com.ust.fixmyride.model.ConnectionDetector;
import com.ust.fixmyride.model.ForAllDrawerFragment;
import com.ust.fixmyride.model.ProductItems;
import com.ust.fixmyride.model.VersionsList;
import com.ust.fixmyride.pickmoreinfo.Pick_More_Info;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bipul on 12-09-2016.
 */
public class Pick_Your_Model extends AppCompatActivity {
    ListItemsAdapter adapter;
    int i;
    ArrayList<ProductItems> productitems;
    ListView listView;
    com.ust.fixmyride.model.CircularImageView pickyourmodelfab;
    private ArrayList<HashMap<String, String>> list;

    private CoordinatorLayout coordinatorlayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    boolean gpsEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_your_model);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.pick_your_model_coordinatorlayout);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        listView=(ListView)findViewById(R.id.modellist);
        listView.setDivider(null);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // check for Internet status
        if (isInternetPresent && gpsEnabled) {

            productitems = new ArrayList<ProductItems>();
            new ProductLayerCall().execute();
            adapter = new ListItemsAdapter(this, R.layout.productlist, productitems);
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
        pickyourmodelfab=(com.ust.fixmyride.model.CircularImageView)findViewById(R.id.pickyourmodelfab);
        pickyourmodelfab.setOnTouchListener(new View.OnTouchListener() {
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

    private class ListItemsAdapter extends ArrayAdapter<ProductItems> implements Filterable {
        ArrayList<ProductItems> productitems;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;

        public ListItemsAdapter(Context context, int resource, ArrayList<ProductItems> objects) {
            super(context, resource, objects);

            vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            productitems = objects;
        }

        @Override
        public int getCount() {
            return productitems.size();
        }

        @Override
        public ProductItems getItem(int position) {
            return productitems.get(position);
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
                holder.productname= (TextView) v.findViewById(R.id.productname);
                holder.radioButton=(RadioButton)v.findViewById(R.id.checkproduct);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();// like a fragment class all items added to viewholder
            }
            holder.productname.setText(productitems.get(position).getProduct_name());
            holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FragmentManager manager = getFragmentManager();
                    VersionsList dialog = new VersionsList();
                    dialog.show(manager, "dialog");
                }
            });
            return v;
        }

    }

    private class ViewHolder{
        public TextView productname;
        public RadioButton radioButton;
    }

    //---------------------------------------------------------Fatching data ----------------------------

    class ProductLayerCall extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... para) {
            for (i = 0; i < 50; i++) {
                ProductItems recentproductItems= new ProductItems();
                recentproductItems.setProduct_name("HONDA AMAZE");
                productitems.add(recentproductItems);
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
        Intent intent=new Intent(this, Pick_Your_Brand.class);
        startActivity(intent);
        finish();
    }

}
