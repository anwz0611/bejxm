package com.zyinfo.brj.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by Zwei  on 2018/6/1.
 * E-Mail Address：592296083@qq.com
 */

public class MyXFormatter  implements IAxisValueFormatter {

    private ArrayList<String> mValues;

    public MyXFormatter( ArrayList<String> values) {
        this.mValues = values;
    }
    private static final String TAG = "MyXFormatter";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Log.d(TAG, "----->getFormattedValue: "+value);
        return mValues.get((int) value % mValues.size());
    }
}

