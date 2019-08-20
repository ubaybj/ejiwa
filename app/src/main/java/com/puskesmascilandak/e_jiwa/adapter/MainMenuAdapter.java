package com.puskesmascilandak.e_jiwa.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;

public class MainMenuAdapter extends ArrayAdapter<MainMenuAdapter.Menu> {
    private final Context context;

    public MainMenuAdapter(Context context) {
        super(context, R.layout.menu_main_item);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.menu_main_item, null);
        }

        ImageView logoMenu = view.findViewById(R.id.logo_menu);
        TextView titleMenu = view.findViewById(R.id.title_menu_textview);
        TextView descriptionMenu = view.findViewById(R.id.description_menu_textview);

        Menu menu = getItem(position);
        if (menu != null) {
            Drawable logo = context.getResources()
                    .getDrawable(menu.drawable);
            logoMenu.setImageDrawable(logo);

            titleMenu.setText(menu.title);
            descriptionMenu.setText(menu.description);
        }
        return view;
    }

    public static class Menu {
        private String title;
        private String description;
        private int drawable;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }
    }
}
