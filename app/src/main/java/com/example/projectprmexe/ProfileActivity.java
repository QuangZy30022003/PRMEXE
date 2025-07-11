package com.example.projectprmexe;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etAddress, etBirthDate;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private TextView tvEmail;
    private Spinner spinnerGender;
    private Button btnSave, btnCancel;
    private ImageButton btnBack;
    private AuthApi authApi;
    private String selectedGender = "Nam";
    private UserProfile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupRetrofit();
        setupSpinner();
        setupDatePicker();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        tvEmail = findViewById(R.id.tvEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        etBirthDate = findViewById(R.id.etBirthDate);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5150/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthApi.class);
    }

    private void setupSpinner() {
        String[] genders = {"Nam", "Nữ"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genders[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupDatePicker() {
        etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etBirthDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String authHeader = "Bearer " + token;
        Call<UserProfile> call = authApi.getProfile(authHeader);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    populateFields(currentProfile);
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải thông tin profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(UserProfile profile) {
        etFullName.setText(profile.getFullName());
        tvEmail.setText(profile.getEmail());
        etPhone.setText(profile.getPhone());
        etAddress.setText(profile.getAddress());
        etBirthDate.setText(profile.getBirthDate());

        // Set gender spinner
        if ("Nam".equals(profile.getGender())) {
            spinnerGender.setSelection(0);
        } else {
            spinnerGender.setSelection(1);
        }
        selectedGender = profile.getGender();
    }

    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String gender = selectedGender;
        String birthDate = etBirthDate.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Vui lòng nhập địa chỉ");
            etAddress.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("Vui lòng nhập ngày sinh");
            etBirthDate.requestFocus();
            return;
        }

        // Check if user wants to change password
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean isChangingPassword = !TextUtils.isEmpty(oldPassword) || 
                                   !TextUtils.isEmpty(newPassword) || 
                                   !TextUtils.isEmpty(confirmPassword);

        if (isChangingPassword) {
            if (TextUtils.isEmpty(oldPassword)) {
                etOldPassword.setError("Vui lòng nhập mật khẩu cũ");
                etOldPassword.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(newPassword)) {
                etNewPassword.setError("Vui lòng nhập mật khẩu mới");
                etNewPassword.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                etConfirmPassword.setError("Vui lòng xác nhận mật khẩu mới");
                etConfirmPassword.requestFocus();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                etConfirmPassword.requestFocus();
                return;
            }
            if (newPassword.length() < 6) {
                etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                etNewPassword.requestFocus();
                return;
            }
        }

        // Get token
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        String authHeader = "Bearer " + token;
        UpdateProfileRequest request;
        
        if (isChangingPassword) {
            request = new UpdateProfileRequest(fullName, phone, address, gender, birthDate, 
                                            oldPassword, newPassword, confirmPassword);
        } else {
            request = new UpdateProfileRequest(fullName, phone, address, gender, birthDate);
        }

        Call<String> call = authApi.updateProfile(authHeader, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (isChangingPassword) {
                        Toast.makeText(ProfileActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 