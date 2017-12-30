package com.kavi.droid.exchange.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kavi.droid.exchange.utils.CommonUtils;

/**
 * Created by kavi707 on 10/8/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class ExchangeBaseActivity extends Activity {

    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init local
        commonUtils.setLocal(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Init local
        commonUtils.setLocal(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
