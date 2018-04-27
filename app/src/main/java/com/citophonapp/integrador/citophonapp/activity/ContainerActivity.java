package com.citophonapp.integrador.citophonapp.activity;

import android.os.Bundle;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.fragment.LocalFragment;
import com.citophonapp.integrador.citophonapp.parent.ActivityFragmentUsage;

public class ContainerActivity extends ActivityFragmentUsage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        fragmentManager = getSupportFragmentManager();

        addFragment(new LocalFragment(), true, false, R.id.fragmentContainer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            this.finish();
        }
    }
}
