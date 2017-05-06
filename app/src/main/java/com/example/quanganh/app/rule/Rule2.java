package com.example.quanganh.app.rule;

import android.util.Log;

/**
 * Created by QuangAnh on 5/6/2017.
 */

public class Rule2 extends Rule {
    @Override
    public boolean checkValid(String s) {
        String notIn = "wjzf";
        for (int j = 0; j < s.length(); ++j) {
            if (notIn.contains(s.charAt(j) + ""))
                return false;
        }
        return true;
    }

    @Override
    public void show() {
        Log.e("Error", "Sai luat 2");
    }
}
