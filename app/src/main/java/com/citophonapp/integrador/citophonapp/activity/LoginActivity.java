package com.citophonapp.integrador.citophonapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    //firebase metodos
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("Users");

    //constantes
    private final String SHARED = "login";

    //variables vista
    private TextInputEditText user, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (TextInputEditText) findViewById(R.id.user);
        password = (TextInputEditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = user.getText().toString();
                //se realiza una busqueda en la base de datos para obtener el correo para login
                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //se verifica que exista el snap correspondiente a la llave primaria
                        if (dataSnapshot.exists()) {
                            //de existir se serializa en el objeto la informacion
                            User user = new User();
                            user.setDocument(dataSnapshot.child("document").getValue(String.class));
                            user.setAge(dataSnapshot.child("age").getValue(String.class));
                            user.setCallId(dataSnapshot.child("callId").getValue(String.class));
                            user.setEmail(dataSnapshot.child("email").getValue(String.class));
                            user.setName(dataSnapshot.child("name").getValue(String.class));
                            user.setVigilant(dataSnapshot.child("vigilant").getValue(Boolean.class));
                            List<String> members = new ArrayList<>();
                            for (DataSnapshot snap : dataSnapshot.child("members").getChildren()) {
                                members.add(snap.getValue(String.class));

                            }
                            user.setMembers(members);

                            CreateSharedPreferences(user);

                            //Se verifica que sea vigilante para poder acceder al modulo
                            if (user.getVigilant()) {
                                //se llama el metodo de inicio de sessión
                                auth.signInWithEmailAndPassword(user.getEmail(), password.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                //si se completo la operacion

                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    //si la operacion falla
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        if (existpreference()) {
                                            deleteDatabase(SHARED);
                                        }
                                    }
                                });
                                //si la llave primaria no se encuentra

                            } else {
                                auth.signInWithEmailAndPassword(user.getEmail(), password.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                //si se completo la operacion

                                                Intent intent = new Intent(getApplicationContext(), UserActivity.class);

                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    //si la operacion falla
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        if (existpreference()) {
                                            deleteDatabase(SHARED);
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario no existente", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    //ejecucion inicial
    @Override
    protected void onStart() {
        super.onStart();
        //obtiene el usuario
        FirebaseUser user = auth.getCurrentUser();
        //si el usuario existe pasa a la proxima ventana
        if (user != null) {
            if (existpreference()) {
                if (readSharedPreferences().getVigilant()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                }
            } else {
                auth.signOut();
                Toast.makeText(getApplicationContext(), "No se encontraron datos de usuario inicie de nuevo", Toast.LENGTH_LONG).show();
            }

        }
    }

    //Guarda el objeto en cache
    private void CreateSharedPreferences(User user) {
        SharedPreferences value = getSharedPreferences(SHARED, 0);
        SharedPreferences.Editor editor = value.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();

    }

    //Lee el objeto en cache
    private User readSharedPreferences() {
        SharedPreferences value = getSharedPreferences(SHARED, 0);
        String l = value.getString("user", null);
        Gson gson = new Gson();

        User user = gson.fromJson(l, User.class);
        return user;

    }

    //Verifica si existe el objeto en cache
    private boolean existpreference() {
        SharedPreferences value = getSharedPreferences(SHARED, 0);
        String l = value.getString("user", null);
        return l != null;
    }
}
