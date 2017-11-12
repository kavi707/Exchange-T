package com.kavi.droid.exchange.utils;

import android.app.Activity;
import android.content.Intent;

import com.kavi.droid.exchange.R;

import java.util.List;

/**
 * Created by kavi707 on 11/12/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class NavigationUtil {

    private Activity currentActivity;
    private Class navigationClass;
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
