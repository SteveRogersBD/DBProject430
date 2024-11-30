package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.example.greenpulse.adapters.VPAdapter;
import com.example.greenpulse.databinding.ActivityMainBinding;
import com.example.greenpulse.fragments.HomeFragment;
import com.example.greenpulse.fragments.MediaFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    VPAdapter vpAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GradientDrawable gradientDrawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.grad_3);
        int startColor = gradientDrawable.getColors()[0];
        int endColor = gradientDrawable.getColors()[1];
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(endColor);
        int bottomColor = Color.parseColor("#153E50");
        window.setNavigationBarColor(bottomColor);


        //logic related to the tabLayout
        binding.tabMode.setupWithViewPager(binding.viewPager);
        vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new HomeFragment(),"Home");
        vpAdapter.addFragment(new MediaFragment(),"Agri World");
        binding.viewPager.setAdapter(vpAdapter);



    }
}