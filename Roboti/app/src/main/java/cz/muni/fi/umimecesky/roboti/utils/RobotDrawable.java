package cz.muni.fi.umimecesky.roboti.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.muni.fi.umimecesky.roboti.R;

public class RobotDrawable {

    private List<String> robotNames = new ArrayList<>();
    private Activity activity;

    private final Random RANDOM = new SecureRandom();

    public RobotDrawable(Activity activity) {

        this.activity = activity;
        Field[] drawables = R.drawable.class.getFields();
        for (Field f : drawables) {
            String fieldName = f.getName();
            if (fieldName.startsWith("robot")) {
                robotNames.add(f.getName());
            }
        }
    }


    public Drawable removeRobotDrawable() {

        Resources resources = activity.getResources();
        int num = RANDOM.nextInt(robotNames.size());
        String randomDrawableName = robotNames.remove(num);
        Log.v("num", String.valueOf(num));
        Log.v("random", randomDrawableName);
        Log.v("list", String.valueOf(robotNames));
        final int resourceId = resources.getIdentifier(randomDrawableName, "drawable",
                activity.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
