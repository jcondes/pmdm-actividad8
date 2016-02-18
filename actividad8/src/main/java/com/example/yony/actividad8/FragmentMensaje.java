package com.example.yony.actividad8;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jose.condes on 15/02/2016.
 */
public class FragmentMensaje extends Fragment {

    private Button btn_enviar;
    private Button btn_enviar_id;
    private Button btn_cerrar;
    private EditText txt_mensaje_fragment;

    private FragmentMensajeListener listener;

    // ***********************************************************
    // Interfaz
    // ***********************************************************
    public interface FragmentMensajeListener
    {
        public void onClick(View v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.layout_fragment, container, false);
        btn_enviar = (Button)v.findViewById(R.id.button_enviar);
        btn_enviar_id = (Button)v.findViewById(R.id.button_enviar_id);
        btn_cerrar = (Button)v.findViewById(R.id.button_cerrar);
        txt_mensaje_fragment = (EditText)v.findViewById(R.id.editText_fragment);
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    String mensaje = txt_mensaje_fragment.getText().toString();
                    DataHolder.instance.setMensajeEnviado(mensaje);
                    listener.onClick(v);
                }
            }
        });
        btn_enviar_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    String mensaje = txt_mensaje_fragment.getText().toString();
                    DataHolder.instance.setMensajeEnviado(mensaje);
                    listener.onClick(v);
                }
            }
        });
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(v);
                }
            }
        });
        return v;
    }

    public void setOnFragmentMensajeListener(FragmentMensajeListener listener){
        this.listener = listener;
    }

}
