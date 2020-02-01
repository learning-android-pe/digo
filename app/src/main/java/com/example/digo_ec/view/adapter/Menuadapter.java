package com.example.digo_ec.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digo_ec.R;
import com.example.digo_ec.service.model.Item_menu;

import java.util.List;

public class Menuadapter extends RecyclerView.Adapter<Menuadapter.MenuHolder> {

    int selectedPosition=0;

    List<Item_menu> lstitem;
    FragmentManager fragmentManager;
    Menuadapter.OnItemClick itemClick;

    public Menuadapter(List<Item_menu> lstitem, OnItemClick itemClick) {
        this.lstitem = lstitem;
        this.itemClick = itemClick;
    }

    public Menuadapter(List<Item_menu> lstitem, FragmentManager fragmentManager) {
        this.lstitem = lstitem;
        this.fragmentManager = fragmentManager;
    }

    public Menuadapter(List<Item_menu> lstitem) {
        this.lstitem = lstitem;
    }

    @NonNull
    @Override
    public Menuadapter.MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,parent,false);
        MenuHolder th= new MenuHolder(view);
        return th;    }

    @Override
    public void onBindViewHolder(@NonNull Menuadapter.MenuHolder holder, final int position) {


        if(selectedPosition==position){

            holder.contenedor.setBackgroundColor(Color.parseColor("#DFDCDC"));

        }else {
            holder.contenedor.setBackgroundColor(Color.parseColor("#ffffff"));

        }




            holder.icono.setBackgroundResource(lstitem.get(position).getIcono());

        holder.nombre.setText(""+lstitem.get(position).getNombre());

        holder.descripcion.setText(""+lstitem.get(position).getDescripcion());

        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition=position;
                notifyDataSetChanged();
                itemClick.onItemClickImagen(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstitem.size();
    }




    public class MenuHolder extends RecyclerView.ViewHolder {

        ImageView icono;
        TextView nombre,descripcion;
        RelativeLayout contenedor;




        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            icono=itemView.findViewById(R.id.foto_icono);
            nombre=itemView.findViewById(R.id.nombre_texto);
            descripcion=itemView.findViewById(R.id.descripcion_texto);
            contenedor=itemView.findViewById(R.id.item_click);

        }
    }





    public interface  OnItemClick{
        void onItemClickImagen(int position);
    }
}
