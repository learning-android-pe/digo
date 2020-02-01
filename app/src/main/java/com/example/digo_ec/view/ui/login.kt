package com.example.digo_ec.view.ui


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.digo_ec.R
import com.example.digo_ec.service.model.Usuario
import com.example.digo_ec.service.utils.Global
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class login : Fragment() {
    var myDialog: Dialog? = null
    var pantalla=false
    var dialog_permisos: SweetAlertDialog? = null
    lateinit var animacion: LottieAnimationView
    lateinit var presentacion: LinearLayout
    lateinit var perfil: CircleImageView
    var usuario_registro = Usuario()
    lateinit var rootView: View
    var firebaseauth= FirebaseAuth.getInstance()
    lateinit var callbackManager: CallbackManager
    lateinit  var mGoogleSignInClient: GoogleSignInClient
    var db = FirebaseFirestore.getInstance()
    var mAuthListenr : FirebaseAuth.AuthStateListener? = null

    var comprobar_boolean=false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_login, container, false)

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Ui()
        rebote()
        facebook()
        LoginManager.getInstance().logOut()
        google()
        cerrar_google()
        clicks()
        animacion_cargando()
        firebaseauth.signOut()
        animacion.addAnimatorUpdateListener {valueAnimator ->


            val progress = (valueAnimator.animatedValue as  Float  *  100 ) .toInt ()

            if (pantalla && progress >= 96) {
                Log.e("progresos",""+progress)
                presentacion.visibility=View.VISIBLE
                animacion.visibility=View.INVISIBLE
                animacion.pauseAnimation()
                launch_MainActivity()
            }
        }

        mAuthListenr= FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if(user != null) {
                sesion_facebook.isEnabled=false
                sesion_facebook.isEnabled=false

                Log.e("oyente ", "tengo un usuario: " +  user.email)
                if(user.email!=null)
                existe_usuario(user.email.toString(),rootView)
            }

        }

    }





    private fun Ui(){
        btn_ingresar_login_digo.visibility=View.GONE
        textInputLayout_password_login.visibility=View.GONE

    }



    private  fun clicks(){


        siguiente_login_kt.setOnClickListener{view ->
            comprobar_datos(view)
        }












        btn_ingresar_login_digo.setOnClickListener {view->

           comprobar_datos(view)
            Log.e("click","click");

        }


        registrarme_login.setOnClickListener {

            val intent = Intent(this@login.context, registroUsuario::class.java)
            startActivity(intent)
            getActivity()?.finish()
            /*val intent = Intent(this@Login_digo, Registro_digo::class.java)
            startActivity(intent)
            finish()*/
        }
    }

    private fun comprobar_datos(view:View){

        if(correo_login.text.toString().length>0){


            if(!comprobar_boolean){
                textInputLayout_password_login.visibility=View.VISIBLE
                var arriba = AnimationUtils.loadAnimation(this@login.context,R.anim.hacia_arriba)
                textInputLayout_password_login.startAnimation(arriba)
                // ingresar_login.visibility=View.VISIBLE
                // ingresar_login.startAnimation(arriba)


                btn_ingresar_login_digo.visibility=View.VISIBLE
                btn_ingresar_login_digo.startAnimation(arriba)


                siguiente_login_kt.visibility=View.INVISIBLE
            }

        }else{
            correo_login.error="ingrese el usuario"
        }


        if(comprobar_boolean && correo_login.text.length>0){
            if(password_login.text.toString().length<8){
                password_login.error="la contraseña es corta"

            }else{

                Log.e("revisar","voy")
                btn_ingresar_login_digo.startAnimation()
                revisar_datos(view,correo_login.text.toString(),password_login.text.toString())
            }

        }

        comprobar_boolean=true


    }



    private fun animacion(){
        var mover_flecha = AnimationUtils.loadAnimation(this@login.context,R.anim.hacia_derecha2)
        var mover_correo = AnimationUtils.loadAnimation(this@login.context,R.anim.hacia_izquierda2)
        textInputLayout_correo_login.startAnimation(mover_correo)
        siguiente_login_kt.startAnimation(mover_flecha)
    }



    private fun rebote(){

        var rebote = AnimationUtils.loadAnimation(this@login.context,R.anim.rebote)

        flecha_registro.startAnimation(rebote)
    }
    private fun revisar_datos( rootView:View,correo:String,pass:String){
        Log.e("revisar: ",""+correo+"--->"+pass)
        myDialog?.show()


        btn_ingresar_login_digo.isEnabled=false
        firebaseauth.signInWithEmailAndPassword(correo.trim(),pass.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("login", "exito")
                    btn_ingresar_login_digo.doneLoadingAnimation(
                        Color.parseColor("#cdcdcd"),
                        BitmapFactory.decodeResource(resources,R.drawable.login_check))

                } else {
                    Log.e("login", "fallo inicio", task.exception)
                    btn_ingresar_login_digo.doneLoadingAnimation(
                        Color.parseColor("#cdcdcd"),
                        BitmapFactory.decodeResource(resources,R.drawable.login_no_check))


                    Handler().postDelayed({

                        btn_ingresar_login_digo.revertAnimation()
                        btn_ingresar_login_digo.setBackgroundResource(R.drawable.boton_ingresar)
                        btn_ingresar_login_digo.isEnabled=true



                    }, 1200)

                    Snackbar.make(rootView, getString(R.string.fallo), Snackbar.LENGTH_LONG)
                        .setAction("VER ERROR ")
                        {  Toast.makeText(this@login.context, task.exception?.message, Toast.LENGTH_LONG).show() }.show()


                }

                // ...
            }
    }
    private fun existe_usuario(correo:String,view: View){

        //       myDialog.show();
        val docRef = db.collection("USUARIOS").document(correo)

        docRef.get().addOnSuccessListener(OnSuccessListener<DocumentSnapshot?> { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Log.e("DOCUMENTO", "existe ese usuario---------> $correo")
                generar_token(correo)
                traer_datos(correo)


            } else {

                Log.e("DOCUMENTO", "no existe -------------xxxxxxxx: $correo")

                registrar(correo,view)
            }
        })



    }
    private fun firebase_credenciales(credential: AuthCredential){

        myDialog?.show()

        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    Log.e("firebase_credencial", "exito")


                } else { // If sign in fails, display a message to the user.
                    Log.w("fire_google", "signInWithCredential:failure", task.exception)
                    myDialog?.dismiss()

                }
                // ...


            }
    }
    private fun registrar(correo: String, view:View){
        usuario_registro.correo=correo

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    usuario_registro.token = task.result?.token
                    ///db
                    db.collection("USUARIOS").document(correo).set(usuario_registro).addOnSuccessListener {
                        Log.e("registre", correo)
                        traer_datos(usuario_registro.correo)
                    }.addOnFailureListener { e ->
                        Snackbar.make(view, getString(R.string.no_se_registro), Snackbar.LENGTH_LONG).setAction("ver") { Toast.makeText(this@login.context, task.exception?.message, Toast.LENGTH_LONG).show() }.show()
                        Toast.makeText(this@login.context, e.message, Toast.LENGTH_LONG).show()
                    }
                    ///db
                }
            }.addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.no_se_registro), Snackbar.LENGTH_LONG).setAction("ver") {
                    //Todo al separar la variable task no era accesible remplace por la variabl execption
                    //   Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    Toast.makeText(this@login.context, e.toString(), Toast.LENGTH_LONG).show()
                }.show()
            }

    }
    private fun traer_datos(correo: String){
        val docRef = db.collection("USUARIOS").document(correo)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            Log.e("exito traje datos de ", correo)
            Global.usuario = documentSnapshot.toObject(Usuario::class.java)


            Glide.with(this)
                .load(Global.usuario.foto_perfil) // Uri of the picture
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target:  com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        //TODO handle error images while loading photo
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        //TODO use "resource" as the photo for your ImageView
                        perfil.setImageDrawable(resource)

                        pantalla=true;
                        Log.e("cambio a true ", correo)

                        return true
                    } })
                .submit()




        }
    }
    private fun launch_MainActivity() {



myDialog?.dismiss()

        sesion_launch?.sesion()
        Log.e("abrir","main")

        /*val intent = Intent(this@login.context, registroUsuario::class.java)
        startActivity(intent)
        getActivity()?.finish()*/


        // Handler().postDelayed({ finish() }, 2500)
    }
    private fun generar_token(correo: String){

        //   FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener{ instanceIdResult: InstanceIdResult ->
            val newToken = instanceIdResult.token
            Log.e("tokencito", newToken)
            db.collection("USUARIOS").document(correo).update("token", newToken).addOnSuccessListener { Log.e("token", "nuevo token actualizado") }
        }

    }
    private fun animacion_cargando() {
        myDialog = this@login.context?.let { Dialog(it, R.style.NewDialog) }

        //  myDialog = new Dialog(LoginActivity.this);
        myDialog?.setContentView(R.layout.layout_animacion_registro)
        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //     myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog?.setCancelable(false)
        animacion = myDialog?.findViewById(R.id.animation_view) as LottieAnimationView
        presentacion = myDialog?.findViewById(R.id.presentacion_animacion) as LinearLayout
        perfil = myDialog?.findViewById(R.id.perfil_animacion) as CircleImageView


    }


    private fun facebook(){


        try {
            val info = getActivity()?.packageManager?.getPackageInfo(
                "com.vallejo.now",
                PackageManager.GET_SIGNATURES)
            for (signature in info?.signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }



        /*  mProfileTracker = object : ProfileTracker() {
              override fun onCurrentProfileChanged(
                      oldProfile: Profile,
                      currentProfile: Profile) {
                  val perfil_name = currentProfile.name
                  Toast.makeText(baseContext, "profile", Toast.LENGTH_LONG).show()
                  Log.d("fb", "FirstName: " + currentProfile.firstName)
                  Log.d("fb", "LastName: " + currentProfile.lastName)
                  Log.d("fb", " MiddleName: " + currentProfile.middleName)
                  Log.d("fb", "LinkUri: " + currentProfile.linkUri)
                  Log.d("fb", "ProfilePictureUri: " + currentProfile.getProfilePictureUri(250, 250))
              }
          }*/

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) { //  setFacebookData(loginResult);
                // union_face_fire(loginResult.accessToken)
                Log.e("facebook", "inicio")
                val credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken())

                GraphRequest.newMeRequest(
                    loginResult.accessToken) { me, response ->
                    if (response.error != null) { // handle error
                    } else {



                        //  val nombre = me.optString("first_name")
                        //  val apellido = me.optString("last_name")
                        val name = me.optString("name")
                        val id = me.optString("id")
                        val email = me.optString("email")

                        Log.e("fb email","-"+email);
                        imprimir_facebook(id,name)



                        // send email and id to your web server
                    }
                }.executeAsync()


                firebase_credenciales(credential)
                //   imprimir_facebook()


            }

            override fun onCancel() {
                Log.e("facebook", "cancele")
            }

            override fun onError(error: FacebookException) {
                Log.e("facebook", "error $error")
            }
        })
        sesion_facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@login, Arrays.asList("email","public_profile")) }



    }
    private fun imprimir_facebook(id:String,name:String){
        val image_url = "http://graph.facebook.com/" +id+ "/picture?type=large"
        val parts = name.split(" ").toTypedArray()
        val nombre=name.replace(""+parts[parts.size - 1],"");//replaces all occurrences of 'a' to 'e'
        val apellido=parts[parts.size - 1]
        Log.e("fb",nombre)
        Log.e("fb", apellido)
        Log.e("fb",name)
        Log.e("fb",id)
        usuario_registro.nombre = nombre
        usuario_registro.apellido = apellido
        usuario_registro.foto_perfil = image_url
        //  LoginManager.getInstance().logOut()
    }

    private fun google(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = getActivity()?.let { GoogleSignIn.getClient(it, gso) }!!


        sesion_google.setOnClickListener {
            Log.e("google", "CLICK")
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 246)
        }

    }
    private fun escucha_google(completedTask: Task<GoogleSignInAccount>){


        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)


            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

            firebase_credenciales(credential)
            imprimir_google()
            ///Todo firebase y google
            // firebaseAuthWithGoogle(account)
            Log.e("google", "exitoso")

        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            Log.e("google", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun imprimir_google(){

        val acct = GoogleSignIn.getLastSignedInAccount(getActivity())



        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto = acct.photoUrl
            Log.e("google", personName) //Todo nombre completo
            Log.e("google", personGivenName)
            Log.e("google", personFamilyName)
            Log.e("google", personEmail)
            Log.e("google", personId)
            usuario_registro.nombre = personGivenName
            usuario_registro.apellido = personFamilyName
            usuario_registro.correo = personEmail
            usuario_registro.foto_perfil = personPhoto.toString()


            /* Glide.with(this)
                     .load(personPhoto) // Uri of the picture
                     .into(imagen_pincheta)*/
        }
//cerrar_google()

    }
    private fun cerrar_google(){

        mGoogleSignInClient.signOut()
            .addOnCompleteListener() {
                // ...
                Log.e("google", "cerre sesion")
            }
    }


    var sesion_launch: sesionCorrecta? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is sesionCorrecta) {
            sesion_launch = context
        } else {
            throw RuntimeException(context!!.toString() + " debe implementar Sesion")
        }
    }

    interface sesionCorrecta {
        fun sesion()
    }

    //Todo un dialog que recomienda por que activar los permisos

    override fun onStart() {
        super.onStart()
        mAuthListenr?.let { firebaseauth.addAuthStateListener(it) }


    }
    override fun onStop() {

        super.onStop()

        mAuthListenr?.let { firebaseauth.removeAuthStateListener(it) }


    }


    override fun onDestroy() {
//myDialog!!.dismiss()
        Log.e("finalice","login")
        super.onDestroy()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 246) { // The Task returned from this call is always completed, no need to attach
// a listener.
            Log.e("google", "pantalla")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            //  handleSignInResult(task)
            escucha_google(task)
        }

        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    override fun onDetach() {
        super.onDetach()
        sesion_launch = null
    }


}



