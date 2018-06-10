package com.citophonapp.integrador.citophonapp.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.config.SinchConfig;
import com.citophonapp.integrador.citophonapp.entity.User;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private final String SHARED = "login";

    private View rootView;

    private Button callButton;
    private AlertDialog.Builder builder;


    private Call call;
    private SinchClient sinchClient;

    private User user;
    private User callUser;

    public InformationFragment() {
        // Required empty public constructor
    }


    private User readSharedPreferences() {
        SharedPreferences value = getActivity().getSharedPreferences(SHARED, 0);
        String l = value.getString("user", null);
        Gson gson = new Gson();

        User user = gson.fromJson(l, User.class);
        return user;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            callUser = (User) bundle.get("callUser");
        }
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_information, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }
        user = readSharedPreferences();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(getContext())
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

        callButton = (Button) rootView.findViewById(R.id.callButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(callUser.getCallId());
                    call.addCallListener(new SinchCallListener());
                    callButton.setText("Colgar");
                } else {
                    call.hangup();
                }
            }
        });

        builder = new AlertDialog.Builder(getActivity());
// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (call != null) {
                    call.answer();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (call != null) {
                    call.hangup();
                }
            }
        });

        return rootView;
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            callButton.setText("Llamar");
            getActivity().setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            Toast.makeText(getContext(), "Conectado", Toast.LENGTH_SHORT).show();
            getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            Toast.makeText(getContext(), "Llamando", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
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
                callButton.setText("Colgar");
            }
        }
    }


}
