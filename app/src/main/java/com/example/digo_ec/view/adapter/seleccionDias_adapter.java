package com.example.digo_ec.view.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digo_ec.R;
import com.example.digo_ec.service.model.Horarios;
import com.example.digo_ec.service.model.Tienda;
import com.example.digo_ec.service.utils.Dateutil;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class seleccionDias_adapter extends RecyclerView.Adapter<seleccionDias_adapter.DiasHolder> {

    List<Horarios> lst_horarios;
    OnItemClicHora onItemClicHora;
    OnItemClicDeshabilitar onItemClicDeshabilitar;

    public seleccionDias_adapter(List<Horarios> lst_horarios) {
        this.lst_horarios = lst_horarios;
    }

    public seleccionDias_adapter(List<Horarios> lst_horarios, OnItemClicHora onItemClicHora, OnItemClicDeshabilitar onItemClicDeshabilitar) {
        this.lst_horarios = lst_horarios;
        this.onItemClicHora = onItemClicHora;
        this.onItemClicDeshabilitar = onItemClicDeshabilitar;
    }

    @NonNull
    @Override
    public seleccionDias_adapter.DiasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dias_laboral,parent,false);

        //RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // view.setLayoutParams(layoutParams);
        DiasHolder th= new DiasHolder(view);
        return th;      }

    @Override
    public void onBindViewHolder(@NonNull seleccionDias_adapter.DiasHolder holder, final int position) {


        Calendar calendar = Calendar.getInstance();
        int  dia =  calendar.get(Calendar.DAY_OF_WEEK);

        if(dia==lst_horarios.get(position).getDia()){

            holder.dia.setTextColor(Color.parseColor("#4CAF50"));
            holder.dia.setText(Dateutil.saber_Dia(lst_horarios.get(position).getDia()) +" (hoy)");

        }else{
            holder.dia.setText(Dateutil.saber_Dia(lst_horarios.get(position).getDia()));
            holder.dia.setTextColor(Color.parseColor("#808080"));


        }



        if(lst_horarios.get(position).isAtencion()){
            holder.hora.setText(lst_horarios.get(position).getApertura()+"-"+lst_horarios.get(position).getCierre());
            holder.hora.setTextColor(Color.parseColor("#4CAF50"));

        }else{
            holder.hora.setText("Sin Atencion");
            holder.hora.setTextColor(Color.parseColor("#ff4040"));

        }

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onItemClicHora.onItemClickHora(position);
        notifyDataSetChanged();
    }
});

        holder.deshabilitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicDeshabilitar.onItemClickDeshabilitar(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst_horarios.size();
    }

    public class DiasHolder extends RecyclerView.ViewHolder {
        TextView dia , hora;
        CircleImageView deshabilitar;

        public DiasHolder(@NonNull View itemView) {
            super(itemView);

            dia= itemView.findViewById(R.id.item_dia);

            hora= itemView.findViewById(R.id.item_horas);

            deshabilitar= itemView.findViewById(R.id.item_deshabilitar);


        }
    }


    public interface  OnItemClicHora{
        void onItemClickHora( int position);
    }
    public interface  OnItemClicDeshabilitar{
        void onItemClickDeshabilitar( int position);
    }
}
