package com.a4kgarden.simpleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.a4kgarden.mysimpleprogress.R;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private RadiusProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (RadiusProgress) findViewById(R.id.progress);
        progress.setMax(1000);

        seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int mprogress, boolean b) {
                progress.setProgress(mprogress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        progress.setProgress(200);
    }
}
