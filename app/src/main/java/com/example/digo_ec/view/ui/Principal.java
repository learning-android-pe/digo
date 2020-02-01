package com.example.digo_ec.view.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.digo_ec.R;
import com.example.digo_ec.service.model.Item_menu;
import com.example.digo_ec.service.model.Usuario;
import com.example.digo_ec.service.utils.Global;
import com.example.digo_ec.service.utils.Vista_tabs;
import com.example.digo_ec.view.adapter.Menuadapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity  implements login.sesionCorrecta, OnMapReadyCallback {


    login sesion= new login();
    private GoogleMap mMap;
    int manejador_de_fragments = 0;
    FrameLayout frame_vistas,frame_busqueda;
    RelativeLayout layout_principal;
    TabLayout tabLayout;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseauth= FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListenerRegistration registration;
    FrameLayout contenedor_todo;
    RelativeLayout contenedor_Main;

    ArrayAdapter adapterCat , adapterSubcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_items_menu_principal);
        contenedor_todo=findViewById(R.id.contenedor_todo);
        contenedor_Main=findViewById(R.id.Contenedor_main);
        firebaseauth.signOut();

        if(user != null) {
        Log.e("inicio","normal");
        Global.usuario.setCorreo(user.getEmail());
        iniciar_main();

        }else{

            Log.e("inicio","login");

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_todo, sesion).commit();
        }


    }




    private void iniciar_main(){
        inicio_login();
        iniciar_tabs();
        botton_sheet();
        botton();
        UI_Mapa();
    }


    private  void inicio_login(){
        contenedor_todo.setVisibility(View.GONE);
        contenedor_Main.setVisibility(View.VISIBLE);
    }



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



    //Todo iniciar mapa
    private void UI_Mapa() {
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }





private void escuchar_datos_usuario(){

    DocumentReference datos = db.collection("USUARIOS").document(Global.usuario.getCorreo());
    registration = datos.addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            if (e != null) {

                Log.e("usuario", e.toString());
                return;
            }
            try {
                Global.usuario = documentSnapshot.toObject(Usuario.class);
            } catch (Exception ee) {
                Toast.makeText(getApplicationContext(), "" + ee.toString(), Toast.LENGTH_LONG).show();
            }

            datos_sheet();
        }
    });
}


    TextView nombre_foto;
    ImageView perfil_foto;
    RecyclerView recyclerVistaItems;
    Menuadapter menuadapter;
    LinearLayout layout_monedas;
    List<Item_menu> lstitem=new ArrayList<>();
    RelativeLayout custom_tool;
    LinearLayout contenedor_busqueda;
    RoundedImageView icono_buscar ;
    TextView cancelar;
    LinearLayout busqueda;
    ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation anim2 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
    BottomSheetBehavior bottomSheetBehavior;
    View botton_sheeet;
    EditText escribir_busqueda;
    RoundedImageView icono_toolbar;



    private void botton_sheet(){

        frame_busqueda=findViewById(R.id.vista_busqueda);
        layout_principal = findViewById(R.id.constraintLayout);


        contenedor_busqueda = findViewById(R.id.contenedor_busqueda);
        icono_buscar = findViewById(R.id.icono_buscar);
        cancelar=findViewById(R.id.cancelar_busqueda);
        busqueda = findViewById(R.id.busqueda);
        escribir_busqueda=findViewById(R.id.escribir_busqueda);

        icono_toolbar = findViewById(R.id.icono_toolbar);
        layout_monedas=findViewById(R.id.layout_monedas);


        botton_sheeet=findViewById(R.id.botton_sheet);
        nombre_foto=botton_sheeet.findViewById(R.id.nombre_perfil_foto);
        perfil_foto=botton_sheeet.findViewById(R.id.perfil_foto);

        recyclerVistaItems=botton_sheeet.findViewById(R.id.recycler_menu);
        bottomSheetBehavior= BottomSheetBehavior.from(botton_sheeet);
        custom_tool=findViewById(R.id.contenedor_tool);
        iniciar_items();
        iniciar_recycler_items();
      //  direccion();
        datos_sheet();
        movimiento_busqueda();
    }
    private void iniciar_items(){

        lstitem.add(new Item_menu(R.drawable.cerca_de_mi_v3,"Cerca de mi","Descubre o Pregunta y yo te lo digo"));
        lstitem.add(new Item_menu(R.drawable.chat_v3,"Chat","Consulta el producto con un mensaje"));
        lstitem.add(new Item_menu(R.drawable.tendencia_v3,"Tendencias","Top de Tiendas a tu alrededor"));
        lstitem.add(new Item_menu(R.drawable.promociones_v3,"Promociones","Las mejores Promociones a tu alcance"));
        lstitem.add(new Item_menu(R.drawable.favoritos_v3,"Favoritos","Mira tus Productos o Tiendas Favoritos"));
        lstitem.add(new Item_menu(R.drawable.recientes_v3,"Recientes","Visualiza tus Actividades Recientes"));
        lstitem.add(new Item_menu(R.drawable.categoria_v3,"Categorias","Mira lo que esconde cada categoria de Tienda"));
        lstitem.add(new Item_menu(R.drawable.tienda_v3,"Mis negocios","Administra y date a conocer con tus negocios "));
        lstitem.add(new Item_menu(R.drawable.comunidad_v3,"Comunidad digo","Mira que Famosos usan la App"));
        lstitem.add(new Item_menu(R.drawable.soporte_v3,"Soporte","Comunicate con nosotros y danos tu inquietud"));
        lstitem.add(new Item_menu(R.drawable.cerrar_v3,"Cerrar Sesion","Date un Descanso y Regresa Pronto "));


    }
    private void iniciar_recycler_items(){

        menuadapter=new Menuadapter(lstitem, new Menuadapter.OnItemClick() {
            @Override
            public void onItemClickImagen(int position) {



                Log.e("posicion",""+position);



              //  seleccionar_fragment(position);
            }
        });
        recyclerVistaItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerVistaItems.setHasFixedSize(true);
        recyclerVistaItems.setAdapter(menuadapter);




        icono_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Estado",""+bottomSheetBehavior.getState());

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });





        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        Log.e("Estado","collapsado");
                        icono_toolbar.setVisibility(View.VISIBLE);
                        layout_monedas.setVisibility(View.VISIBLE);
                        icono_buscar.setVisibility(View.VISIBLE);




                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Estado","dragging");

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Estado","exxpandido");
                        icono_toolbar.setVisibility(View.GONE);
                        layout_monedas.setVisibility(View.GONE);
                        icono_buscar.setVisibility(View.GONE);



                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        Log.e("Estado","half expanded");
                        icono_toolbar.setVisibility(View.VISIBLE);
                        layout_monedas.setVisibility(View.VISIBLE);
                        icono_buscar.setVisibility(View.VISIBLE);




                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Estado","hidden");
                        icono_toolbar.setVisibility(View.VISIBLE);
                        layout_monedas.setVisibility(View.VISIBLE);
                        icono_buscar.setVisibility(View.VISIBLE);



                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Estado","settling");

                        break;





                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }
    private void datos_sheet(){
        nombre_foto.setText(Global.usuario.getNombre()+" "+Global.usuario.getApellido());
        Glide
                .with(getApplicationContext())
                .load(Global.usuario.getFoto_perfil())
                .error(R.drawable.user_imagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(perfil_foto);

    }
    private void movimiento_busqueda(){

        icono_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cancelar.setVisibility(View.GONE);

                contenedor_busqueda.setVisibility(View.VISIBLE);

//Write whatever to want to do after delay specified (1 sec)
                icono_buscar.setVisibility(View.INVISIBLE);//escondo el icono buscar para que no se vea feo





                anim.setDuration(400);
                busqueda.startAnimation(anim);

                if(manejador_de_fragments==0){

                    /*buscar_tienda_productos= new Tienda_Productos();
                    buscar_tienda_productos.manejador=manejadordefiltro;

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.vista_busqueda, buscar_tienda_productos).commit();

                    */

                    frame_busqueda.setVisibility(View.VISIBLE);
                    layout_principal.setVisibility(View.GONE);
                    Log.e("fragment","buscar abrir");

                }


                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        cancelar.setVisibility(View.VISIBLE);





                        //  anim.setDuration(200);
                        //  cancelar.startAnimation(anim);
                    }
                }, 375);



                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)


                        custom_tool.setVisibility(View.INVISIBLE);

                    }
                }, 300);

            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(manejador_de_fragments==0){
                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    frame_busqueda.setVisibility(View.GONE);
                    layout_principal.setVisibility(View.VISIBLE);


                    Fragment old = fragmentManager.findFragmentById(R.id.vista_busqueda);
                    if(old!=null ) fragmentTransaction.remove(old).commit();

                    Log.e("fragment","buscar cerrar");

                }






                //
                custom_tool.setVisibility(View.VISIBLE);

                anim2.setDuration(400);
                contenedor_busqueda.startAnimation(anim2);

                anim2.setDuration(300);
                cancelar.startAnimation(anim2);




                anim.setDuration(250);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        icono_buscar.setVisibility(View.VISIBLE);//lo regreso a la normalidad
                        contenedor_busqueda.setVisibility(View.INVISIBLE);
                        icono_buscar.startAnimation(anim);



                    }
                }, 250);
            }
        });




        escribir_busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {



                if(manejador_de_fragments==0){
                //    buscar_tienda_productos.filtro(editable.toString(),manejadordefiltro);

                }








                //  filter(editable.toString());

            }
        });
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

    @Override
    public void sesion() {
      iniciar_main();
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
                    setMargins(multifuncion, 0, 0, 0, 90);
                    tabLayout.setVisibility(View.VISIBLE);
                    items.setVisibility(View.GONE);
                    multifuncion.setImageResource(R.drawable.esconder_filtro);


                }else{
                    cambio=true;
                    setMargins(multifuncion, 0, 0, 0, 0);

                    tabLayout.setVisibility(View.INVISIBLE);
                    items.setVisibility(View.VISIBLE);
                    multifuncion.setImageResource(R.drawable.flecha_arriba);


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


}
