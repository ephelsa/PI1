package com.citophonapp.integrador.citophonapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.parent.FragmentFragmentUsage;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends FragmentFragmentUsage implements View.OnClickListener {

    // View Element
    private InformationFragment informationFragment;

    private CardView infoA;
    private ImageButton showMore;

    public LocalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_local, container, false);

        // Control
        fragmentManager = getActivity().getSupportFragmentManager();

        // View Element
        infoA = (CardView) rootView.findViewById(R.id.inforA);
        showMore = (ImageButton) infoA.findViewById(R.id.showMore);

        // Click event
        showMore.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == showMore) {
            informationFragment = new InformationFragment();

            addFragment(informationFragment, true, false, R.id.informationFragment);
        }
    }
}
