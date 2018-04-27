package com.citophonapp.integrador.citophonapp.parent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.citophonapp.integrador.citophonapp.iface.IFragmentUsage;

/**
 * Created by Leonardo Pérez on 16/03/2018.
 */

public abstract class FragmentFragmentUsage extends Fragment implements IFragmentUsage {
    // Control
    protected static FragmentManager fragmentManager;

    public void addFragment(Fragment fragment, boolean addBackStack, boolean invalidateOptionsMenu, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (addBackStack) {
            String backStackName = fragment.getClass().getName();

            transaction.replace(containerId, fragment, backStackName);
            transaction.addToBackStack(backStackName);
        } else {
            transaction.replace(containerId, fragment);
        }

        if (invalidateOptionsMenu) {
            getActivity().invalidateOptionsMenu();
        }

        transaction.commit();
    }

    public void removeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment).commit();
    }
}
