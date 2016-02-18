package com.example.yony.actividad8;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBMessages;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener, FragmentMensaje.FragmentMensajeListener, FragmentContactos.FragmentContactoListener{

    private ListaAdapter listaAdapter = null;
    private Button btn_fragment = null;
    private Button btn_lista = null;

    private ListView listView = null;

    private FragmentManager fm;
    private static FragmentMensaje fragmentMensaje;
    private FragmentTransaction ft;

    private FragmentContactos fc;

    private GuardarDatos guardarDatos;
    private boolean leerMemoria = false;

    private static String idUsuario = "9391222";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        guardarDatos = new GuardarDatos(this);
        DataHolder.instance.setGd(guardarDatos);

        SharedPreferences settings = getSharedPreferences("PREFS_USUARIO", 0);

        leerMemoria = settings.getBoolean("descargarDeLocal", false);
        if(leerMemoria){
            guardarDatos.leerDatos();
        }

        listView = (ListView)findViewById(R.id.list);

        DataHolder.instance.setMessagesActivity(this);

        btn_fragment = (Button)findViewById(R.id.button_abrir_fragment);
        btn_fragment.setOnClickListener(this);
        btn_lista = (Button)findViewById(R.id.button_abrir_lista);
        btn_lista.setOnClickListener(this);

        listaAdapter = new ListaAdapter(this);
        listView.setAdapter(listaAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Mensaje> mensajes = DataHolder.instance.getMensajes();
                idUsuario = mensajes.get(position).getIdUsuario();
                mostrarFragment();
                Toast.makeText(getApplicationContext(), mensajes.get(position).getNombreUsuario(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        fm = getFragmentManager();
        fragmentMensaje = new FragmentMensaje();
        fragmentMensaje.setOnFragmentMensajeListener(this);
        ft = fm.beginTransaction();
        ft.replace(R.id.contenedor, fragmentMensaje, "FragmentMensaje");
        ft.addToBackStack("FragmentMensaje");
        ft.commit();
        getFragmentManager().beginTransaction().hide(fragmentMensaje).commit();
    }

    public void mostrarFragment(){
        fm = getFragmentManager();
        fragmentMensaje = new FragmentMensaje();
        fragmentMensaje.setOnFragmentMensajeListener(this);
        ft = fm.beginTransaction();
        ft.replace(R.id.contenedor, fragmentMensaje, "FragmentMensaje");
        ft.addToBackStack("FragmentMensaje");
        ft.commit();
    }

    public void cerrarFragment(){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.remove(fragmentMensaje);
        ft.commit();
    }

    @Override
    public void enviarMensajeContacto(String idContacto){
        idUsuario = idContacto;
        mostrarFragment();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_enviar){
            enviarMensajeGrupo();
        }else if(v.getId() == R.id.button_enviar_id){
            enviarMensajeIds();
        }else if(v.getId() == R.id.button_cerrar){
            cerrarFragment();
        }else if(v.getId() == R.id.button_abrir_fragment) {
            mostrarFragment();
        }else if(v.getId() == R.id.button_abrir_lista){
            Log.v("MessageActivity", "ENTRA");
            fm = getFragmentManager();
            fc = new FragmentContactos();
            fc.setOnFragmentContactoListener(this);
            ft = fm.beginTransaction();
            ft.replace(R.id.contenedor, fc, "FragmentContactos");
            ft.addToBackStack("FragmentContactos");
            ft.commit();
        }
    }

    public void enviarMensajeGrupo(){

        JSONObject jsonMensaje = null;

        // Send Push: create QuickBlox Push Notification Event
        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);

        String mensajeEnviado = DataHolder.instance.getMensajeEnviado();
        try {
            jsonMensaje = new JSONObject();
            jsonMensaje.put("idUsuario", DataHolder.instance.getIdUser());
            jsonMensaje.put("nombreUsuario", DataHolder.instance.getNameUser());
            jsonMensaje.put("message", mensajeEnviado);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String mensajeJson = jsonMensaje.toString();
        Log.v("MessageActivity", "El mensaje JSON enviado es " + mensajeJson);

        // generic push - will be delivered to all platforms (Android, iOS, WP, Blackberry..)
        qbEvent.setMessage(mensajeJson);
        // recipients
        StringifyArrayList<String> userTags = new StringifyArrayList<String>();
        userTags.add("damp");

        qbEvent.setUserTagsAny(userTags);

        QBMessages.createEvent(qbEvent, new QBEntityCallbackImpl<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                Log.v("MessageActivity", "LA ID DEL USUARIO QUE ENVIA ES " + qbEvent.getUserId().toString());
            }

            @Override
            public void onError(List<String> strings) {

            }
        });
    }

    public void enviarMensajeIds(){

        String mensajeEnviado = DataHolder.instance.getMensajeEnviado();
        JSONObject jsonMensaje = null;

        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(Integer.parseInt(idUsuario));

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        try {
            jsonMensaje = new JSONObject();
            jsonMensaje.put("idUsuario", DataHolder.instance.getIdUser());
            jsonMensaje.put("nombreUsuario", DataHolder.instance.getNameUser());
            jsonMensaje.put("message", mensajeEnviado);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String mensajeJson = jsonMensaje.toString();
        Log.v("MessageActivity", "El mensaje JSON enviado es " + mensajeJson);

        event.setMessage(mensajeJson);

        QBMessages.createEvent(event, new QBEntityCallbackImpl<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {

            }

            @Override
            public void onError(List<String> strings) {

            }
        });
    }
}
