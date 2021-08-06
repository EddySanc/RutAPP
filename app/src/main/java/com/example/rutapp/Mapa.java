package com.example.rutapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rutapp.Clases.Identify;
import com.example.rutapp.Clases.Paradas;
import com.example.rutapp.Clases.Recorrido;
import com.example.rutapp.Clases.Ruta;
import com.example.rutapp.Clases.Solicitudes;
import com.example.rutapp.Clases.Ubicacion;
import com.example.rutapp.GestionesBD.GestionesParadas;
import com.example.rutapp.GestionesBD.GestionesRecorrido;
import com.example.rutapp.GestionesBD.GestionesRuta;
import com.example.rutapp.GestionesBD.GestionesSolicitudes;
import com.example.rutapp.GestionesBD.GestionesUbicacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mapa extends Fragment implements OnMapReadyCallback {

    private ListView lvRuta;//ListView
    private View vista;
    private ProgressBar progressBar;

    private int idRecorrio;
    private Double latitudR;
    private Double longitudR;
    private int rutaFK_R;

    private int idRuta = 0;
    private int num_ruta;
    private String descripcion;

    private int idParada;
    private Double latitudP;
    private Double longitudP;
    private int rutaFK_P;

    private int idSolicitud;
    private Double latitudS;
    private Double longitudS;

    private int tipo = 1;
    private int e = 1;


    private ArrayList<Ruta> listaRutaRef = new ArrayList<>();//Refencia a la list view segun el id
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listRuta = new ArrayList<>(); // Con esta variable se llenará los datos en el ListView

    private ArrayList<Recorrido> listaRecorridoConsulta = new ArrayList<>();
    private ArrayList<Paradas> listaParadasConsulta = new ArrayList<>();
    private ArrayList<Solicitudes> listaSolicitudesConsulta = new ArrayList<>();
    private ArrayList<Ubicacion> listaUbicacionConsulta = new ArrayList<>();

    private RealizarGestiones realizarGestiones;//Clase intnerna tipo Asyntask
    private GestionesParadas gestionesParadas;
    private GestionesRecorrido gestionesRecorrido;
    private GestionesRuta gestionesRuta;
    private GestionesSolicitudes gestionesSolicitudes;
    private GestionesUbicacion gestionesUbicacion;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private GoogleMap mMap;
    private AlertDialog alert = null;
    private Polyline polyline;
    private Marker marcador;
    private Marker[] colectivos;




    public Mapa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista = inflater.inflate(R.layout.fragment_mapa, container, false);

         lvRuta = vista.findViewById(R.id.lista_ruta);
         progressBar = vista.findViewById(R.id.progreso);
         progressBar.setProgress(0);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapa);

        mapFragment.getMapAsync(this);

        CargarHilo();
        ClicListaRuta();


        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Alerta emergente al no tener el gps activado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGPS();
        }

        //Verfico la version de android que tiene el dispositivo

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                return null;

            }
            else{
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        else{

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location locatio) {
                mMap.setMyLocationEnabled(true);

                location = locatio;

                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if(e==1){

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    e++;
                }
                if(idRuta!= 0){

                    tipo = 6;
                    realizarGestiones = new RealizarGestiones();
                    realizarGestiones.execute();

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,0,locationListener);


        return  vista;
    }

    public void LlenarLista() {

        //Llenado del ListView
        adapter = new ArrayAdapter<String>(vista.getContext(), android.R.layout.simple_list_item_1, listRuta);
        lvRuta.setAdapter(adapter);

    }

    public void CargarHilo() {

        realizarGestiones = new RealizarGestiones();
        realizarGestiones.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        ClicParadas();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(alert != null)
        {
            alert.dismiss ();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        }
    }

    private void AlertNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Los  sistemas de localización estan desactivados, ¿Desea activar el GPS?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    private void ClicParadas(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                latitudS = marker.getPosition().latitude;
                longitudS = marker.getPosition().longitude;

                System.out.println(latitudS+"Longitud"+longitudS);

                if(marcador == null){

                    final CharSequence[] opciones={"si","no"};
                    final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(vista.getContext());
                    alertOpciones.setTitle("¿Desea solicitar su parada en esta ubicación?");
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (opciones[i].equals("si")){
                                tipo =3;
                                CargarHilo();
                            }else{
                                Toast.makeText(vista.getContext(),"Cancelado",Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }
                    });
                    alertOpciones.show();
                }
                else {

                    final CharSequence[] opciones={"Actualizar mi parada","Eliminar parada"};
                    final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(vista.getContext());
                    alertOpciones.setTitle("Actualmente tienes una parada solicitada.¿Qué desea hacer?");
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (opciones[i].equals("Actualizar mi parada")){
                                tipo =5;
                                CargarHilo();

                            }
                            if (opciones[i].equals("Eliminar parada")){

                                tipo =4;
                                CargarHilo();

                            }

                        }
                    });
                    alertOpciones.show();

                }

                return false;
            }
        });
    }



    private void TrazarRecorrido(){



        PolylineOptions recorrido = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i<listaRecorridoConsulta.size();i++) {
            recorrido.add(new LatLng(listaRecorridoConsulta.get(i).getLatitud(), listaRecorridoConsulta.get(i).getLongitud()));
            System.out.println(listaRecorridoConsulta.get(i).getLatitud()+""+ listaRecorridoConsulta.get(i).getLongitud());
        }

        polyline = mMap.addPolyline(recorrido);



        for(int i=0;i<listaParadasConsulta.size();i++) {

            LatLng mLatLng = new LatLng(listaParadasConsulta.get(i).getLatitud(),listaParadasConsulta.get(i).getLongitud());

            mMap.addMarker(new MarkerOptions().position(mLatLng).title(getAddressFromLatLng(mLatLng)).snippet("Paradero").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.paradero))));

        }

        for(int i = 0 ; i<listaSolicitudesConsulta.size();i++){
            marcador = mMap.addMarker(new MarkerOptions().snippet("Tu parada está aquí") .
                    position( new LatLng(listaSolicitudesConsulta.get(i).getLatitud(), listaSolicitudesConsulta.get(i).getLongitud())) .draggable(true).visible(true));
        }


        colectivos = new Marker[listaUbicacionConsulta.size()];



        for(int i = 0 ; i<listaUbicacionConsulta.size();i++){
            colectivos[i] = mMap.addMarker(new MarkerOptions().zIndex(2).icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus))
                    .snippet("Colectivo") .
                            position( new LatLng(listaUbicacionConsulta.get(i).getLatitud(), listaUbicacionConsulta.get(i).getLongitud())).visible(true));
        }



        LatLng myLocation = new LatLng(listaRecorridoConsulta.get(0).getLatitud(), listaRecorridoConsulta.get(0).getLongitud());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,10));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

    }

    private void MostrarColectivos(){

        if(colectivos != null){

            for(int i = 0; i<colectivos.length;i++){
                colectivos[i].remove();
            }
        }

        colectivos = new Marker[listaUbicacionConsulta.size()];

        for(int i = 0 ; i<listaUbicacionConsulta.size();i++){
            colectivos[i] = mMap.addMarker(new MarkerOptions().zIndex(2).icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus))
                    .snippet("Colectivo") .
                            position( new LatLng(listaUbicacionConsulta.get(i).getLatitud(), listaUbicacionConsulta.get(i).getLongitud())).visible(true));
        }


    }



    private String getAddressFromLatLng( LatLng latLng ) {
        Geocoder geocoder = new Geocoder( getActivity() );

        String address = "";
        try {
            address = geocoder
                    .getFromLocation( latLng.latitude, latLng.longitude, 1 )
                    .get( 0 ).getAddressLine( 0 );
        } catch (IOException e ) {
        }

        return address;
    }

    private void BuscarRecorrido(){
        tipo = 2;
        realizarGestiones = new RealizarGestiones();
        realizarGestiones.execute();
    }

    private void ClicListaRuta(){
        lvRuta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                idRuta = listaRutaRef.get(position).getId();
                BuscarRecorrido();
            }
        });
    }


    public class RealizarGestiones extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(vista.INVISIBLE);
                    progressBar.setProgress(0);
                }
            });
            if(tipo ==1) {
                if (listaRutaRef.size() == 0) {
                    Toast.makeText(vista.getContext(), s, Toast.LENGTH_LONG).show();
                } else {
                    LlenarLista();
                }
            }

            if(tipo ==2) {
                if (listaRecorridoConsulta.size() == 0) {
                    Toast.makeText(vista.getContext(),s,Toast.LENGTH_SHORT).show();
                } else {

                    TrazarRecorrido();

                    if(listaSolicitudesConsulta.size() ==0){
                        Toast.makeText(vista.getContext(), "Aun no tienes solicitudes de paradas", Toast.LENGTH_LONG).show();
                    }

                }
            }

            if(tipo == 3){
                if(s.equals("true")){

                    marcador = mMap.addMarker(new MarkerOptions().snippet("Tu parada está aquí") .position( new LatLng(latitudS, longitudS)).visible(true));
                }
                else {
                    Toast.makeText(vista.getContext(),"Ocurrio un error al solicitar la parada, intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }
            if(tipo == 4){
                if(s.equals("true")){
                    marcador.remove();
                    marcador = null;
                }
                else {
                    Toast.makeText(vista.getContext(),"Ocurrio un error al eliminar la parada, intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }

            }
            if(tipo == 5){
                if(s.equals("true")){
                    marcador.remove();
                    marcador = mMap.addMarker(new MarkerOptions().snippet("Tu parada está aquí") .position( new LatLng(latitudS, longitudS)).visible(true));


                }
                else {
                    Toast.makeText(vista.getContext(),"Ocurrio un error al actualizar la parada, intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }

            }
            if(tipo == 6){

                if(listaUbicacionConsulta.size()>0){
                    MostrarColectivos();
                }

            }


        }

        @Override
        protected String doInBackground(String... strings) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(vista.VISIBLE);
                }
            });

            if(tipo==1) {
                gestionesRuta = new GestionesRuta();
                listaRutaRef = gestionesRuta.ConsultaGral();


                if (listaRutaRef.size() > 0) {
                    for (int i = 0; i < listaRutaRef.size(); i++) {

                        listRuta.add(listaRutaRef.get(i).getNum_ruta()+"");

                    }
                } else {
                    return "No hay ningun resultado";
                }
            }
            if(tipo==2) {
                gestionesRecorrido = new GestionesRecorrido();
                listaRecorridoConsulta = gestionesRecorrido.ConsultaPorRuta(idRuta);

                gestionesParadas = new GestionesParadas();
                listaParadasConsulta = gestionesParadas.ConsultaPorRuta(idRuta);

                gestionesSolicitudes = new GestionesSolicitudes();
                listaSolicitudesConsulta = gestionesSolicitudes.ConsultaPorId(Identify.imei,idRuta);

                gestionesUbicacion = new GestionesUbicacion();
                listaUbicacionConsulta = gestionesUbicacion.ConsultaPorRuta(idRuta);

                if (listaRecorridoConsulta.size() > 0) {

                } else {
                    return "Aun no se ha trazado el recorrido para esta ruta";
                }
            }
            if(tipo == 3) {//Agregar parada pasajero

                gestionesSolicitudes = new GestionesSolicitudes();
                return gestionesSolicitudes.Agregar(Identify.imei,latitudS,longitudS,idRuta)+"";

            }
            if(tipo == 4){//Eliminar Parada
                gestionesSolicitudes = new GestionesSolicitudes();
                return gestionesSolicitudes.Eliminar(Identify.imei)+"";
            }
            if(tipo == 5){//Actualizar parada

                gestionesSolicitudes = new GestionesSolicitudes();
                if(!gestionesSolicitudes.Eliminar(Identify.imei)){
                    return false+"";
                }
                else {
                    return gestionesSolicitudes.Agregar(Identify.imei,latitudS,longitudS,idRuta)+"";
                }
            }
            if(tipo == 6){

                gestionesUbicacion = new GestionesUbicacion();
                listaUbicacionConsulta = gestionesUbicacion.ConsultaPorRuta(idRuta);

            }
            return "";
        }
    }

}
