package com.example.puntosciegos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class radarActivity extends AppCompatActivity {

    AudioPlayer audioPlayer;

    boolean mostrarMenu = false;

    boolean modoVehiculo = false;
    ImageView v_vehiculo,v_motocicleta;
    Button btn_opciones, btn_vehiculo, btn_motocicleta, btn_volver;
    ImageView iv_sensor_derecha_1,iv_sensor_derecha_2, iv_sensor_derecha_3,
            iv_sensor_izquierda_1,iv_sensor_izquierda_2, iv_sensor_izquierda_3,
            iv_sensor_atras_1,iv_sensor_atras_2,iv_sensor_atras_3,
            iv_scan_1,iv_scan_2,iv_scan_3,iv_scan_4;
    Animation scan_derecha_1,scan_derecha_2,scan_derecha_3,
            scan_izquierda_1,scan_izquierda_2,scan_izquierda_3,
            scan_atras_1,scan_atras_2,scan_atras_3,
            scan_1,scan_2,scan_3,scan_4;

    String colorCercano = "#ff0000";
    String colorPrudente = "#f8f511";
    String colorLejano = "#1ca709";
    String colorApagado = "#00ffffff";

    MyServer myServer = new MyServer();
    Thread myThread;

    int guiIzquierda,guiDesactivado,guiDerecha;
    float sensorIzquierda = 0,sensorAtras = 0,sensorDerecha = 0;

    float antiguoValor = 0;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myThread = new Thread(myServer);
        myThread.start();

        audioPlayer = new AudioPlayer(this, R.raw.audio_alarma_recortado);

        v_vehiculo = (ImageView) findViewById(R.id.v_vehiculo);
        v_motocicleta = (ImageView) findViewById(R.id.v_motocicleta);
        btn_opciones = (Button) findViewById(R.id.btn_opciones);
        btn_vehiculo = (Button) findViewById(R.id.btn_vehiculo);
        btn_motocicleta = (Button) findViewById(R.id.btn_motocicleta);
        btn_volver = (Button) findViewById(R.id.btn_volver);

        v_vehiculo.setVisibility(View.VISIBLE);
        v_motocicleta.setVisibility(View.GONE);

        iv_sensor_derecha_1 = (ImageView) findViewById(R.id.iv_sensor_derecha_1);
        iv_sensor_derecha_2 = (ImageView) findViewById(R.id.iv_sensor_derecha_2);
        iv_sensor_derecha_3 = (ImageView) findViewById(R.id.iv_sensor_derecha_3);
        iv_sensor_izquierda_1 = (ImageView) findViewById(R.id.iv_sensor_izquierda_1);
        iv_sensor_izquierda_2 = (ImageView) findViewById(R.id.iv_sensor_izquierda_2);
        iv_sensor_izquierda_3 = (ImageView) findViewById(R.id.iv_sensor_izquierda_3);
        iv_sensor_atras_1 = (ImageView) findViewById(R.id.iv_sensor_atras_1);
        iv_sensor_atras_2 = (ImageView) findViewById(R.id.iv_sensor_atras_2);
        iv_sensor_atras_3 = (ImageView) findViewById(R.id.iv_sensor_atras_3);
        iv_scan_1 = (ImageView) findViewById(R.id.iv_scan_1);
        iv_scan_2 = (ImageView) findViewById(R.id.iv_scan_2);
        iv_scan_3 = (ImageView) findViewById(R.id.iv_scan_3);
        iv_scan_4 = (ImageView) findViewById(R.id.iv_scan_4);
        iniciarScanner();
        iniciarSensorAtras();
        iniciarSensorDerecha();
        iniciarSensorIzquierda();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.id_v){
            Toast.makeText(this,"mostrar vehiculo",Toast.LENGTH_SHORT).show();
            modoVehiculo = true;
        }else if(id == R.id.id_m){
            Toast.makeText(this,"mostrar motocicleta",Toast.LENGTH_SHORT).show();
            modoVehiculo = false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrar(View v){
        modoVehiculo = !modoVehiculo;
        if(mostrarMenu)
            muestraMenu();
        else
            ocultaMenu();
    }

    public void muestraMenu(){
        btn_volver.setVisibility(View.VISIBLE);
        btn_vehiculo.setVisibility(View.VISIBLE);
        btn_motocicleta.setVisibility(View.VISIBLE);
    }

    public void ocultaMenu(){
        btn_volver.setVisibility(View.GONE);
        btn_vehiculo.setVisibility(View.GONE);
        btn_motocicleta.setVisibility(View.GONE);
    }

    public void cambiaVehiculo(View v){
        v_vehiculo.setVisibility(View.VISIBLE);
        v_motocicleta.setVisibility(View.GONE);
        ocultaMenu();
    }

    public void cambiaMotocicleta(View v){
        v_vehiculo.setVisibility(View.GONE);
        v_motocicleta.setVisibility(View.VISIBLE);
        ocultaMenu();
    }

    public void iniciarScanner(){
        scan_1 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_scan);
        iv_scan_1.startAnimation(scan_1);

        scan_2 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_scan);
        scan_2.setStartOffset(1200);
        iv_scan_2.startAnimation(scan_2);

        scan_3 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_scan);
        scan_3.setStartOffset(1800);
        iv_scan_3.startAnimation(scan_3);

        scan_4 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_scan);
        scan_4.setStartOffset(2400);
        iv_scan_4.startAnimation(scan_4);
    }

    public void cambiarColorScanner(float direccional){
        String colorToApply = "#00000000";
        if(direccional<5)
            colorToApply = colorApagado;
        else if(direccional<150)
            colorToApply = colorCercano;
        else if(direccional<250)
            colorToApply = colorPrudente;
        else if(direccional<350)
            colorToApply = colorLejano;
        else
            colorToApply = colorApagado;

        Drawable shape_1 = (Drawable) iv_scan_1.getBackground();
        shape_1.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_2 = (Drawable) iv_scan_2.getBackground();
        shape_2.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_3 = (Drawable) iv_scan_3.getBackground();
        shape_3.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_4 = (Drawable) iv_scan_4.getBackground();
        shape_4.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
    }

    public void iniciarSensorAtras(){
        scan_atras_1 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        iv_sensor_atras_1.startAnimation(scan_atras_1);

        scan_atras_2 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_atras_2.setStartOffset(1100);
        iv_sensor_atras_2.startAnimation(scan_atras_2);

        scan_atras_3 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_atras_3.setStartOffset(1600);
        iv_sensor_atras_3.startAnimation(scan_atras_3);
    }

    public void cambiarColorAtras() {
        String colorToApply = "#FF0000";
        if(sensorAtras<5)
            colorToApply = colorApagado;
        else if(sensorAtras<150)
            colorToApply = colorCercano;
        else if(sensorAtras<250)
            colorToApply = colorPrudente;
        else if(sensorAtras<350)
            colorToApply = colorLejano;
        else
            colorToApply = colorApagado;

        Drawable shape_1 = (Drawable) iv_sensor_atras_1.getBackground();
        shape_1.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_2 = (Drawable) iv_sensor_atras_2.getBackground();
        shape_2.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_3 = (Drawable) iv_sensor_atras_3.getBackground();
        shape_3.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
    }

    public void iniciarSensorDerecha(){
        scan_derecha_1 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        iv_sensor_derecha_1.startAnimation(scan_derecha_1);

        scan_derecha_2 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_derecha_2.setStartOffset(900);
        iv_sensor_derecha_2.startAnimation(scan_derecha_2);

        scan_derecha_3 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_derecha_3.setStartOffset(1400);
        iv_sensor_derecha_3.startAnimation(scan_derecha_3);
    }
    public void cambiarColorDerecha(){
        String colorToApply = "#FF0000";
        if(sensorDerecha<5)
            colorToApply = colorApagado;
        else if(sensorDerecha<150)
            colorToApply = colorCercano;
        else if(sensorDerecha<250)
            colorToApply = colorPrudente;
        else if(sensorDerecha<350)
            colorToApply = colorLejano;
        else
            colorToApply = colorApagado;

        Drawable shape_1 = (Drawable) iv_sensor_derecha_1.getBackground();
        shape_1.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_2 = (Drawable) iv_sensor_derecha_2.getBackground();
        shape_2.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_3 = (Drawable) iv_sensor_derecha_3.getBackground();
        shape_3.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
    }

    public void iniciarSensorIzquierda(){
        scan_izquierda_1 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        iv_sensor_izquierda_1.startAnimation(scan_izquierda_1);

        scan_izquierda_2 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_izquierda_2.setStartOffset(700);
        iv_sensor_izquierda_2.startAnimation(scan_izquierda_2);

        scan_izquierda_3 = AnimationUtils.loadAnimation(radarActivity.this,R.anim.anim_sensor);
        scan_izquierda_3.setStartOffset(1000);
        iv_sensor_izquierda_3.startAnimation(scan_izquierda_3);
    }
    public void cambiarColorIzquierda() {
        String colorToApply = "#000000";
        if(sensorIzquierda<5)
            colorToApply = colorApagado;
        else if(sensorIzquierda<150)
            colorToApply = colorCercano;
        else if(sensorIzquierda<250)
            colorToApply = colorPrudente;
        else if(sensorIzquierda<350)
            colorToApply = colorLejano;
        else
            colorToApply = colorApagado;

        Drawable shape_1 = (Drawable) iv_sensor_izquierda_1.getBackground();
        shape_1.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_2 = (Drawable) iv_sensor_izquierda_2.getBackground();
        shape_2.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
        Drawable shape_3 = (Drawable) iv_sensor_izquierda_3.getBackground();
        shape_3.setColorFilter(Color.parseColor(colorToApply), android.graphics.PorterDuff.Mode.SRC);
    }

    public void volver(View v){
        try {
            myServer.mysocket.close();
            myServer.servidorSocket.close();
            myThread.interrupt();
        }catch(IOException e){
            e.printStackTrace();
        }
        finish();
    }

    public void lectura(String mensaje){
        Log.d("Mensaje: ",mensaje);
        String []lecturaSeparada = mensaje.split(";");
//        for(int i=0; i<lecturaSeparada.length;i++)
//            Log.d("Iteracion: ",lecturaSeparada[i]);
        if(mensaje.length() != 0) {
            guiIzquierda = Integer.parseInt(lecturaSeparada[1]);
            guiDesactivado = Integer.parseInt(lecturaSeparada[2]);
            guiDerecha = Integer.parseInt(lecturaSeparada[3]);
            sensorIzquierda = Float.parseFloat(lecturaSeparada[4]);
            sensorAtras = Float.parseFloat(lecturaSeparada[5]);
            sensorDerecha = Float.parseFloat(lecturaSeparada[6]);

            // Iniciando audio
            if (modoVehiculo) {
                audioPlayer.audioSensorIzquierdo(sensorIzquierda);
                audioPlayer.audioSensorAtras(sensorAtras);
                audioPlayer.audioSensorDerecho(sensorDerecha);
            }

            if(guiDesactivado == 1)
                cambiarColorScanner(0);
            else{
                if(guiIzquierda == 1){
                    cambiarColorScanner(sensorIzquierda);
                    sensorIzquierda = 0;
                    sensorAtras = 0;
                    sensorDerecha = 0;
                }else if(guiDerecha == 1){
                    cambiarColorScanner(sensorDerecha);
                    sensorIzquierda = 0;
                    sensorAtras = 0;
                    sensorDerecha = 0;
                }
            }
            cambiarColorIzquierda();
            cambiarColorAtras();
            cambiarColorDerecha();
        }
    }

    class MyServer implements Runnable{

        ServerSocket servidorSocket;
        Socket mysocket;
        DataInputStream dataInputStream;
        String mensaje;
        Handler handler = new Handler();

        @Override
        public void run() {
            try{
                servidorSocket = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Iniciando comunicación",Toast.LENGTH_LONG).show();
                    }
                });
                while(true){
                    mysocket = servidorSocket.accept();
                    dataInputStream = new DataInputStream(mysocket.getInputStream());
                    StringBuffer inputLine = new StringBuffer();
                    String tmp;
                    mensaje="";
                    while((tmp = dataInputStream.readLine()) != null){
                        inputLine.append(tmp);
                        mensaje = tmp;
                    }
                    dataInputStream.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),"Mensaje recibido del cliente: "+mensaje,Toast.LENGTH_LONG).show();
                            if(mensaje.length() != 0){
                                lectura(mensaje);
                            }
                        }
                    });
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    public void send(View v){
        BackgroundTask b = new BackgroundTask();
        //b.execute(e1.getText().toString(),e2.getText().toString());

    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        Socket socket;
        DataOutputStream dos;
        String ip,mensaje;

        @Override
        protected String doInBackground(String... strings) {
            ip = strings[0];
            mensaje = strings[1];
            try{
                socket = new Socket(ip,9700);
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(mensaje);
                dos.close();
                socket.close();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK) {
            try {
                myServer.mysocket.close();
                myServer.servidorSocket.close();
                myThread.interrupt();
            }catch(IOException e){
                e.printStackTrace();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void revasamiento(){
        if (antiguoValor < sensorAtras){
            contador++;
            if(contador == 20){
                Log.d("AlertaRebasamiento","Alerta rebasamiento");
                // opcion de enviar a un bucle while hasta que la distancia sea mayor
                // opcion de utilizar un timer de 5seg para mostrar la alerta de rebasamiento
                // no olvidar la funcion de los guiñadores para enfatizar la lectura de sensores
                // ultima etapa -> habilitar un sonido de alerta en caso de medidas cercanas
                contador = 0;
            }
        }else{
            contador = 0;
        }
        antiguoValor = sensorAtras;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
        }
    }
}