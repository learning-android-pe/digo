package com.example.digo_ec.service.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dateutil {
    public static  String hoy="";
    public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy HH:mm";
    public static String saber_Dia(int dia){
        /*Calendar calendar = Calendar.getInstance();
        dia =  calendar.get(Calendar.DAY_OF_WEEK);*/
        switch (dia){

               case Calendar.MONDAY:
                   hoy="Lunes";
               break;


               case Calendar.TUESDAY:
                   hoy="Martes";

                   break;

               case Calendar.WEDNESDAY:

                   hoy="Miercoles";

                   break;

               case Calendar.THURSDAY:
                   hoy="Jueves";

                   break;

               case Calendar.FRIDAY:
                   hoy="Viernes";


               break;

               case Calendar.SATURDAY:
                   hoy="Sabado";

               break;

               case Calendar.SUNDAY:
                   hoy="Domingo";
               break;


       }

       return hoy;

    }

    public static Date parseDate(String source){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return dateFormat.parse(source);
        }catch (Exception e){}

        return null;
    }
}
