package com.example.digo_ec.service.utils.tienda;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.digo_ec.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

public class cambioDireccionExacta extends DialogFragment {
    AlertDialog Dialogo;

    EditText direccionExacta;
    Button cancel, enviar;
    View view;
    OnItemEnviar onItemEnviar;

    String direccion;
    String direccion_Exacta;




    public cambioDireccionExacta(OnItemEnviar onItemEnviar, String direccion) {
        this.onItemEnviar = onItemEnviar;
        this.direccion = direccion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_direccion, container, false);
        if (getDialog() != null && getDialog().getWindow() != null)
        {
            getDialog().getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.border_login));
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        direccionExacta=view.findViewById(R.id.direccion_exacta);
        cancel=view.findViewById(R.id.cancelar_direccion);
        enviar=view.findViewById(R.id.enviar_direccion);


        direccionExacta.setText(direccion);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direccion_Exacta=direccionExacta.getText().toString();

                if(direccion.equals(direccion_Exacta))
                Snackbar.make(view,"No se a cambiado la dirección",Snackbar.LENGTH_LONG).show();
                else {
                    onItemEnviar.onItemClickEnviar(direccion_Exacta);
                    dismiss();

                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

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

    public interface  OnItemEnviar{
        void onItemClickEnviar( String dir);
    }
}
