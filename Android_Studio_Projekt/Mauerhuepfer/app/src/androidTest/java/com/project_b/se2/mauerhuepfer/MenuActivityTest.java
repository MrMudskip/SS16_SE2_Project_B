package com.project_b.se2.mauerhuepfer;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by rohrbe on 18.06.16.
 */
public class MenuActivityTest extends ActivityInstrumentationTestCase2<MenuActivity> {

    public MenuActivityTest() {
        super(MenuActivity.class);
    }

    public void testActivityExists() {
        MenuActivity activity = getActivity();
        assertNotNull(activity);
    }
}
