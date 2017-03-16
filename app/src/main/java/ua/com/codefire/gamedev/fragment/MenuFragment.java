package ua.com.codefire.gamedev.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.codefire.gamedev.R;
import ua.com.codefire.gamedev.adapter.MenuListAdapter;

/**
 * Created by human on 2/11/17.
 */

public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String NAME = "name";
    public static final String ICON = "icon";

    public static final String[] SELECTION = {
            NAME,
            ICON,
    };
    public static final int[] PROJECTION = {
            R.id.text,
            R.id.icon,
    };
    private static final String TAG = "GMF";

    private Menu menu;
    private ListView lvMenu;
    private MenuListAdapter menuListAdapter;
    private FlowFragment flowFragment;

    public MenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowFragment = new FlowFragment();

        menu = new MenuBuilder(getContext());

        menuListAdapter = new MenuListAdapter(getContext(), menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menuView = inflater.inflate(R.layout.menu_fragment, container, false);

        // INITIALIZE LIST VIEW
        lvMenu = (ListView) menuView.findViewById(R.id.lvMenu);
        lvMenu.setAdapter(menuListAdapter);
        // HANDLE CLICK ON ITEMS IN LIST
        lvMenu.setOnItemClickListener(this);

        return menuView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (menu.getItem(position).getItemId()) {
            case R.id.mi_play:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main, flowFragment)
                        .add(R.id.activity_main, new GameFragment())
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.mi_stat:

                break;
        }
    }
}
