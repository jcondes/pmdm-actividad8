package com.example.yony.actividad8;

/**
 * Created by jose.condes on 15/02/2016.
 */
public class Mensaje {

    private String textoMensaje;
    private String idUsuario;
    private String nombreUsuario;
    
    public Mensaje(String idUsuario, String nombreUsuario, String textoMensaje){
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.textoMensaje = textoMensaje;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
