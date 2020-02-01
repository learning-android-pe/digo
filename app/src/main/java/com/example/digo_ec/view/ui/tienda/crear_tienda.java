package com.example.digo_ec.view.ui.tienda;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.digo_ec.R;
import com.example.digo_ec.service.Api.ApiService;
import com.example.digo_ec.service.Api.Retrofitcliente;
import com.example.digo_ec.service.model.ApiMaps.AddressComponent;
import com.example.digo_ec.service.model.ApiMaps.Example;
import com.example.digo_ec.service.model.Categorias;
import com.example.digo_ec.service.model.Horarios;
import com.example.digo_ec.service.model.Tienda;
import com.example.digo_ec.service.utils.AppUtils;
import com.example.digo_ec.service.utils.Global;
import com.example.digo_ec.service.utils.mapas.ApiString;
import com.example.digo_ec.service.utils.mapas.MapDialogFragment;
import com.example.digo_ec.service.utils.tienda.cambioDireccionExacta;
import com.example.digo_ec.service.utils.tienda.sugerenciaTipoDialog;
import com.example.digo_ec.view.adapter.seleccionDias_adapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class crear_tienda extends Fragment {

    private GoogleMap gmap;
    public String correo_usuario = "carlos.aforever@gmail.com";

    public crear_tienda() {
        // Required empty public constructor
    }

    Retrofit retrofit;
    ApiService retrofitApi;
    StorageReference mStorageRef;
    String calle = "Dirección no especifica";
    Spinner categoria, subcategoria;
    MapDialogFragment map;
    List<Horarios> lst_horarios = new ArrayList<>();
    LatLng nuevo = null;
    public String horadeapertura;
    public String horadecierre;
    Button apertura, cierre;
    Dialog myDialog;
    LinearLayout ubicacion, layout_direccion;
    TextView presentacion_dirección;

    View vista;
    EditText nombredenegocio, cedula, telefono, descripcion;
    CountryCodePicker codigo_pais;
    ImageView portada;
    CircleImageView perfil, soporte, cambioDireccion;
    Button registrar;
    String numeroTelefono = "";
    int eleccion = 0;
    Uri imagen_portada, imagen_perfil;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Tienda tienda = new Tienda();

    String categoria_elegida = "";
    String subcategoria_elegida = "";

    SweetAlertDialog progressDialog;
    ///
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private ListenerRegistration categoriesListener =null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_tienda, container, false);


        requestQueue = Volley.newRequestQueue(getContext());


        UI();

        clicks();

        iniciar_lista();
        iniciar_recycler();
        categorias2();


        return vista;
    }


    private void consulta() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //  Log.e("resultado","hey"+response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);


                        //      Log.e("descompuesto",jsonObject.optString("address_components"));


                        JSONArray jsonArray2 = new JSONArray(jsonObject.optString("address_components"));

                        for (int i = 0; i < jsonArray2.length(); i++) {
                            try {
                                JSONObject json = jsonArray.getJSONObject(i);
                                //Aquí se obtiene el dato y es guardado en una lista
                                if (i == 0) {
                                    //                   Log.e("recorro","bueno"+json.toString());

                                }

                                //             Log.e("recorro",json.getString("address_components"));
////                                Log.e("recorro",json.getString("short_name"));
////                                Log.e("recorro",json.getString("types"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        // Do something...
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        String llave = "AIzaSyBrqlSYGXnmHVsp8yw90cnmi7GvGgjxB50";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng="
                + nuevo.latitude + "," + nuevo.longitude + "&key=" + llave;

//AIzaSyBrqlSYGXnmHVsp8yw90cnmi7GvGgjxB50
        //Todo prestado

        // String url2="https://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=-2.179710,%20-79.821858&key=AIzaSyBrqlSYGXnmHVsp8yw90cnmi7GvGgjxB50";
        JsonObjectRequest request = new JsonObjectRequest(url, null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "error" + error.toString());
            }
        });
        requestQueue.add(request);

    }


    private void consulta_retrofit() {
        retrofit = Retrofitcliente.getInstance();
        retrofitApi = retrofit.create(ApiService.class);
        Disposable disposable;


        String lat = "" + nuevo.latitude + "," + nuevo.longitude;

        //Logger.addLogAdapter(new AndroidLogAdapter());
        disposable = retrofitApi.traerGeo2(true, lat, "AIzaSyBrqlSYGXnmHVsp8yw90cnmi7GvGgjxB50")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Example>() {
                    @Override
                    public void onNext(Example respuesta) {
                        Log.e("Carlos Caguana", "" + respuesta.getResults().get(0).getAddressComponents().get(1).getLongName());
                        Log.e("Carlos Caguana: ", respuesta.getResults().get(0).getAddressComponents().get(2).getShortName());
                        Log.e("Carlos Caguana: ", respuesta.getResults().get(0).getFormattedAddress());


                        calle = respuesta.getResults().get(0).getFormattedAddress();
                        tienda.setDireccion(calle);

                        //todo cambio
                        for (AddressComponent address : respuesta.getResults().get(0).getAddressComponents()) {


                            for (String s : address.getTypes()) {
                                Log.e("Carlin:", "---" + s);


                                datos_ubicacion(s, address.getLongName());
                            }


                            if (address.getTypes().contains(ApiString.CIUDADELA3) || address.getTypes().contains(ApiString.CIUDADELA4)) {
                                tienda.setCiudad(address.getLongName());
                            } else if (address.getTypes().contains(ApiString.CIUDADELA) || address.getTypes().contains(ApiString.CIUDADELA2)) {
                                tienda.setCiudad(address.getLongName());
                            }


                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Ciudadela", "-" + tienda.getCiudadela());
                        Log.e("Provincia", tienda.getProvincia());
                        Log.e("Calle", "-" + tienda.getCalle());
                        Log.e("Ciudad", tienda.getCiudad());
                        Log.e("Pais", tienda.getPais());

                        presentacion_dirección.setText(calle);

                    }
                });

    }


    private void datos_ubicacion(String tipo, String dato) {

        switch (tipo) {

            case ApiString.PROVINCIA:
                tienda.setProvincia(dato);
                break;

            case ApiString.CIUDAD:
                tienda.setCiudad(dato);
                break;

            case ApiString.CALLE:
                tienda.setCalle(dato);
                break;

            case ApiString.PAIS:
                tienda.setPais(dato);

                break;
        }

//    tienda.setCiudad(yourCiudadela);
    }


    private void UI() {
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#2583ef"));
        map = new MapDialogFragment(new MapDialogFragment.OnItemOk() {
            @Override
            public void onItemClickok(LatLng position) {

                nuevo = position;

                if (nuevo != null) {

                    nuevo = new LatLng(-2.1903639, -79.8866216);
                    ubicacion.setBackground(getResources().getDrawable(R.drawable.border_verde));
                    presentacion_dirección.setText(calle);
                    layout_direccion.setVisibility(View.VISIBLE);
                    consulta_retrofit();
                   /* new AsyncTask<Void, Void, Boolean>() {


                        @Override
                        protected Boolean doInBackground( Void... voids ) {
                            //Do things...
                               direccion();
                               consulta();

                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if(aBoolean){
                                presentacion_dirección.setText(calle);

                            }
                        }
                    }.execute();
*/
                    //  presentacion_dirección.setText(calle);  esto esta en layout direccion

                }
                Log.e("obtengo", "position");
            }
        });


        ubicacion = vista.findViewById(R.id.layout_ubicacion_registro);
        apertura = vista.findViewById(R.id.btn_horaApertura);
        cierre = vista.findViewById(R.id.btn_horaCierre);
        nombredenegocio = vista.findViewById(R.id.nombre_negocio);
        cedula = vista.findViewById(R.id.cedula_registro);
        codigo_pais = vista.findViewById(R.id.ccp);
        telefono = vista.findViewById(R.id.telefono_registro);
        portada = vista.findViewById(R.id.portada);
        perfil = vista.findViewById(R.id.perfil);
        cambioDireccion = vista.findViewById(R.id.cambio_direccion);

        Glide.with(this).load(R.drawable.zeus).into(portada);
        Glide.with(this).load(R.drawable.zeus).into(perfil);


        descripcion = vista.findViewById(R.id.descripcion_tienda_registro);
        registrar = vista.findViewById(R.id.registrar_tienda);
        categoria = vista.findViewById(R.id.spn_categoria);
        subcategoria = vista.findViewById(R.id.spn_subCategoria);
        soporte = vista.findViewById(R.id.soporte_tipo);


        layout_direccion = vista.findViewById(R.id.layout_dirección);
        presentacion_dirección = vista.findViewById(R.id.presentacion_dirección);
    }

    private void clicks() {


        apertura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_hora_abrir();
            }
        });

        cierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_hora_cerrar();
            }
        });
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagen_perfil != null)
                    map.fotito = imagen_perfil;
                if (nuevo != null) {
                    Log.e("cambio", "ubicacion");
                    map.nuevo = nuevo;
                }


                map.show(getFragmentManager(), "simple dialog");


            }
        });

        cambioDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioDireccionExacta dir = new cambioDireccionExacta(new cambioDireccionExacta.OnItemEnviar() {
                    @Override
                    public void onItemClickEnviar(String dir) {
                        Log.e("Direccion Exacta", "" + dir);
                        tienda.setDireccionExacta(dir);
                        presentacion_dirección.setText(dir);
                        layout_direccion.setBackground(getResources().getDrawable(R.drawable.border_verde));


                    }
                }, calle);

                dir.show(getFragmentManager(), "cambio Direccion");
            }
        });


        portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccion = 1;
                funcion_cortar();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccion = 2;
                funcion_cortar();
            }
        });


        soporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sugerenciaTipoDialog sugerenciaDialog = new sugerenciaTipoDialog(correo_usuario, nombredenegocio.getText().toString());
                sugerenciaDialog.show(getFragmentManager(), sugerenciaTipoDialog.class.getSimpleName());

            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_campos();
            }
        });


    }


    private void iniciar_lista() {
        lst_horarios.add(new Horarios(2, "", "", false));//Lunes
        lst_horarios.add(new Horarios(3, "", "", false));//Martes
        lst_horarios.add(new Horarios(4, "", "", false));//Miercoles
        lst_horarios.add(new Horarios(5, "", "", false));//Jueves
        lst_horarios.add(new Horarios(6, "", "", false));//Viernes
        lst_horarios.add(new Horarios(7, "", "", false));//Sabado
        lst_horarios.add(new Horarios(1, "", "", false));//Domingo
    }

    private void iniciar_recycler() {
        RecyclerView recyclerView = vista.findViewById(R.id.recycler_horario);
        seleccionDias_adapter adapter = new seleccionDias_adapter(lst_horarios, new seleccionDias_adapter.OnItemClicHora() {
            @Override
            public void onItemClickHora(int position) {
                Log.e("obtengo", "hora");

                if (horadeapertura == null || horadecierre == null) {
                    Snackbar.make(vista, "Eliga una Hora de Apertura y Cierre", Snackbar.LENGTH_LONG).show();

                } else {

                    lst_horarios.get(position).setApertura(horadeapertura);
                    lst_horarios.get(position).setCierre(horadecierre);
                    lst_horarios.get(position).setAtencion(true);

                }

            }
        }, new seleccionDias_adapter.OnItemClicDeshabilitar() {
            @Override
            public void onItemClickDeshabilitar(int position) {

                lst_horarios.get(position).setAtencion(false);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    public void select_hora_abrir() {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                if (minute < 10 || hourOfDay < 10) {
                    if (minute < 10 && hourOfDay < 10)
                        horadeapertura = ("0" + hourOfDay + ":0" + minute);
                    else if (hourOfDay < 10 && minute > 10)
                        horadeapertura = ("0" + hourOfDay + ":" + minute);
                    else
                        horadeapertura = (hourOfDay + ":0" + minute);

                } else
                    horadeapertura = (hourOfDay + ":" + minute);
                apertura.setText(horadeapertura);


            }
        }, hora, minutos, true);
        timePickerDialog.show();
    }

    public void select_hora_cerrar() {

        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (minute < 10 || hourOfDay < 10) {
                    if (minute < 10 && hourOfDay < 10)
                        horadecierre = ("0" + hourOfDay + ":0" + minute);
                    else if (hourOfDay < 10 && minute > 10)
                        horadecierre = ("0" + hourOfDay + ":" + minute);
                    else
                        horadecierre = (hourOfDay + ":0" + minute);

                } else
                    horadecierre = (hourOfDay + ":" + minute);

                cierre.setText(horadecierre);
            }
        }, hora, minutos, true);
        timePickerDialog.show();

    }


    private void validar_campos() {

        if (verificar_vacio(nombredenegocio.getText().toString())) nombredenegocio.requestFocus();
        else if (verificar_vacio(cedula.getText().toString())) cedula.requestFocus();
        else if (cedula.getText().toString().length() < 10) {
            cedula.setError("Se requiere 10 digitos en la Cedula");
            cedula.requestFocus();
            //Snackbar.make(vista,"Se requiere 10 digitos en la Cedula",Snackbar.LENGTH_LONG).show();
        } else if (verificar_vacio(telefono.getText().toString())) telefono.requestFocus();
        else if (verificar_vacio(descripcion.getText().toString())) descripcion.requestFocus();
        else if (imagen_perfil == null) {
            mensaje(1);
        } else if (imagen_portada == null) {
            mensaje(2);

        } else if (nuevo == null) {
            Snackbar.make(vista, getString(R.string.noUbicacion), Snackbar.LENGTH_LONG).show();

        } else if (telefono.getText().toString().substring(0, 1).equals("0")) {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + telefono.getText().toString().trim().substring(1);

            llenar_datos();
        } else {
            numeroTelefono = codigo_pais.getSelectedCountryCode() + telefono.getText().toString().trim();
            llenar_datos();
        }


    }


    private void llenar_datos() {
        tienda.setNombre(nombredenegocio.getText().toString());
        tienda.setLat(nuevo.latitude);
        tienda.setLng(nuevo.longitude);
        tienda.setCategoria(categoria_elegida);
        tienda.setCedula(cedula.getText().toString());
        tienda.setSubcategoria(categoria_elegida);
        tienda.setDescripcion(descripcion.getText().toString());
        tienda.setPropietario(this.correo_usuario);
        //   tienda.setPropietario("carlos.aforever@gmail.com");
        tienda.setTelefono(numeroTelefono);
        int x = 0;
        tienda.setId(tienda.getPropietario() + "_tienda_" + x);


        List<String> trabajo = new ArrayList<>();
        for (Horarios n : lst_horarios) {
            trabajo.add(n.fusion());
        }

        tienda.setListado_horario(trabajo);

        crear_tienda();
    }


    private void mensaje(int elegir) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¡OH!");

        if (elegir == 1) {
            builder.setMessage(getString(R.string.noHaPuestoFoto));

        } else {
            builder.setMessage(getString(R.string.noHaPuestoPortada));

        }


        builder.setPositiveButton(getString(R.string.entiendo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private Boolean verificar_vacio(String texto) {


        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            Snackbar.make(vista, getString(R.string.todosLosdatosObligatorios), Snackbar.LENGTH_LONG).show();
            return true;
        }


        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {

                if (eleccion == 1) {
                    imagen_portada = result.getUri();

                } else {
                    imagen_perfil = result.getUri();

                }


                llenar_foto(eleccion);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void llenar_foto(int elegir) {

        if (elegir == 1) {
            Glide
                    .with(getContext())
                    .load(imagen_portada)
                    .error(R.drawable.user_imagen)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(portada);
        } else {
            Glide
                    .with(getContext())
                    .load(imagen_perfil)
                    .error(R.drawable.user_imagen)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(perfil);
        }


    }


    public void funcion_cortar() {
        CropImage.activity()
                .start(getContext(), this);
    }


    //https://firebase.google.com/docs/firestore/query-data/listen
    //Agregar listener para escuchar los cambios en una query
    private void categorias2() {
        Log.e("voy", "consultar");
        Query categoriesQuery =  db.collection("Categorias").orderBy("nombre_categoria");
        categoriesListener= categoriesQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots,FirebaseFirestoreException e) {
                Log.v("CONSOLE", "queryDocumentSnapshots "+queryDocumentSnapshots+"e "+e);
                if(e!=null){
                    Log.v("CONSOLE", "Error "+e);
                    return;
                }
                for (DocumentSnapshot s : queryDocumentSnapshots) {
                    Categorias cate = new Categorias();
                    //  Log.e("query",""+s.getData());
                    cate = s.toObject(Categorias.class);
                    categorias.add(cate);
                    nombre_cate.add(cate.getNombre_categoria());


                    //    Log.e("clase",cate.getNombre_categoria());
                }


                adapter();
            }
        });
                /*.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot s : queryDocumentSnapshots) {
                    Categorias cate = new Categorias();
                    //  Log.e("query",""+s.getData());
                    cate = s.toObject(Categorias.class);
                    categorias.add(cate);
                    nombre_cate.add(cate.getNombre_categoria());


                    //    Log.e("clase",cate.getNombre_categoria());
                }


                adapter();


            }
        });*/


    }


    List<Categorias> categorias = new ArrayList<>();
    List<String> nombre_cate = new ArrayList<>();
    List<String> nombre_subcate = new ArrayList<>();

    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<String> spinnerArrayAdapter2;

    private void adapter() {
    /*try {
        spinnerArrayAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombre_subcate);

    }catch (Exception e){

        Log.e("hey","ni idea"+e.toString());

    }*/
    //verificar si el fragment esta dentro un activity
        if(getActivity()==null || isRemoving() || isDetached()){
            return;
        }
        spinnerArrayAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombre_subcate);

        if (spinnerArrayAdapter2 == null) {

            Log.e("hey", "nulo");
        } else {

            Log.e("hey", "no nulo");

        }
/*
    spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombre_cate);

    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

    categoria.setAdapter(spinnerArrayAdapter);




    spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_checked);
 //   spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    subcategoria.setAdapter(spinnerArrayAdapter2);




    categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e("spinner","cate"+position);

             llenar(position);
             spinnerArrayAdapter2.notifyDataSetChanged();
             categoria_elegida=nombre_cate.get(position);



        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


    subcategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e("spinner","nombre_subcate"+position);
            subcategoria_elegida= nombre_subcate.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });*/


    }

    public void llenar(int position) {
        nombre_subcate.clear();


        for (String c : categorias.get(position).getSubcategoria()) {
            nombre_subcate.add(c);

        }

        //  nombre_subcate=categorias.get(position).getSubcategoria();
    }

    private void direccion() {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if (geocoder.isPresent())
            Log.e("GEOCODER", "Geocoder presente!");
        else
            Log.e("GEOCODER", "Geocoder no soportado");

        Geocoder geocoder2;
        List<Address> yourAddresses;
        geocoder2 = new Geocoder(getActivity(), Locale.getDefault());
        try {
            yourAddresses = geocoder2.getFromLocation(nuevo.latitude, nuevo.longitude, 1);
            if (yourAddresses.size() > 0) {
                String yourAddress = yourAddresses.get(0).getAddressLine(0);
                String yourCiudadela = yourAddresses.get(0).getLocality();
                String yourCiudad = yourAddresses.get(0).getSubLocality();

                String yourCountry = yourAddresses.get(0).getCountryName();

                Log.e("direccion", "" + yourAddress);
                Log.e("city", "" + yourCiudad);
                Log.e("ciudadela", "" + yourCiudadela);
                Log.e("country", "" + yourCountry);


                Log.e("admin area", "" + yourAddresses.get(0).getAdminArea());
                Log.e("Feature Name", "" + yourAddresses.get(0).getFeatureName());
                Log.e("Postal Code", "" + yourAddresses.get(0).getPostalCode());
                Log.e("Premises", "" + yourAddresses.get(0).getPremises());

                calle = yourAddress;
                tienda.setPais(yourCountry);
                tienda.setDireccion(yourAddress);
                tienda.setCiudad(yourCiudadela);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("direccion", "-" + e.toString());

        }


     /*   try {
            List<Address> addresses = geocoder.getFromLocation(nuevo.latitude, nuevo.latitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.e("GEOCODER", "-----"+addresses.size());

            if(addresses != null && addresses.size()>0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String knownName2 = addresses.get(0).getPremises();
                String knownName3 = addresses.get(0).getSubAdminArea();
                String city2 = addresses.get(0).getSubLocality();
                String knownName5 = addresses.get(0).getSubThoroughfare();


                Log.e("lugar","-"+address);
                calle=address;
                presentacion_dirección.setText(calle);


                Log.e("lugar","-"+city);
                Log.e("lugar","-"+state);
                Log.e("lugar","-"+country);
                Log.e("lugar","-"+postalCode);
                Log.e("lugar","-"+knownName);
                Log.e("lugar","-"+knownName2);
                Log.e("lugar","-"+knownName3);
                Log.e("lugar","-"+city2);
                Log.e("lugar","-"+knownName5);

                //Todo direccion de la pincheta
                tienda.setPais(country);
                tienda.setDireccion(address);
                tienda.setCiudad(city2);

            }else{
                Log.e("lugar","estoy nulo ");

            }




        } catch (IOException e) {
            e.printStackTrace();
            Log.e("lugar","-"+e.toString());

        }
*/

    }


    private void crear_tienda() {
        progressDialog.setTitleText(getString(R.string.subiendoDatos));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("voy", "crear tienda");

        db.collection("TIENDAS").document(tienda.getId()).set(tienda).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                progressDialog.dismissWithAnimation();
                Toast.makeText(getContext(), getString(R.string.tiendaRegistradaExitosamente), Toast.LENGTH_LONG).show();

                Log.e("registro", "tienda");
                List<String> tiendasuser = new ArrayList<>();
                if (Global.usuario.getListado_tiendas() != null)
                    tiendasuser.addAll(Global.usuario.getListado_tiendas());

                tiendasuser.add(tienda.getId());


                geo_query();
                actualizar_datos_usuario(tiendasuser, 1);
                registrarFoto(imagen_perfil, 1);
                registrarFoto(imagen_portada, 2);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.alerta))
                        .setContentText(getString(R.string.no_se_registro))
                        .show();
            }
        });
    }


    private void geo_query() {
        CollectionReference colle = db.collection("TIENDAS");
        GeoFirestore fuego = new GeoFirestore(colle);
        //     fuego.setLocation(tienda.getId(), new GeoPoint(nuevo.latitude, nuevo.longitude));

        fuego.setLocation(tienda.getId(), new GeoPoint(nuevo.latitude, nuevo.longitude), new GeoFirestore.CompletionCallback() {
            @Override
            public void onComplete(@Nullable Exception e) {
                Log.e("registro", "geo firestore actualizado");

            }
        });


    }

    private void actualizar_datos_usuario(List<String> tiendasuser, int creacion) {
        db.collection("USUARIOS").document(correo_usuario)
                .update("listado_tiendas", tiendasuser, "creados", creacion, "tiene_tienda", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Log.e("registro", "usuario actualizado");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  Snackbar.make(vista,"Se requiere 10 digitos en la Cedula",Snackbar.LENGTH_LONG).show();

                //  Toast.makeText(ge(), getString(R.string.algoFalloFoto), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void actualizar_foto(final String nombre, String image) {
        db.collection("TIENDAS").document(tienda.getId())
                .update(nombre, image).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("actualizar registro", "tienda " + nombre);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  Snackbar.make(vista,"Se requiere 10 digitos en la Cedula",Snackbar.LENGTH_LONG).show();

                //  Toast.makeText(ge(), getString(R.string.algoFalloFoto), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void registrarFoto(Uri uri_para_subir, final int eleccion) {
        AppUtils.verificaSD_Directory("/Digo_images/");
        if (!AppUtils.bool_sdAccesoEscritura || !AppUtils.bool_sdDisponible)
            Toast.makeText(getContext(),
                    "Problemas con la Memoria Externa", Toast.LENGTH_SHORT).show();
        if (!AppUtils.bool_dirExist)
            Toast.makeText(getActivity(),
                    "No se puede crear el Directorio de imagenes", Toast.LENGTH_SHORT).show();
        String nombreFoto = "";
        if (eleccion == 1)
            nombreFoto = tienda.getId() + "_perfil_" + uri_para_subir.getLastPathSegment();

        else if (eleccion == 2)
            nombreFoto = tienda.getId() + "_portada_" + uri_para_subir.getLastPathSegment();


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


        final StorageReference riversRef = mStorageRef.child("imagenes_tienda").child(nombreFoto);

        riversRef.putFile(uri_para_subir).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();


                    if (eleccion == 1) {
                        actualizar_foto("imagen", (downUri.toString()));
                    } else if (eleccion == 2) {
                        actualizar_foto("portada", (downUri.toString()));
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismissWithAnimation();
                        Toast.makeText(getContext(), getString(R.string.algoFallosubirFoto), Toast.LENGTH_LONG).show();

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(categoriesListener!=null){
            categoriesListener.remove();
        }
    }
}
