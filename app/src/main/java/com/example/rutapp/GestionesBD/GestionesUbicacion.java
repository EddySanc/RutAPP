package com.example.rutapp.GestionesBD;

import com.example.rutapp.Clases.Paradas;
import com.example.rutapp.Clases.Ubicacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GestionesUbicacion {
    private String IP = "http://sigegapp.000webhostapp.com/";//Direccion del hosting donde se alojó nuestro web service
    private String agregar = IP + "paradas_insertar.php";//Noormbre del archivo que hace la función de insertar
    private String obtener = IP + "paradas_obtener.php";
    private String obtener_id = IP + "paradas_obtener_por_id.php";
    private String obtener_ruta = IP + "ubicacion_obtener_por_ruta.php";
    private String editar = IP + "ubicacion_actualizar.php";
    private String eliminar = IP + "paradas_borrar.php";
    private URL url = null;//Direccion a donde mandaremos la peticion
    private HttpURLConnection urlConnection;

    private ArrayList<Ubicacion> listaUbicacion = new ArrayList<>();

    private int id;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private int colectivo;

    public GestionesUbicacion() {


    }


    public ArrayList<Ubicacion> ConsultaPorRuta(int ruta){

        try {
            url = new URL(obtener_ruta+"?ruta="+ruta);
            System.out.println(":::::::::::::::::::::::::::"+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            //connection.setHeader("content-type", "application/json");

            int respuesta = connection.getResponseCode();
            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK){


                InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                // StringBuilder.

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);        // Paso toda la entrada al StringBuilder
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")){      // hay un alumno que mostrar

                    JSONArray rutaJSON = respuestaJSON.getJSONArray("ubicacion");


                    for (int i = 0; i < rutaJSON.length(); i++) {
                        id = rutaJSON.getJSONObject(i).getInt("id");
                        latitud = rutaJSON.getJSONObject(i).getDouble("latitud");

                        longitud = rutaJSON.getJSONObject(i).getDouble("longitud");

                        colectivo = rutaJSON.getJSONObject(i).getInt("colectivo");



                        listaUbicacion.add(new Ubicacion(id, latitud,longitud,colectivo));

                    }
                    return listaUbicacion;

                }
                else{
                    return listaUbicacion;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaUbicacion;

    }


}
