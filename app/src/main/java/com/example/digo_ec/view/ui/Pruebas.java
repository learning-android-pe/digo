package com.example.digo_ec.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.digo_ec.R;
import com.example.digo_ec.service.utils.Vista_tabs;
import com.example.digo_ec.view.ui.tienda.crear_tienda;
import com.example.digo_ec.view.ui.tienda.viewMultitienda;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import ru.nikartm.support.ImageBadgeView;

public class Pruebas extends AppCompatActivity implements login.sesionCorrecta, OnMapReadyCallback {

RelativeLayout primer_item,segundo_item,tercer_item,cuarto_item;

List<View> vista = new ArrayList<>();

View selection1,selection2,selection3,selection4;

    login sesion= new login();
    private GoogleMap mMap;

//////////////////////////////////////////
    FloatingActionButton multifuncion;
    RoundedImageView icono_toolbar;
    FrameLayout contenedor_todo;
    RelativeLayout contenedor_Main;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.layout_items_menu_principal);
      /*  getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_pruebas, new crear_tienda()).commit();*/


      UI_Mapa();
        multifuncion= findViewById(R.id.floating_multi);


        contenedor_todo=findViewById(R.id.contenedor_todo);
        contenedor_Main=findViewById(R.id.Contenedor_main);



        icono_toolbar=findViewById(R.id.icono_toolbar);
      primer_item=findViewById(R.id.first_menu_item);
      segundo_item=findViewById(R.id.second_menu_item);
      tercer_item=findViewById(R.id.third_menu_item);
      cuarto_item=findViewById(R.id.fourth_menu_item);



      selection1=findViewById(R.id.selection_item1);
      selection2=findViewById(R.id.selection_item2);
      selection3=findViewById(R.id.selection_item3);
      selection4=findViewById(R.id.selection_item4);
       vista.add(selection1);
       vista.add(selection2);
       vista.add(selection3);
       vista.add(selection4);



        logica_estetica_seleccion(0);



        icono_toolbar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.e("click","click");
          }
      });

        primer_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica_estetica_seleccion(0);
                Log.e("click","item");
                contenedor_Main.setVisibility(View.VISIBLE);
                contenedor_todo.setVisibility(View.GONE);
eliminar_vista();

            }
        });
        segundo_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica_estetica_seleccion(1);

                Log.e("click","item2");

                contenedor_Main.setVisibility(View.GONE);
                contenedor_todo.setVisibility(View.VISIBLE);


                eliminar_vista();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_todo, new crear_tienda()).commit();



            }
        });
        tercer_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica_estetica_seleccion(2);
                contenedor_Main.setVisibility(View.GONE);
                contenedor_todo.setVisibility(View.VISIBLE);


eliminar_vista();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_todo, new viewMultitienda()).commit();

                Log.e("click","item3");
            }
        });
        cuarto_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logica_estetica_seleccion(3);

                contenedor_Main.setVisibility(View.GONE);
                contenedor_todo.setVisibility(View.VISIBLE);

eliminar_vista();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_todo, new Menu()).commit();

                Log.e("click","item4");
            }
        });






        iniciar_tabs();
      botton();
    }

    TabLayout tabLayout;

    private void iniciar_tabs() {
         tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.marker, R.drawable.marker, "TODO")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.serviciosgeneral_filtro_color, R.drawable.serviciosgeneral_filtro, "Servicios")));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//Todo esta linea que tiene index debe ser usado para inicializar una vista esa y aparte la funcion que desea usarse
        tabLayout.getTabAt(0).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*manejadordefiltro = tab.getPosition();
                starFiltro();*/
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }



    boolean cambio= true;

    private void botton(){
        final FloatingActionButton multifuncion= findViewById(R.id.floating_multi);
        final BottomAppBar items= findViewById(R.id.bottomAppBar);


        multifuncion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cambio){
                    cambio=false;
                    setMargins(multifuncion, 0, 0, 0, 50);
                    tabLayout.setVisibility(View.VISIBLE);
                    items.setVisibility(View.GONE);
                    multifuncion.setImageResource(R.drawable.listov3);


                }else{
                    cambio=true;
                    setMargins(multifuncion, 0, 0, 0, 0);

                    tabLayout.setVisibility(View.INVISIBLE);
                    items.setVisibility(View.VISIBLE);
                    multifuncion.setImageResource(R.drawable.mapa2v3);


                }


            }
        });






    }


    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @SuppressLint("RestrictedApi")
    private void logica_estetica_seleccion(int x){

        for(int i=0; i<=3; i++){

            if(x==i){
                vista.get(i).setVisibility(View.VISIBLE);
            }else{
                vista.get(i).setVisibility(View.INVISIBLE);

            }



        }

        if(x==3){
            multifuncion.setVisibility(View.INVISIBLE);

        }else{
            multifuncion.setVisibility(View.VISIBLE);

            if(x==2){
                multifuncion.setImageResource(R.drawable.ic_add_black_24dp);

            }else if(x==1){
                multifuncion.setImageResource(R.drawable.flecha_arriba);

            }else if(x==0){
                multifuncion.setImageResource(R.drawable.mapa2v3);

            }

        }
    }

    @Override
    public void sesion() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng latLng2 = new LatLng(-2.192149, -79.890231);

        mMap.addMarker(new MarkerOptions().position(latLng2).title("Marker Carlos"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
        mMap.setMinZoomPreference(12);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }


    //Todo iniciar mapa
    private void UI_Mapa() {
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    FragmentManager fragmentManager = getSupportFragmentManager();

    private void eliminar_vista(){


        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment old = fragmentManager.findFragmentById(R.id.contenedor_todo);
        if(old!=null ) fragmentTransaction.remove(old).commit();

    }

}


