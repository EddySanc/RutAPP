package com.example.rutapp.GestionesBD;

import com.example.rutapp.Clases.Paradas;
import com.example.rutapp.Clases.Paradas;
import com.example.rutapp.Clases.Ruta;

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

public class GestionesParadas {
    private String IP = "http://sigegapp.000webhostapp.com/";//Direccion del hosting donde se alojó nuestro web service
    private String agregar = IP + "paradas_insertar.php";//Noormbre del archivo que hace la función de insertar
    private String obtener = IP + "paradas_obtener.php";
    private String obtener_id = IP + "paradas_obtener_por_id.php";
    private String obtener_ruta = IP + "paradas_obtener_por_ruta.php";
    private String editar = IP + "paradas_actualizar.php";
    private String eliminar = IP + "paradas_borrar.php";
    private URL url = null;//Direccion a donde mandaremos la peticion
    private HttpURLConnection urlConnection;

    private ArrayList<Paradas> listaParadas = new ArrayList<>();

    private int id;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private int ruta;

    public GestionesParadas() {

    }


    public ArrayList<Paradas> ConsultaPorId(int idRuta){

        try {
            url = new URL(obtener_id+"?ruta="+idRuta);
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

                    id = respuestaJSON.getJSONObject("paradas").getInt("id");

                    descripcion = respuestaJSON.getJSONObject("paradas").getString("descripcion");

                    latitud = respuestaJSON.getJSONObject("paradas").getDouble("latitud");

                    longitud = respuestaJSON.getJSONObject("paradas").getDouble("longitud");

                    ruta = respuestaJSON.getJSONObject("paradas").getInt("ruta");

                    listaParadas.add(new Paradas(id,descripcion,latitud,longitud,ruta));

                    return listaParadas;

                }
                else{
                    return listaParadas;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaParadas;

    }


    public ArrayList<Paradas> ConsultaPorRuta(int ruta){

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

                    JSONArray rutaJSON = respuestaJSON.getJSONArray("paradas");


                    for (int i = 0; i < rutaJSON.length(); i++) {
                        id = rutaJSON.getJSONObject(i).getInt("id");

                        descripcion = rutaJSON.getJSONObject(i).getString("descripcion");

                        latitud = rutaJSON.getJSONObject(i).getDouble("latitud");

                        longitud = rutaJSON.getJSONObject(i).getDouble("longitud");

                        ruta = rutaJSON.getJSONObject(i).getInt("ruta");

                        listaParadas.add(new Paradas(id,descripcion,latitud,longitud,ruta));

                    }
                    return listaParadas;

                }
                else{
                    return listaParadas;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaParadas;

    }


    public boolean Agregar(Double latitud,Double longitud, int r) {

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
            jsonParam.put("ruta", r);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
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

    public boolean Editar(Ruta ruta){

        try {

            url = new URL(editar);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", ruta.getId());
            jsonParam.put("num_ruta", ruta.getNum_ruta());
            jsonParam.put("descripcion", ruta.getDescripcion());


            // Envio los parámetros post.
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();

            int respuesta = urlConnection.getResponseCode();



            StringBuilder result = new StringBuilder();
            System.out.println("result::::::::::"+respuesta);
            System.out.println("result::::::::::"+HttpURLConnection.HTTP_OK);
            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    result.append(line);
                    //response+=line;
                }

                System.out.println("result::::::::::"+result);

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")) {      // hay un alumno que mostrar
                    return true;

                } else {
                    return false;
                }

            }

        } catch (MalformedURLException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (JSONException e) {
            System.out.println(e.toString());
        }

        return false;
    }

    public boolean Eliminar(int idRuta){

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
            jsonParam.put("ruta", idRuta);
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
