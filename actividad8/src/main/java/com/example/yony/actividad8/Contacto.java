package com.example.yony.actividad8;

/**
 * Created by Jose on 18/02/2016.
 */
public class Contacto {

    private String idContacto;
    private String nombreContacto;

    public Contacto(String idContacto, String nombreContacto){
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
    }

    public String getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(String idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }
}
