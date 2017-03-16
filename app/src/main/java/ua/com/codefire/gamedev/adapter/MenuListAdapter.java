package ua.com.codefire.gamedev.adapter;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.com.codefire.gamedev.R;

/**
 * Created by human on 2/11/17.
 */

public class MenuListAdapter extends BaseAdapter {

    private Menu menu;
    private Context context;
    private LayoutInflater layoutInflater;

    public MenuListAdapter(Context context, Menu menu) {
        this.context = context;
        this.menu = menu;
        this.layoutInflater = LayoutInflater.from(context);

        // CREATE AND INFLATE MENU FROM XML
        new MenuInflater(context).inflate(R.menu.game_menu, this.menu);
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return menu.getItem(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.menu_item_layout, container, false);
        }

        MenuItem menuItem = menu.getItem(position);

        ImageView imIcon = (ImageView) convertView.findViewById(R.id.icon);
        imIcon.setImageDrawable(menuItem.getIcon());

        TextView tvText = (TextView) convertView.findViewById(R.id.text);
        tvText.setText(menuItem.getTitle());

        return convertView;
    }
}
