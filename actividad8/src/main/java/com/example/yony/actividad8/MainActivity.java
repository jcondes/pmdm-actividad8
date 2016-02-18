package com.example.yony.actividad8;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private final String PREFS_NAME="PMDM_Actividad8";
    public EditText edtLoginUser, edtLoginPass,edtRegisterUser,edtRegisterPass,edtRegisterRePass;
    public CheckBox chkLoginGuardar,chkRegisterGuardar;
    public Button btnLogin,btnLoginRegistrar,btnRegistrarAceptar,btnRegistrarCancelar;
    MainActivityListeners malistener;
    public LinearLayout login_layout,register_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        malistener=new MainActivityListeners(this);
        initViews();
        DataHolder.instance.initQbAdmin(this);
        DataHolder.instance.qbAdmin.setQbAdminListener(malistener);
        DataHolder.instance.qbAdmin.setQBAdminListn(malistener);
        DataHolder.instance.qbAdmin.crearSession();
        DataHolder.instance.initPushNotificationsAdmin(this,"162501646954");
    }


    public void loadFromSharedPreferences(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String user=settings.getString("user", null);
        String pass=settings.getString("pass", null);

        if(user!=null){
            executeLogin(user,pass);
        }
    }

    public void saveUserPass(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("user",edtLoginUser.getText().toString());
        editor.putString("pass",edtLoginPass.getText().toString());
        editor.commit();
    }

    public void initViews(){
        login_layout=(LinearLayout)findViewById(R.id.login_layout);
        register_layout=(LinearLayout)findViewById(R.id.register_layout);
        edtLoginUser=(EditText)findViewById(R.id.login_usuario);
        edtLoginPass=(EditText)findViewById(R.id.login_pass);
        edtRegisterUser=(EditText)findViewById(R.id.registrar_usuario);
        edtRegisterPass=(EditText)findViewById(R.id.registrar_pass);
        edtRegisterRePass=(EditText)findViewById(R.id.registrar_repetir_pass);
        chkLoginGuardar=(CheckBox)findViewById(R.id.login_checkbox);
        chkRegisterGuardar=(CheckBox)findViewById(R.id.registrar_checkbox);
        btnLogin=(Button)findViewById(R.id.login_btnlogin);
        btnLogin.setOnClickListener(malistener);
        btnLoginRegistrar=(Button)findViewById(R.id.login_registrar);
        btnLoginRegistrar.setOnClickListener(malistener);
        btnRegistrarAceptar=(Button)findViewById(R.id.registrar_btnaceptar);
        btnRegistrarAceptar.setOnClickListener(malistener);
        btnRegistrarCancelar=(Button)findViewById(R.id.registrar_btncancelar);
        btnRegistrarCancelar.setOnClickListener(malistener);
    }

    public void executeLogin(String user, String pass){
        DataHolder.instance.qbAdmin.loginWithUser(user, pass);
    }

    public void retrieveUsers(){
        DataHolder.instance.qbAdmin.retrieveAllUsers();
    }
}
