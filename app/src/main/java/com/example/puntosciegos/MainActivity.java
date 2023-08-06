package com.example.puntosciegos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean verificarConexion(){
        String ssidWifi = "puntosCiegos";
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid  = info.getSSID();
        Log.d("nombre WIFI: ",ssid);
        if(ssid.contains(ssidWifi))
            return true;
        return false;
    }

    public void iniciarLectura(View v){
        Intent ventanaMoto = new Intent(getApplicationContext(), radarActivity.class);
        if (verificarConexion()){
            startActivity(ventanaMoto);
        }else {
            Toast.makeText(this,"No está conectado a la red Wifi correcta\n ingrese a PARÁMETROS DE RED WIFI",Toast.LENGTH_LONG).show();
        }
    }

    public void mostrarWiFi(View v){
        Intent wifi = new Intent(getApplicationContext(), WifiActivity.class);
        startActivity(wifi);
    }

    public void salir(View v){
        finish();
    }
}