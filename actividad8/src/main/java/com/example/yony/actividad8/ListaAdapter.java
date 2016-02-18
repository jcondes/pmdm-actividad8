package com.example.yony.actividad8;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import java.util.ArrayList;

/**
 * Creatd by Jose Manuel Condes on 17/12/2015
 */
public class ListaAdapter extends BaseAdapter {

    private Activity act;

    private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

    /**
     * En el constructor vamos añadiendo los mensajes del JSON recibido en un ArrayList
     * @param activity
     */
    public ListaAdapter(Activity activity) {
        this.act = activity;
        mensajes = DataHolder.instance.getMensajes();
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflamos la vista a partir del layout del elemento fila que genera la lista
        LayoutInflater lif = act.getLayoutInflater();
        View v = lif.inflate(R.layout.layout_mensaje, null, true);
        //Creamos los objetos de las filas y les damos valores que se obtienen del ArrayList de objetos mensajes
        TextView userId = (TextView)v.findViewById(R.id.textView_idUser);
        TextView userName = (TextView)v.findViewById(R.id.textView_nameUser);
        TextView textMessage = (TextView)v.findViewById(R.id.textView_mensaje);

        userId.setText(mensajes.get(position).getIdUsuario());
        userName.setText(mensajes.get(position).getNombreUsuario());
        textMessage.setText(mensajes.get(position).getTextoMensaje());



        return v;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }
}