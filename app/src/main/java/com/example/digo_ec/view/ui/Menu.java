package com.example.digo_ec.view.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.digo_ec.R;
import com.example.digo_ec.service.model.Item_menu;
import com.example.digo_ec.view.adapter.Menuadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Menu extends Fragment {


    public Menu() {
        // Required empty public constructor
    }

    List<Item_menu> lstitem=new ArrayList<>();
    TextView nombre_foto;
    ImageView perfil_foto;
    RecyclerView recyclerVistaItems;
    Menuadapter menuadapter;
    View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.menu_sheet, container, false);

        nombre_foto=vista.findViewById(R.id.nombre_perfil_foto);
        perfil_foto=vista.findViewById(R.id.perfil_foto);

        recyclerVistaItems=vista.findViewById(R.id.recycler_menu);



        iniciar_items();
        iniciar_recycler();

        return vista;

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




    private void iniciar_recycler(){

        menuadapter=new Menuadapter(lstitem, new Menuadapter.OnItemClick() {
            @Override
            public void onItemClickImagen(int position) {



                Log.e("posicion",""+position);



                //  seleccionar_fragment(position);
            }
        });
        recyclerVistaItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerVistaItems.setHasFixedSize(true);
        recyclerVistaItems.setAdapter(menuadapter);



    }

}
