package com.ust.fixmyride.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ust.fixmyride.R;

/**
 * Created by Bipul on 21-10-2016.
 */
public class CustomProgressDialog extends ProgressDialog {
    ImageView la;
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_progress_dialog);
         la = (ImageView) findViewById(R.id.animation);
        la.setBackgroundResource(R.drawable.custom_progress_dialog_animation);
        startAnimation();

    }
    private void startAnimation(){
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise_rotation);
        la.startAnimation(rotation);
    }

   /* private void stopAnimation(){
        la.clearAnimation();
    }*/
}
