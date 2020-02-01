package com.example.digo_ec.service.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.digo_ec.R;


public class Vista_tabs extends LinearLayout {



    public Vista_tabs(Context c, int drawable, int drawableselec, String label) {
        super(c);

        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.tamano_icono_tabs, null); // Here tabicon layout contains imageview
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView nombre=(TextView) view.findViewById(R.id.nombre_icono_tab);
        nombre.setText(label);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(SELECTED_STATE_SET, this.getResources().getDrawable(drawable));
        listDrawable.addState(ENABLED_STATE_SET, this.getResources().getDrawable(drawableselec));
        icon.setImageDrawable(listDrawable);
        icon.setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.CENTER);
        addView(view);
    }



    public Vista_tabs(Context c, int drawable, int drawableselec, String nombre_icono, String x) {
        super(c);

        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.tabs_iconos_tienda, null); // Here tabicon layout contains imageview
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView nombre=(TextView) view.findViewById(R.id.nombre_icono_tab);
        nombre.setText(nombre_icono);


        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(SELECTED_STATE_SET, this.getResources().getDrawable(drawable));
        listDrawable.addState(ENABLED_STATE_SET, this.getResources().getDrawable(drawableselec));


        icon.setImageDrawable(listDrawable);
        icon.setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.CENTER);

        addView(view);
    }




}
