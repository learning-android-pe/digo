package com.example.digo_ec.view.ui


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.digo_ec.R
import com.example.digo_ec.service.model.Usuario
import com.example.digo_ec.service.utils.Global
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_registro_digo.*

class registroUsuario : AppCompatActivity() {

    var myDialog: Dialog? = null
    var uri_para_subir: Uri? = null
    var firebaseauth= FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    lateinit var credenciales:CircularProgressButton
    lateinit var datos:CircularProgressButton
    lateinit var fotos:CircularProgressButton

    lateinit var perfil: RoundedImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_digo)
        animacion_cargando()
        click()
    }


    private fun animacion_cargando() {
        myDialog = Dialog(this@registroUsuario, R.style.NewDialog)

        myDialog?.setContentView(R.layout.layout_registro_digo)

        credenciales = myDialog?.findViewById(R.id.btn_credenciales)as CircularProgressButton
        datos = myDialog?.findViewById(R.id.btn_datos) as CircularProgressButton
        fotos = myDialog?.findViewById(R.id.btn_foto)as CircularProgressButton

        perfil=myDialog?.findViewById(R.id.perfil_registro)as RoundedImageView

        //  myDialog = new Dialog(LoginActivity.this);
        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog?.setCancelable(false)

    }





    private fun click(){


        btn_registro_digo.setOnClickListener {view->

           comprobar_campos(view)

        }


        registro_perfil.setOnClickListener {


             funcion_cortar()
        }
    }



    private fun comprobar_campos(view: View){


        if(verificar_vacio(registro_nombre.text.toString())) registro_nombre.requestFocus()

        else if(verificar_vacio(registro_apellido.text.toString())) registro_apellido.requestFocus()

        else if(verificar_vacio(registro_email.text.toString())) registro_email.requestFocus()

        else if(verificar_vacio(registro_pass.text.toString())) registro_pass.requestFocus()

        else if(tamaño_texto(registro_pass) )

        else if(verificar_vacio(registro_repass.text.toString())) registro_repass.requestFocus()

        else if(!registro_pass.text.toString().equals(registro_repass.text.toString())){
            Snackbar.make(view, getString(R.string.no_coinciden_contra), Snackbar.LENGTH_LONG).show()

        }else if(uri_para_subir==null){
            registro_perfil.requestFocus()
            Snackbar.make(view, "Se necesita una Foto de Perfil ", Snackbar.LENGTH_LONG).show()

        }else{

animacion_registro()
            registrar_user_pass(registro_email.text.toString(),registro_pass.text.toString(),view)
        }

    }



    private fun verificar_vacio(texto:String): Boolean {


        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            return true
        }


        return false
    }

    private fun tamaño_texto(texto: EditText):Boolean{


        if (texto.text.toString().length<8) {

            texto.setError((getString(R.string.error_clave_corta)))
            return true
        }
        return false
    }


    fun funcion_cortar() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@registroUsuario)
    }

    private fun llenar_subida() {


        Glide
                .with(this@registroUsuario)
                .load(uri_para_subir)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.override(106, 106)
                .into(registro_perfil)
    }


    private fun registrar_user_pass(correo: String, contraseña: String, view: View) {
        firebaseauth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {

                        credenciales.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_check))


                        registrar_datos_firestore(correo,view)
                    } else {

                        credenciales.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))
                        datos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))
                        fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))
                        Handler().postDelayed({



                            myDialog?.dismiss()
                            revertir()


                        }, 1500)

                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Snackbar.make(view, "Este correo ya esta registrado", Snackbar.LENGTH_LONG).show()
                        } else { //Todo fin de la animacion
                            Snackbar.make(view, "Existe algun problema con el correo", Snackbar.LENGTH_LONG).setAction("Ver") { Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_LONG).show() }.show()
                        }
                    } ///Todo fin del else mayor
                })
    }


    private fun registrar_datos_firestore(correo:String,view: View) {
        val user1 = Usuario()
        user1.nombre = registro_nombre.text.toString()
        user1.apellido = registro_apellido.text.toString()
        user1.foto_perfil = ""
        user1.correo = correo
        user1.isTiene_tienda = false
        user1.monedas = 0


        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user1.token = task.result!!.token
                        db.collection("USUARIOS").document(correo).set(user1).addOnSuccessListener(OnSuccessListener<Void?> {
                            datos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_check))


                            Global.usuario = user1

                            uri_para_subir?.let { it1 -> subirfoto(correo, it1,view) }

                        }).addOnFailureListener(OnFailureListener {

                            datos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))
                            fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))

                            //todo fin de la animacion
                            Handler().postDelayed({



                                myDialog?.dismiss()
                                revertir()


                            }, 1500)
                            Snackbar.make(view, getString(R.string.no_se_registro), Snackbar.LENGTH_LONG).setAction("ver") {
                                Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_LONG).show() }.show()
                        })
                    }
                }.addOnFailureListener { e ->
                    //todo fin de la animacion
                    datos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))
                    fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))

                    //todo fin de la animacion
                    Handler().postDelayed({



                        myDialog?.dismiss()
                        revertir()


                    }, 1500)



                    Snackbar.make(view, getString(R.string.no_se_registro), Snackbar.LENGTH_LONG).setAction("ver") {
                        //Todo al separar la variable task no era accesible remplace por la variabl execption
                        //   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                    }.show()
                }
    }


    fun subirfoto(correo: String, imagen_subir_uri: Uri,view: View) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val nombreFoto: String = correo+"_"+ imagen_subir_uri.lastPathSegment
      val riversRef = mStorageRef.child("imagenes_perfil_usuarios").child(nombreFoto)

      //  val riversRef = mStorageRef.child("imagenes_perfil_usuarios/" + Global.usuario.foto_perfil_paraEliminar)

        //add file on Firebase and got Download Link
        riversRef.putFile(imagen_subir_uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downUri = task.result//url de foto

            llenar_foto(downUri.toString(),correo)


            }
        }.addOnFailureListener {



            fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_no_check))

            //todo fin de la animacion
            Handler().postDelayed({



                myDialog?.dismiss()
                revertir()


            }, 1000)

            Snackbar.make(view, getString(R.string.algoFalloFoto), Snackbar.LENGTH_LONG).setAction("ver") {
                //Todo al separar la variable task no era accesible remplace por la variabl execption
                //   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(applicationContext, "Puede iniciar sesion con normalidad", Toast.LENGTH_LONG).show()
            }.show()
            // Handle unsuccessful uploads
            Toast.makeText(applicationContext, getString(R.string.algoFalloFoto), Toast.LENGTH_LONG).show()
        }
    }





    private fun llenar_foto(foto:String,correo: String){

        db.collection("USUARIOS").document(correo)
                .update("foto_perfil", foto).addOnCompleteListener {


                    Global.usuario.foto_perfil=foto



                Glide
                            .with(this@registroUsuario)
                            .load(foto)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.override(106, 106)
                            .into(perfil)
                    fotos.doneLoadingAnimation(Color.parseColor("#00b347"), BitmapFactory.decodeResource(resources,R.drawable.login_check))

                  //  Toast.makeText(applicationContext, getString(R.string.fotosubidaexito), Toast.LENGTH_LONG).show()
                    Handler().postDelayed({



                        launch_main()


                    }, 2000)

                }.addOnFailureListener {

                    Toast.makeText(applicationContext, getString(R.string.algoFalloFoto), Toast.LENGTH_LONG).show()
                launch_main()

                }
    }



private fun animacion_registro(){

    myDialog?.show()
    credenciales?.startAnimation()
    datos?.startAnimation()
    fotos?.startAnimation()

}

 private fun revertir(){
// todo dejar en estado originsl el boton
     credenciales?.revertAnimation()
     datos?.revertAnimation()
     fotos?.revertAnimation()


 }



    private fun launch_main(){

        val i = Intent(this@registroUsuario, Principal::class.java)
        startActivity(i)
        finish()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                uri_para_subir = result.uri
                Log.e("obtuve imagen", "" + uri_para_subir)
                llenar_subida()
                //    fotoanterior=Global.usuario.getFoto_perfil_paraEliminar();
//      nombreFotoBDModif=Global.usuario.getCorreo()+"_"+String.valueOf( (int)(Math.random() * 1000000 + 1));
//  Global.usuario.setFoto_perfil_paraEliminar(nombreFotoBDModif);
//subirfoto();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("error imagen", result.error.toString())
            }
        }
    }


    override fun onBackPressed() {

        val intent = Intent(this@registroUsuario, Principal::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
