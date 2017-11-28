package com.kavi.droid.exchange.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kavi.droid.exchange.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kavi707 on 11/12/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class NavigationUtil {

    private Activity currentActivity;
    private Class navigationClass;
    private Map<String, Object> paramsObject = null;
    private boolean isCurrentFinish = false;

    private boolean isFlagsAvailable = false;
    private List<Integer> intentFlags;

    private boolean isAnimate = false;
    private int animType;
    public static int ANIM_FADE_IN = 1;
    public static int ANIM_FADE_OUT = 2;
    public static int ANIM_LEFT_TO_RIGHT = 3;
    public static int ANIM_RIGHT_TO_LEFT = 4;

    public NavigationUtil(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public NavigationUtil to(Class navigationClass) {
        this.navigationClass = navigationClass;
        return this;
    }

    public NavigationUtil withParams(Map<String, Object> paramsObj) {
        this.paramsObject = paramsObj;
        return this;
    }

    public NavigationUtil setTransitionAnim(int animType) {
        isAnimate = true;
        this.animType = animType;
        return this;
    }

    public NavigationUtil setFlags(List<Integer> flags) {
        this.isFlagsAvailable = true;
        this.intentFlags = flags;
        return this;
    }

    public NavigationUtil finish() {
        this.isCurrentFinish = true;
        return this;
    }

    public void go() {
        Intent navigationIntent = new Intent(this.currentActivity, this.navigationClass);

        if (isFlagsAvailable) {
            for (Integer intentFlag : intentFlags) {
                navigationIntent.setFlags(intentFlag);
            }
        }

        if (paramsObject != null) {
            Bundle paramsBundle = new Bundle();

            List<String> keys = new ArrayList<String>(this.paramsObject.keySet());
            for (String key: keys) {
                Object value = this.paramsObject.get(key);
                if (value instanceof String) {
                    paramsBundle.putString(key, (String)value);
                } else if (value instanceof Integer) {
                    paramsBundle.putInt(key, (Integer) value);
                } else if (value instanceof Float) {
                    paramsBundle.putFloat(key, (Float) value);
                } else if (value instanceof Long) {
                    paramsBundle.putLong(key, (Long) value);
                } else if (value instanceof Boolean) {
                    paramsBundle.putBoolean(key, (Boolean) value);
                }
            }

            navigationIntent.putExtras(paramsBundle);
        }

        currentActivity.startActivity(navigationIntent);
        if (isAnimate) {
            if (animType == ANIM_FADE_IN) {
                currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (animType == ANIM_FADE_OUT){
                currentActivity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            } else if (animType == ANIM_LEFT_TO_RIGHT) {
                currentActivity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            } else if (animType == ANIM_RIGHT_TO_LEFT) {
                currentActivity.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        }

        if (isCurrentFinish) {
            currentActivity.finish();
        }
    }
}
