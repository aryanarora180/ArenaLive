package com.example.arenalive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScheduleFragment scheduleFragment1 = new ScheduleFragment(R.id.frame_layout_1);
        ScheduleFragment scheduleFragment2 = new ScheduleFragment(R.id.frame_layout_2);
        ScheduleFragment scheduleFragment3 = new ScheduleFragment(R.id.frame_layout_3);
        ScheduleFragment scheduleFragment4 = new ScheduleFragment(R.id.frame_layout_4);
        ScheduleFragment scheduleFragment5 = new ScheduleFragment(R.id.frame_layout_5);
        ScheduleFragment scheduleFragment6 = new ScheduleFragment(R.id.frame_layout_6);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_1, scheduleFragment1).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_2, scheduleFragment2).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_3, scheduleFragment3).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_4, scheduleFragment4).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_5, scheduleFragment5).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_6, scheduleFragment6).commit();

    }
}
