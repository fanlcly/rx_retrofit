package com.fancy.rxretrofit.widget;

import android.animation.TimeInterpolator;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2018\7\7 0007
 * @since JDK 1.7
 */
public class EaseInOutCubicInterpolator implements  TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        if ((input *= 2) < 1.0f) {
            return 0.5f * input * input * input;
        }
        input -= 2;
        return 0.5f * input * input * input + 1;
    }
}
