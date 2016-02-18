package com.example.yony.actividad8;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.ArrayList;

import gebulot.pmdmlib.PushNotificationAdmin.Consts;
import gebulot.pmdmlib.PushNotificationAdmin.PushNotificationAdmin;
import gebulot.pmdmlib.PushNotificationAdmin.PushNotificationsAdminListener;
import gebulot.pmdmlib.QbAdmin.QbAdmin;

/**
 * Created by Yony on 20/01/2016.
 */
public class DataHolder implements PushNotificationsAdminListener{

    public final static DataHolder instance=new DataHolder();
    public final String TAG="DataHolder";


    public QbAdmin qbAdmin;
    public PushNotificationAdmin pushNotificationAdmin;

    public ArrayList<QBUser> qbUsers = new ArrayList<QBUser>();

    public ArrayList<Contacto> contactos = new ArrayList<Contacto>();

    private String idUser = "";

    private String nameUser = "";

    private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    private MessagesActivity messagesActivity;

    private String mensajeEnviado;

    private GuardarDatos gd;

    public DataHolder(){

    }


    public GuardarDatos getGd() {
        return gd;
    }

    public void setGd(GuardarDatos gd) {
        this.gd = gd;
    }

    public ArrayList<QBUser> getQbUsers() {
        return qbUsers;
    }

    public void setQbUsers(ArrayList<QBUser> qbUsers) {
        this.qbUsers = qbUsers;
    }


    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getMensajeEnviado() {
        return mensajeEnviado;
    }

    public void setMensajeEnviado(String mensajeEnviado) {
        this.mensajeEnviado = mensajeEnviado;
    }

    public MessagesActivity getMessagesActivity() {
        return messagesActivity;
    }

    public void setMessagesActivity(MessagesActivity messagesActivity) {
        this.messagesActivity = messagesActivity;
    }

    public void initQbAdmin(Context context){
        qbAdmin=new QbAdmin(context,"29341", "wxfT9He8dhp2JN2", "sTeZqDvpbSNeX7K");
    }

    public void initPushNotificationsAdmin(Activity activity,String aid){
        pushNotificationAdmin=new PushNotificationAdmin(activity,aid);
        pushNotificationAdmin.addListener(this);


        LocalBroadcastManager.getInstance(activity).registerReceiver(mPushReceiver,
                new IntentFilter(Consts.NEW_PUSH_EVENT));
    }

    @Override
    public void pushNotificationsRegistered(boolean blRegistered) {

    }

    // ESTE ES EL ULTIMO PASO QUE HARA EL MENSAJE RECIBIDO. AQUI ES DONDE EJECUTAMOS LO QUE NOS INTERESE EJECUTAR
    //AL RECIBIR UN MENSAJE. EN CASO DE RECIBIR EL MENSAJE CUANDO ESTAMOS DENTRO DE LA APP, O SI ESTAMOS FUERA DE LA APP
    //AQUI ES DONDE LLEGA EL MENSAJE. TODAS LAS ACCIONES QUE HAGAMOS CON EL MENSAJE SE HARAN AQUI.
    //
    private BroadcastReceiver mPushReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            JSONObject jsonMensaje = null;
            // Get extra data included in the Intent
            String message = intent.getStringExtra(Consts.EXTRA_MESSAGE);
            String idUser = intent.getStringExtra("idUsuario");
            String nameUser = intent.getStringExtra("nombreUsuario");
            Log.v("DataHolder", message);
            String qbcid = intent.getStringExtra("QBCID");


            Log.v(TAG, "Receiving event " + Consts.NEW_PUSH_EVENT + " with data: " + message);

            //AQUI INSERTAREMOS EL CODIGO QUE EJECUTAREMOS CUANDO LLEGUE EL MENSAJE.
            //Actualiza el ArrayList de mensajes añadiendo el recibido
            mensajes.add(new Mensaje(idUser, nameUser, message));
            DataHolder.instance.setMensajes(mensajes);
            //Escribe el ArrayList actualizado a la memoria
            DataHolder.instance.getGd().escribirDatos();

            //Para actualizar la lista se recrea el activity
            Activity act = DataHolder.instance.getMessagesActivity();
            //act.finish();
            //act.startActivity(act.getIntent());
            act.recreate();
        }
    };
}
