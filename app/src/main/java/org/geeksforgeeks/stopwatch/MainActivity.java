package org.geeksforgeeks.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Locale;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    private ListView vueltas;
    private ArrayList<String> marcas;
    private TextView conv;
    private TextView timeView;
    private ProgressBar p;
    Handler h = new Handler();
    boolean isActivo=false;
    int i;
    private Button sp;
    private Button pausa;
    private Boolean bandera = false;
    private ArrayAdapter<String> adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = (ProgressBar) findViewById(R.id.circle);
        vueltas = (ListView) findViewById(R.id.lv_vueltas);
        sp = (Button) findViewById(R.id.start_button);
        pausa = (Button) findViewById(R.id.start_button2);
        marcas = new ArrayList<String>();
        pausa.setEnabled(false);

        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        runTimer();
    }

    // Save the state of the stopwatch
    // if it's about to be destroyed.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume()
    {

        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }
    public void onClickStart(View view)
    {
        bandera=true;
        if (bandera==true){
            sp.setEnabled(false);
            pausa.setEnabled(true);
            bandera=false;
        }
        if(!isActivo){
            i = p.getProgress();
            Thread hilo=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (i<=100){
                        i+=1;
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                p.setProgress(i);
                            }
                        });
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }



                    }
                    isActivo=true;
                }
            });
        }

        running = true;
    }



    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
        marcas.clear();
        adapter.notifyDataSetChanged();

        
    }
    private void runTimer()
    {
         timeView = (TextView)findViewById(R.id.time_view);

        // Creates a new Handler
        final Handler handler
                = new Handler();
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 10);
            }
        });
    }
    public void onClickStop(View view)
    {
       // running = false
            marcas.add(timeView.getText().toString());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, marcas);
            vueltas.setAdapter(adapter);
            i++;



    }
    public void onClickPause(View view)
    {
        super.onPause();
        running = false;
        if (bandera==false){
            sp.setEnabled(true);
            pausa.setEnabled(false);
            bandera=true;
        }
    }

}