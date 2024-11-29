package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivitySignUpBinding;
import com.example.greenpulse.databinding.ActivitySplashBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Pair[] pairs = new Pair[9];
        pairs[0]=new Pair<View,String>(binding.logoSignUp,"logo_image");
        pairs[1]=new Pair<View,String>(binding.htSignUp,"title");
        pairs[2]=new Pair<View,String>(binding.wbSignUp,"subtitle");
        pairs[3]=new Pair<View,String>(binding.subSignUp,"sub_anim");
        pairs[4]=new Pair<View,String>(binding.emailSignUp,"email_anim");
        pairs[5]=new Pair<View,String>(binding.passwordSignUp,"pw_anim");
        pairs[6]=new Pair<View,String>(binding.signUpBtnSignUp,"button_anim");
        pairs[7]=new Pair<View,String>(binding.googleBtnSignUp,"google_anim");
        pairs[8]=new Pair<View,String>(binding.bottomLinearSignUp,"linear_anim");
        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,
                pairs);
        binding.signInTvSignUp.setOnClickListener((v)->{
            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }
}