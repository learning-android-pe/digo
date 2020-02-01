package com.example.digo_ec.service.utils;

import android.util.Log;

import com.example.digo_ec.service.model.Usuario;

public class Global {


    public static Usuario usuario = new Usuario();
    public static void logErrorMessage(String Tag,String errorMessage) {
        Log.e(Tag, errorMessage);
    }
}
