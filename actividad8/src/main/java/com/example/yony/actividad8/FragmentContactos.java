package com.example.yony.actividad8;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jose on 18/02/2016.
 */
public class FragmentContactos extends Fragment {

    private FragmentContactoListener listener;

    // ***********************************************************
    // Interfaz
    // ***********************************************************
    public interface FragmentContactoListener
    {
        public void enviarMensajeContacto(String idContacto);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_lista_contactos, container, false);
        ListView lv = (ListView) v.findViewById(R.id.list_contactos);
        lv.setAdapter(new ListaContAdapter(DataHolder.instance.getMessagesActivity()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Contacto> contactos = DataHolder.instance.getContactos();
                if(listener != null) {
                    listener.enviarMensajeContacto(contactos.get(position).getIdContacto());
                }
                Toast.makeText(getActivity(), contactos.get(position).getNombreContacto(), Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    public void setOnFragmentContactoListener(FragmentContactoListener listener){
        this.listener = listener;
    }


}
