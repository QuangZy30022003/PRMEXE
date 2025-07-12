package com.example.projectprmexe.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.PromotionAPI;
import com.example.projectprmexe.data.model.Promotion.PromotionCreateUpdateDTO;
import com.example.projectprmexe.data.repository.PromotionInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.DatePickerDialog;
import java.util.Calendar;
import com.example.projectprmexe.data.model.Promotion.PromotionDTO;

public class PromotionCreateActivity extends AppCompatActivity {
    private EditText etCode, etDescription, etDiscountPercent, etMaxDiscount, etStartDate, etEndDate;
    private Button btnSave, btnCancel;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_create);

        etCode = findViewById(R.id.etPromotionCode);
        etDescription = findViewById(R.id.etPromotionDescription);
        etDiscountPercent = findViewById(R.id.etPromotionDiscountPercent);
        etMaxDiscount = findViewById(R.id.etPromotionMaxDiscount);
        etStartDate = findViewById(R.id.etPromotionStartDate);
        etEndDate = findViewById(R.id.etPromotionEndDate);
        btnSave = findViewById(R.id.btnSavePromotion);
        btnCancel = findViewById(R.id.btnCancelPromotion);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        token = prefs.getString("token", null);

        etStartDate.setFocusable(false);
        etEndDate.setFocusable(false);
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                createPromotion();
            }
        });
        btnCancel.setOnClickListener(v -> finish());
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etCode.getText())) {
            etCode.setError("Nhập mã giảm giá");
            return false;
        }
        if (TextUtils.isEmpty(etDiscountPercent.getText())) {
            etDiscountPercent.setError("Nhập % giảm giá");
            return false;
        }
        if (TextUtils.isEmpty(etMaxDiscount.getText())) {
            etMaxDiscount.setError("Nhập giảm tối đa");
            return false;
        }
        if (TextUtils.isEmpty(etStartDate.getText())) {
            etStartDate.setError("Nhập ngày bắt đầu (yyyy-MM-dd)");
            return false;
        }
        if (TextUtils.isEmpty(etEndDate.getText())) {
            etEndDate.setError("Nhập ngày kết thúc (yyyy-MM-dd)");
            return false;
        }
        return true;
    }

    private void createPromotion() {
        PromotionCreateUpdateDTO dto = new PromotionCreateUpdateDTO();
        dto.setCode(etCode.getText().toString().trim());
        dto.setDescription(etDescription.getText().toString().trim());
        dto.setDiscountPercent(Integer.parseInt(etDiscountPercent.getText().toString().trim()));
        dto.setMaxDiscount(Double.parseDouble(etMaxDiscount.getText().toString().trim()));
        dto.setStartDate(etStartDate.getText().toString().trim());
        dto.setEndDate(etEndDate.getText().toString().trim());
        dto.setIsActive(true);

        PromotionAPI api = PromotionInstance.getApiService();
        api.create("Bearer " + token, dto).enqueue(new Callback<PromotionDTO>() {
            @Override
            public void onResponse(Call<PromotionDTO> call, Response<PromotionDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PromotionCreateActivity.this, "Tạo mã giảm giá thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMsg = "Tạo mã giảm giá thất bại! Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorMsg += " - Exception: " + e.getMessage();
                    }
                    Toast.makeText(PromotionCreateActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    android.util.Log.e("Promotion", errorMsg);
                }
            }
            @Override
            public void onFailure(Call<PromotionDTO> call, Throwable t) {
                Toast.makeText(PromotionCreateActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("Promotion", "onFailure", t);
            }
        });
    }

    private void showDatePicker(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            (view, year, month, dayOfMonth) -> {
                String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                target.setText(date);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
} 