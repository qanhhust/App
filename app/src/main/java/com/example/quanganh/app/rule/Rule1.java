package com.example.quanganh.app.rule;

import android.util.Log;

/**
 * Created by QuangAnh on 5/6/2017.
 */

public class Rule1 extends Rule {

    @Override
    public boolean checkValid(String s) {
        String firstConsonant = "qsdđklxvb";
        for (int j = 1; j < s.length(); ++j) {
            if (firstConsonant.contains(s.charAt(j) + ""))
                return false;
        }
        return true;
    }

    @Override
    public void show() {
        Log.e("Error", "Sai luat 1");
    }
}
