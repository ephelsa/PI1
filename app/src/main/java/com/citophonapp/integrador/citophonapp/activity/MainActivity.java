package com.citophonapp.integrador.citophonapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.entity.User;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //constantes
    private final String SHARED = "login";

    // Control
    private static FragmentManager fragmentManager;

    // View element
    private RelativeLayout cardViewLocal;
    private TextView username, document, age;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //datos del usuario en cache , su existencia se comprueba al iniciar la aplicación por eso no se valida antes
        user = readSharedPreferences();
        // Control
        fragmentManager = getSupportFragmentManager();

        // View Element
        cardViewLocal = (RelativeLayout) findViewById(R.id.cardViewLocal);
        username = (TextView) findViewById(R.id.username);
        document = (TextView) findViewById(R.id.document);
        age = (TextView) findViewById(R.id.age);

        username.setText(user.getName());
        document.setText(user.getDocument());
        age.setText(user.getAge());

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

    private User readSharedPreferences() {
        SharedPreferences value = getSharedPreferences(SHARED, 0);
        String l = value.getString("user", null);
        Gson gson = new Gson();

        User user = gson.fromJson(l, User.class);
        return user;

    }

}
