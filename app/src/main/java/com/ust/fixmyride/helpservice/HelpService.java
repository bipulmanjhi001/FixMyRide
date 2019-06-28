package com.ust.fixmyride.helpservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ListView;

import com.ust.fixmyride.R;

/**
 * Created by Bipul on 08-12-2016.
 */
public class HelpService extends AppCompatActivity{
    ListView diagnoselistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdiagnose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        diagnoselistView=(ListView)findViewById(R.id.diagnose_list);

    }
}
