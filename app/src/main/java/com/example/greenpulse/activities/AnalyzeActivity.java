package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityAnalyzeBinding;

import java.lang.reflect.Array;

public class AnalyzeActivity extends OtherActivity {

    ActivityAnalyzeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyzeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dealWithTheSpinners();
    }

    private void dealWithTheSpinners()
    {
        String[] cropTypes = {
                "Fruits", "Vegetables", "Legumes", "Grains", "Lentils",
                "Oilseeds", "Tubers", "Spices", "Herbs", "Nuts",
                "Cereals", "Pulses", "Cash Crops", "Beverage Crops",
                "Fiber Crops", "Forage Crops", "Medicinal Crops", "Aquatic Crops"
        };

        String[] growthStages = {
                "Seedling", "Vegetative", "Bud Formation", "Flowering",
                "Pollination", "Fruit Set", "Maturity", "Harvest"
        };

        ArrayAdapter<String>typeAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cropTypes);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String>growthAdapter = new ArrayAdapter<>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                growthStages);
        growthAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.growthSpinner.setAdapter(growthAdapter);


        binding.typeSpinner.setOnItemSelectedListener(itemSelectedListener);
        binding.growthSpinner.setOnItemSelectedListener(itemSelectedListener);



    }
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int parentId = parent.getId();
            String selectedItem = parent.getSelectedItem().toString();
            if(parentId==binding.typeSpinner.getId())
            {
                binding.typeTv.setText(selectedItem);
                binding.typeTv.setTextColor(getColor(R.color.black));
            } else if (parentId==binding.growthSpinner.getId()) {
                binding.growthTv.setText(selectedItem);
                binding.growthTv.setTextColor(getColor(R.color.black));
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==binding.picker.getId())
            {

            }
        }
    };
}