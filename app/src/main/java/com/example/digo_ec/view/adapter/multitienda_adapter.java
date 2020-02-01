package com.example.digo_ec.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.digo_ec.R;
import com.example.digo_ec.service.model.Tienda;

import java.util.List;

public class multitienda_adapter extends RecyclerView.Adapter<multitienda_adapter.MultiHolder> {



    List<Tienda> lstvistas_tienda;
    Context contex;
    FragmentManager fragmentManager;

    public multitienda_adapter(List<Tienda> lstvistas_tienda) {
        this.lstvistas_tienda = lstvistas_tienda;
    }

    public multitienda_adapter(List<Tienda> lstvistas_tienda, FragmentManager fragmentManager) {
        this.lstvistas_tienda = lstvistas_tienda;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multitienda,parent,false);


        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        MultiHolder th= new MultiHolder(view);
        return th;    }

    @Override
    public void onBindViewHolder(@NonNull MultiHolder holder, int position) {
        holder.nombre.setText(""+lstvistas_tienda.get(position).getNombre());
        Glide
                .with(holder.imagen.getContext())
                .load(""+lstvistas_tienda.get(position).getImagen())
                // .override(60,60)

                //   .centerCrop()
                .placeholder(R.drawable.user_imagen)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("click","Tienda");
    }
});

    }

    @Override
    public int getItemCount() {
        return lstvistas_tienda.size();
    }

    public class MultiHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre;
        public MultiHolder(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.multi_foto);
            nombre=itemView.findViewById(R.id.multi_nombre);
        }
    }
}
