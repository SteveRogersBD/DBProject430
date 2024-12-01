package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenpulse.Disease;
import com.example.greenpulse.GeminiHelper;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityDiseaseBinding;

public class DiseaseActivity extends OtherActivity {

    GeminiHelper gm;
    ActivityDiseaseBinding binding;
    Disease disease;
    String crop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        //String disease = intent.getStringExtra("disease");
        //String crop = intent.getStringExtra("crop");
        disease = new Disease();
        disease.name = "Early Blight";
        binding.diseaseName.setText(disease.name);
        crop = "Tomato";
        gm = new GeminiHelper();

        getHelpFromGemini(binding.diseaseDesc,descriptionPrompt(disease.name));
        getHelpFromGemini(binding.diseaseSymptomsTv,symptomPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePotentialTv,potentialThreatPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePreventionTv,preventionPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseaseTreatmentTv,treatmentPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePostTv,postManagementPrompt(disease.name,crop));



    }

    private String descriptionPrompt(String disease){
        String prompt = "Give me a precise description of the disease named "+disease+". Write everyhting" +
                "in one paragraph within 100 words. No more no less.";
        return prompt;
    }
    private String symptomPrompt(String disease, String crop){
        String prompt = "What are the symptoms of "+disease+" in a "+crop+" give each symptom using" +
                " bullet numbers (like 1,2,3,...). Nothing more nothing less.";
        return prompt;
    }
    private String potentialThreatPrompt(String disease, String crop){
        String prompt = "What are the potential threats of "+disease+" in "+crop+"s.";
        return prompt;
    }
    private String preventionPrompt(String disease, String crop){
        String prompt = "What are the prevention methods for "+disease+" in "+crop+"s?";
        return prompt;
    }
    private String treatmentPrompt(String disease, String crop)
    {
        String prompt = "What are the treatment methods for "+disease+" in "+crop+"s?";
        return prompt;
    }
    private String postManagementPrompt(String disease, String crop)
    {
        String prompt = "If in a crop some "+crop+"s have been affected by "+disease+", then " +
                "what are some post disease management strategies that one should follow to avoid" +
                "further attack from that disease";
        return prompt;
    }

    private GeminiHelper.GeminiCallback geminiCallback = new GeminiHelper.GeminiCallback() {
        @Override
        public void onSuccess(String result, int tag) {
            disease.description = result;
        }

        @Override
        public void onFailure(Throwable t) {
            runOnUiThread(()->{
                Toast.makeText(DiseaseActivity.this,t.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            });
        }
    };

    private void getHelpFromGemini(TextView tv,String prompt)
    {
        int id = tv.getId();
        if(id==binding.diseaseDesc.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.description);
        }
        else if(id==binding.diseasePreventionTv.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.prevention);
        }
        else if(id==binding.diseaseSymptomsTv.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.symptoms);
        }
        else if(id==binding.diseaseTreatmentTv.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.treatment);
        }
        else if(id==binding.diseasePostTv.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.postManagement);
        }
        else if(id==binding.diseasePotentialTv.getId())
        {
            gm.callGemini(prompt,geminiCallback);
            tv.setText(disease.potentialLoss);
        }
    }
}