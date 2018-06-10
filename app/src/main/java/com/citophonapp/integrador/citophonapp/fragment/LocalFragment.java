package com.citophonapp.integrador.citophonapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.adapter.RvAdapter;
import com.citophonapp.integrador.citophonapp.entity.User;
import com.citophonapp.integrador.citophonapp.parent.FragmentFragmentUsage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends FragmentFragmentUsage implements View.OnClickListener {

    // View Element
    private InformationFragment informationFragment;

    private RecyclerView rv;
    private CardView infoA;
    private ImageButton showMore;

    private List<User> users;
    private RvAdapter rva;

    //firebasedatabase variables
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = database.getReference("Users");

    public LocalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_local, container, false);

        //iniciacion variables
        users = new ArrayList<>();
        rv = (RecyclerView) rootView.findViewById(R.id.localRv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        /*
        // Control
        fragmentManager = getActivity().getSupportFragmentManager();

        // View Element
        infoA = (CardView) rootView.findViewById(R.id.inforA);
        showMore = (ImageButton) infoA.findViewById(R.id.showMore);

        // Click event
        showMore.setOnClickListener(this);
*/
        //metodo de carga de datos bd
        loadData();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == showMore) {
            informationFragment = new InformationFragment();

            addFragment(informationFragment, true, false, R.id.informationFragment);
        }
    }

    //carga los datos de la base de datos
    private void loadData() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //busqueda en la lista optenida
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    //la lista solo agrega personas que no sean vigilantes

                    /*
                     *
                     ***********************************************************
                     **Este if es para que no aparezcan vigilantes en la lista**
                     ***********************************************************
                     */
                    // if (!snap.child("vigilant").getValue(Boolean.class)) {
                        User user = new User();
                        user.setDocument(snap.child("document").getValue(String.class));
                        user.setAge(snap.child("age").getValue(String.class));
                        user.setCallId(snap.child("callId").getValue(String.class));
                        user.setEmail(snap.child("email").getValue(String.class));
                        user.setName(snap.child("name").getValue(String.class));
                        user.setVigilant(snap.child("vigilant").getValue(Boolean.class));
                    user.setLocalNumber(snap.child("localNumber").getValue(String.class));
                        List<String> members = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.child("members").getChildren()) {
                            members.add(snapshot.getValue(String.class));

                        }
                        user.setMembers(members);
                        users.add(user);
                    //     }
                }
                rva = new RvAdapter(users);
                rv.setAdapter(rva);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
