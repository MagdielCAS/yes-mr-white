package com.equipe.yesmrwhite.canhao;

import android.app.Fragment;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.syllas.yesmrwhite.R;

/**
 * Created by magdi on 18/05/2016.
 */
public class JogoCanhaoFragment extends Fragment {
    private ViewCanhao cannonView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        cannonView = (ViewCanhao) view.findViewById(R.id.cannonView);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    @Override
    public void onPause() {
        super.onPause();
        cannonView.stopGame();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cannonView.releaseResources();
    }
}
