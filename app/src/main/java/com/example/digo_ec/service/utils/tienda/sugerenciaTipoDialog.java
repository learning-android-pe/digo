package com.example.digo_ec.service.utils.tienda;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.digo_ec.R;
import com.example.digo_ec.service.model.SugerenciaTipo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class sugerenciaTipoDialog extends DialogFragment {


    AlertDialog Dialogo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SweetAlertDialog progressDialog;
    EditText categoria,subcategoria,opinion;
    String correo,nombre_negocio;

    public sugerenciaTipoDialog(String correo, String nombre_negocio) {
        this.correo = correo;
        this.nombre_negocio = nombre_negocio;
    }

    Button cancel, enviar;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.dialog_tipo, container, false);
        if (getDialog() != null && getDialog().getWindow() != null)
        {
            getDialog().getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.border_login));
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        categoria=view.findViewById(R.id.sugerencia_categoria);
        subcategoria=view.findViewById(R.id.sugerencia_subcategoria);
        opinion=view.findViewById(R.id.sugerencia_opinion);

        cancel=view.findViewById(R.id.cancelar_sugerencia);
        enviar=view.findViewById(R.id.enviar_sugerencia);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verificar_vacio(categoria.getText().toString()) || verificar_vacio(subcategoria.getText().toString())){
                    categoria.requestFocus();
                    subcategoria.requestFocus();
                }else {
                    envio();
                }


            }
        });


    return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);
        Dialogo = builder.create();
        Dialogo.show();
        return Dialogo;
    }




    private void mensaje_enviado(){
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                        .setContentText(getString(R.string.sugerido))
                .setContentText("Gracias por ayudarnos a mejorar !!")
                .show();
        getDialog().dismiss();
    }



    private void envio(){
        SugerenciaTipo sugerencia = new SugerenciaTipo();
        sugerencia.setCategoria(categoria.getText().toString());
        sugerencia.setSubcategoria(subcategoria.getText().toString());
        sugerencia.setMejora(opinion.getText().toString());
        sugerencia.setCorreo(this.correo);
        sugerencia.setNombre_negocio(this.nombre_negocio);


        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));

        int hora = cal2.get(Calendar.HOUR_OF_DAY);
        int minutos = cal2.get(Calendar.MINUTE);



        sugerencia.setFecha(cal2.getTime());
        SimpleDateFormat format2  =new SimpleDateFormat("kk:mm");

        sugerencia.setFechita(format2.format(sugerencia.getFecha()));


        db.collection("SugerenciaCategoria").document(""+sugerencia.getFecha()).set(sugerencia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //  Toast.makeText(getContext(),"enviado",Toast.LENGTH_LONG).show();
mensaje_enviado();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


             //   Toast.makeText(getContext(), "fallo" + e, Toast.LENGTH_LONG).show();

                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Alerta")
                        .setContentText("No se envio sugerencia")
                        .show();
            }
        });

    }


    private Boolean verificar_vacio(String texto) {


        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
           Snackbar.make(view,"Se necesita la Categoria y Subcategoria",Snackbar.LENGTH_LONG).show();
            return true;
        }


        return false;
    }

}
