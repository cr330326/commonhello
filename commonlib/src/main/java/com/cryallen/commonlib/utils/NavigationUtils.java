package com.cryallen.commonlib.utils;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

/**
 * NavigationView utils
 * Created by chenran3 on 2017/12/12.
 */
public class NavigationUtils {

    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView
                    .getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }
}
