package com.example.rutapp.GestionesBD;

import com.example.rutapp.Clases.Paradas;
import com.example.rutapp.Clases.Ruta;
import com.example.rutapp.Clases.Solicitudes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GestionesSolicitudes {
    private String IP = "http://sigegapp.000webhostapp.com/";//Direccion del hosting donde se alojó nuestro web service
    private String agregar = IP + "solicitud_insertar.php";//Noormbre del archivo que hace la función de insertar
    private String obtener_id = IP + "solicitud_obtener_por_id.php";
    private String obtener_ruta = IP + "solicitud_obtener_por_ruta.php";
    private String eliminar = IP + "solicitud_borrar.php";
    private URL url = null;//Direccion a donde mandaremos la peticion
    private HttpURLConnection urlConnection;

    private ArrayList<Solicitudes> listaSolicitudes = new ArrayList<>();

    private int id;
    private String identi;
    private Double latitud;
    private Double longitud;
    private int ruta;

    public GestionesSolicitudes() {

    }


    public ArrayList<Solicitudes> ConsultaPorId(String identificador,int ruta){

        try {
            url = new URL(obtener_id+"?identificador="+identificador+"&ruta="+ruta);
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

                System.out.println("Respuesta"+result);

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")){      // hay un alumno que mostrar

                    id = respuestaJSON.getJSONObject("solicitud").getInt("id");

                    identi = respuestaJSON.getJSONObject("solicitud").getString("identificador");

                    latitud = respuestaJSON.getJSONObject("solicitud").getDouble("latitud");

                    longitud = respuestaJSON.getJSONObject("solicitud").getDouble("longitud");

                    ruta = respuestaJSON.getJSONObject("solicitud").getInt("ruta");

                    listaSolicitudes.add(new Solicitudes(id,identificador,latitud,longitud,ruta));

                    return listaSolicitudes;

                }
                else{
                    return listaSolicitudes;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaSolicitudes;

    }


    public ArrayList<Solicitudes> ConsultaPorRuta(int ruta){

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
                System.out.println(result);

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")){      // hay un alumno que mostrar

                    JSONArray rutaJSON = respuestaJSON.getJSONArray("solicitud");


                    for (int i = 0; i < rutaJSON.length(); i++) {
                        id = rutaJSON.getJSONObject(i).getInt("id");

                        identi = rutaJSON.getJSONObject(i).getString("identificador");

                        latitud = rutaJSON.getJSONObject(i).getDouble("latitud");

                        longitud = rutaJSON.getJSONObject(i).getDouble("longitud");

                        ruta = rutaJSON.getJSONObject(i).getInt("ruta");

                        listaSolicitudes.add(new Solicitudes(id,identi,latitud,longitud,ruta));

                    }
                    return listaSolicitudes;

                }
                else{
                    return listaSolicitudes;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaSolicitudes;

    }


    public boolean Agregar(String identificador,Double latitud,Double longitud, int r) {

        try {

            url = new URL(agregar);//Teminamos de crear el oobjeto, mandando la ruta del servicio
            urlConnection = (HttpURLConnection) url.openConnection();//Abrimos la conexion.
            //Configuramos nuestro urlConnection con las caracteristicas necesarias
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //finalmente hacemos la conexion
            urlConnection.connect();

            //Se contruye el objeto de tipo Json
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("identificador", identificador);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
            jsonParam.put("ruta", r);
            //Envío los parametros al post de PHP
            OutputStream outputStream = urlConnection.getOutputStream();//Solicitamos la salida de datos al servidor
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonParam.toString());//Escribimos los parametros dentro del post
            writer.flush();
            writer.close();//Cierre del writer, recomendado

            int respuestaConexion = urlConnection.getResponseCode();//Obtenemos la respuesta

            StringBuilder result = new StringBuilder(); //almacenará el resultado

            if (respuestaConexion == HttpURLConnection.HTTP_OK) {

                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {//Recorremos el buffer y verificamos si aun existen datos
                    result.append(line);//Almacenamos por linea de respuestas
                }

                System.out.println(result);

                //Creamos un objeto de tipo Json para poder leer la inf obtenida

                JSONObject jsonObject = new JSONObject(result.toString());
                String resultadoJson = jsonObject.getString("estado");//Estado es el nombre del campo en el Json de retorno

                if (resultadoJson.equals("1")) {
                    return true;
                } else {
                    return false;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean Eliminar(String idenficador){

        try {

            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            url = new URL(eliminar);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.connect();
            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("identificador", idenficador);
            // Envio los parámetros post.
            OutputStream os = urlConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();

            int respuesta = urlConn.getResponseCode();


            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                    //response+=line;
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")) {      // hay un alumno que mostrar
                    return true;

                }
                else{
                    return false;
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;

    }
}
