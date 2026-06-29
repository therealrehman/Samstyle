package com.colorkey.keyboard.effects;

import android.content.Context;
import android.view.View;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

public class KeyboardEffectManager {
    private final Context context;

    public KeyboardEffectManager(Context context) {
        this.context = context;
    }

    public void applyRadialKeyPress(View keyView, int baseColor) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.8f);
        animator.setDuration(320);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
