package com.colorkey.keyboard.themes;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.colorkey.keyboard.effects.KeyboardEffectManager;

public class MasterThemeManager {
    private final Context context;
    private final KeyboardEffectManager effectManager;
    private int primaryColor = Color.parseColor("#FF4081");

    public MasterThemeManager(Context context) {
        this.context = context;
        this.effectManager = new KeyboardEffectManager(context);
    }

    public void applyMasterThemeOnKeyPress(View keyView) {
        effectManager.applyRadialKeyPress(keyView, primaryColor);
    }

    public void updatePrimaryColor(int color) {
        this.primaryColor = color;
    }
}
