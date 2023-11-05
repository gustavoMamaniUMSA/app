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

    String IP_RED_PROTOTIPO = "192.168.4.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean verificarConexion(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

        // Obtener la dirección IP en formato entero y luego convertirla a una cadena legible
        int direccionIp = info.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d",
                (direccionIp & 0xff), (direccionIp >> 8 & 0xff),
                (direccionIp >> 16 & 0xff), (direccionIp >> 24 & 0xff));
        Log.d("direccion IP: ", ipString);

        if (ipString.contains(this.IP_RED_PROTOTIPO)) {
            return true;
        }
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