package com.citophonapp.integrador.citophonapp.iface;

import android.support.v4.app.Fragment;

/**
 * Created by Leonardo Pérez on 16/03/2018.
 */

public interface IFragmentUsage {
    void addFragment(Fragment fragment, boolean addBackStack, boolean invalidateOptionsMenu, int containerID);

    void removeFragment(Fragment fragment);
}
