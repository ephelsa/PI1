package com.citophonapp.integrador.citophonapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.config.SinchConfig;
import com.citophonapp.integrador.citophonapp.entity.User;
import com.citophonapp.integrador.citophonapp.fragment.InformationFragment;
import com.google.gson.Gson;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    private final String SHARED = "login";


    private AlertDialog.Builder builder;


    private Call call;
    private SinchClient sinchClient;
    private Button callButton;
    private TextView member, represent;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        callButton = (Button) findViewById(R.id.usercallButton);
        member = (TextView) findViewById(R.id.usermembers);
        represent = (TextView) findViewById(R.id.userrepresent);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }
        user = readSharedPreferences();

        represent.setText(user.getName());
        String members = "";

        for (String k : user.getMembers()) {
            members += k + "\n";
        }
        member.setText(members);


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call != null) {
                    call.hangup();
                    callButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        sinchClient = Sinch.getSinchClientBuilder()
                .context(getApplicationContext())
                .userId(user.getCallId())
                .applicationKey(SinchConfig.APP_KEY)
                .applicationSecret(SinchConfig.APP_SECRET)
                .environmentHost(SinchConfig.ENVIRONMENT)
                .build();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        builder = new AlertDialog.Builder(this);
// Add the buttons
        builder.setTitle("Tienes una llamada");
        builder.setPositiveButton("Contestar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (call != null) {
                    call.answer();
                    callButton.setVisibility(View.VISIBLE);

                }
            }
        });
        builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (call != null) {
                    call.hangup();

                }
            }
        });


    }


    private User readSharedPreferences() {
        SharedPreferences value = (UserActivity.this).getSharedPreferences(SHARED, 0);
        String l = value.getString("user", null);
        Gson gson = new Gson();

        User user = gson.fromJson(l, User.class);
        return user;

    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call progressingCall) {
            Toast.makeText(getApplicationContext(), "Llamando", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();

            (UserActivity.this).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();

            UserActivity.this.setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            if (call == null) {
                call = incomingCall;
                call.addCallListener(new SinchCallListener());

                AlertDialog dialog = builder.create();

                dialog.show();
            /*call = incomingCall;
            Toast.makeText(getContext(), "Llamada entrante", Toast.LENGTH_SHORT).show();
            call.answer();
            call.addCallListener(new SinchCallListener());*/

            }
        }
    }


}
