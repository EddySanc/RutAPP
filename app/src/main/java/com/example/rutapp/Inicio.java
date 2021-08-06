package com.example.rutapp;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.rutapp.Clases.Identify;

import org.jetbrains.annotations.NotNull;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio extends Fragment implements View.OnClickListener {

    private CardView itemMapa, itemSugerencias;
    public View vista;
    private final int PHONEREQUES = 1;



    public Inicio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_inicio, container, false);


        //Verifico si aun no han sido concedidos los permisos de localizacion, si no es asi pregunto al usuario.
        if (validaPermisos()) {
            Identify.imei = obtenerIMEI();
        }

        itemMapa = vista.findViewById(R.id.itemMapa);
        itemSugerencias = vista.findViewById(R.id.itemSugerencias);



        itemMapa.setOnClickListener(this);
        itemSugerencias.setOnClickListener(this);




        return vista;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.itemMapa:

                if(validaPermisos()) {
                    AbrirFragments(new Mapa());
                }
                else {
                    Toast.makeText(vista.getContext(),"Es necesario conceder los permisos para acceder a esta función",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.itemSugerencias:
                Uri url = Uri.parse("http://rutasbch.ml/manuales.php");
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW,url);
                startActivity(intent);
                break;

        }
    }


    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((vista.getContext().getApplicationContext().checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)&&
                (vista.getContext().getApplicationContext().checkSelfPermission(READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(READ_PHONE_STATE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{ACCESS_FINE_LOCATION,READ_PHONE_STATE},100);
        }

        return false;
    }





    private String obtenerIMEI() {
        final TelephonyManager telephonyManager = (TelephonyManager) vista.getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
            if (vista.getContext().getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return "";
            }
            else {
                return telephonyManager.getImei();
            }
        }
        else {
            return telephonyManager.getDeviceId();
        }

    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(vista.getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, READ_PHONE_STATE}, 100);
            }
        });
        dialogo.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){

                Identify.imei = obtenerIMEI();


            }else{
                System.out.println("Permisos Denied: "+Identify.imei);
            }
        }

    }
    /*
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(vista.getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",vista.getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(vista.getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }*/

    public void AbrirFragments(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
