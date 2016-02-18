package com.example.yony.actividad8;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quickblox.customobjects.model.QBCustomObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**Esta clas se encarga de escribir y leer los datos que se reciben de quickblox en la Internar Storage
 * Created by Jose Manuel Condes on 17/01/2016.
 */
public class GuardarDatos {

    private MessagesActivity ma;
    private ArrayList<Mensaje> mensajes;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private JSONObject objMain;
    private String jsonString;
    private String FILENAME = "MENSAJES";

    public GuardarDatos(MessagesActivity ma){
        this.ma = ma;
    }

    /**
     * Método para guardar un objeto JSON en un fichero con el nombre "POSICIONES" en la memoria interna
     * del dispositivo
     */
    public void escribirDatos(){
        //Coge el ArrayList de mensajes que se ha ido guardando al ir recibiendo los mensajes
        mensajes = DataHolder.instance.getMensajes();

        jsonArray = new JSONArray();
        objMain = new JSONObject();
        //Se crea el objeto JSON leyendo el ArrayList de mensajes
        try {
            for (int i = 0; i < mensajes.size(); i++) {
                jsonObject = new JSONObject();

                jsonObject.put("idUsuario", mensajes.get(i).getIdUsuario());
                jsonObject.put("nombreUsuario",mensajes.get(i).getNombreUsuario());
                jsonObject.put("mensaje", mensajes.get(i).getTextoMensaje());
                //Va metiendo cada objeto JSON con un mensaje en un JSONArray
                jsonArray.put(jsonObject);
            }
            //Mete el array en un objeto JSON
            objMain.put("mensajes", jsonArray);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        //Convierte el JSON en un String para escribirlo en la memoria
        jsonString = objMain.toString();
        Log.v("GuardarDatos", jsonString);

        try {
            //Se crea el fichero y se escribe el String convertido a partir del JSON
            FileOutputStream fos = ma.getBaseContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
            //Almacena en SharedPreferences un boolean para que se descargue los datos de memoria al iniciar la app
            SharedPreferences settings = ma.getSharedPreferences("PREFS_USUARIO", 0);
            SharedPreferences.Editor editorUsuario = settings.edit(); editorUsuario.putBoolean("descargarDeLocal", true);
            // Hace un commit o save de los datos.
            editorUsuario.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que lee de la memoria el JSON de mensajes y actuliza en el DataHolder el ArrayList de mensajes
     * @return
     */
    public void leerDatos(){
        String resultado = "";
        mensajes = new ArrayList<Mensaje>();
        try {
            //Se utiliza el BufferReader de la misma forma que al leer un fichero en java
            FileInputStream fis = ma.getBaseContext().openFileInput(FILENAME);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            resultado = response.toString();
            Log.v("GuardarDatos","LEE DE LA MEMORIA: " + resultado);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonMain = new JSONObject(resultado);
            JSONArray jsonMensajes = jsonMain.getJSONArray("mensajes");
            for (int i = 0; i < jsonMensajes.length(); i++) {
                JSONObject row = jsonMensajes.getJSONObject(i);
                String idUser = row.getString("idUsuario");
                String nameUser = row.getString("nombreUsuario");
                String txtMensaje = row.getString("mensaje");
                Log.v("GuardarDatos", "CAMPOS LEIDOS DEL JSON; " + idUser + "/ " + nameUser + "/ " + txtMensaje);
                mensajes.add(new Mensaje(idUser, nameUser, txtMensaje));
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        DataHolder.instance.setMensajes(mensajes);
    }
}
