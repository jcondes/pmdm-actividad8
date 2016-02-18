package com.example.yony.actividad8;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jose on 18/02/2016.
 */
public class ListaContAdapter extends BaseAdapter{

    private Activity act;

    private ArrayList<Contacto> contactos;

    public ListaContAdapter(Activity activity){
        this.act = activity;
        contactos = DataHolder.instance.getContactos();
        Log.v("ListaContAdapter", "<----------------------------------->" + Integer.toString(contactos.size()));
    }

    @Override
    public int getCount() {
        return contactos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflamos la vista a partir del layout del elemento fila que genera la lista
        LayoutInflater lif = act.getLayoutInflater();
        View v = lif.inflate(R.layout.layout_contacto, null, true);

        TextView txt_idcontacto = (TextView)v.findViewById(R.id.textView_idContacto);
        TextView txt_namecontacto = (TextView)v.findViewById(R.id.textView_nameContacto);

        txt_idcontacto.setText(contactos.get(position).getIdContacto());
        txt_namecontacto.setText(contactos.get(position).getNombreContacto());

        return v;
    }

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }
}
