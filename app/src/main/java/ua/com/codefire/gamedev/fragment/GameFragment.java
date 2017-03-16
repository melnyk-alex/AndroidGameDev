package ua.com.codefire.gamedev.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ua.com.codefire.gamedev.view.GameView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by human on 2/11/17.
 */

public class GameFragment extends Fragment {

    private SensorManager manager;
    private GameView gameView;

    public GameFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);

        Window window = ((Activity) getContext()).getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gameView = new GameView(getContext());

        return gameView;
    }

    @Override
    public void onResume() {
        manager.registerListener(gameView, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        super.onResume();
    }

    @Override
    public void onPause() {
        manager.unregisterListener(gameView);

        super.onPause();
    }
}
