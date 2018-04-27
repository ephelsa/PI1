package com.citophonapp.integrador.citophonapp.fragment;


import android.Manifest;
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

    private View rootView;

    private Button callButton;

    private Call call;
    private SinchClient sinchClient;


    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_information, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        sinchClient = Sinch.getSinchClientBuilder()
                .context(getContext())
                .userId(SinchConfig.CALLER_ID)
                .applicationKey(SinchConfig.APP_KEY)
                .applicationSecret(SinchConfig.APP_SECRET)
                .environmentHost(SinchConfig.ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        callButton = (Button) rootView.findViewById(R.id.callButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(SinchConfig.RECIPENT_ID);
                    call.addCallListener(new SinchCallListener());
                    callButton.setText("Colgar");
                } else {
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
            call = incomingCall;
            Toast.makeText(getContext(), "Llamada entrante", Toast.LENGTH_SHORT).show();
            call.answer();
            call.addCallListener(new SinchCallListener());
            callButton.setText("Colgar");
        }
    }
}
