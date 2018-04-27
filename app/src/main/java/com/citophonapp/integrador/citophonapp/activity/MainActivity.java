package com.citophonapp.integrador.citophonapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.citophonapp.integrador.citophonapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Control
    private static FragmentManager fragmentManager;

    // View element
    private RelativeLayout cardViewLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Control
        fragmentManager = getSupportFragmentManager();

        // View Element
        cardViewLocal = (RelativeLayout) findViewById(R.id.cardViewLocal);

        // Click event
        cardViewLocal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == cardViewLocal) {
            Intent intent = new Intent(this, ContainerActivity.class);

            startActivity(intent);
        }
    }

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
            invalidateOptionsMenu();
        }

        transaction.commit();
    }

    public static void removeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment).commit();
    }

}
