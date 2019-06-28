package com.ust.fixmyride.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.HashMap;

/**
 * Created by anand on 17/11/15.
 */
public class CUstomAutoCompleteText extends AutoCompleteTextView implements Filterable{

    public CUstomAutoCompleteText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //public CUstomAutoCompleteText(String s) {
     //   super();
   // }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    @Override
    public void performCompletion() {
        super.performCompletion();
    }

    /**
     * After a selection, capture the new value and append to the existing
     * text
     */
    @Override
    protected void replaceText(CharSequence text) {
        super.replaceText(text);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


}
