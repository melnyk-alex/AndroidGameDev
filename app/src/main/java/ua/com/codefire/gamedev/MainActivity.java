package ua.com.codefire.gamedev;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.com.codefire.gamedev.fragment.FlowFragment;
import ua.com.codefire.gamedev.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity {

    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        menuFragment = new MenuFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, menuFragment)
                .commit();
    }
}
