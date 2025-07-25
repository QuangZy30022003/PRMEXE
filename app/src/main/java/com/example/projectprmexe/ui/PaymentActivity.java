package com.example.projectprmexe.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprmexe.R;
import com.example.projectprmexe.data.api.PaymentAPI;
import com.example.projectprmexe.data.model.PaymentDto;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private List<PaymentDto> paymentList = new ArrayList<>();
    private PaymentAPI paymentAPI;
    private String authToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Sử dụng Toolbar trong layout
        Toolbar toolbar = findViewById(R.id.paymentToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentAdapter(paymentList);
        recyclerView.setAdapter(adapter);
        setupRetrofit();
        loadAuthToken();
        loadPayments();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5150/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        paymentAPI = retrofit.create(PaymentAPI.class);
    }

    private void loadAuthToken() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        authToken = "Bearer " + token;
    }

    private void loadPayments() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String role = prefs.getString("role", "user"); // Mặc định là user
        Log.d("PAYMENT_ROLE", "Role: " + role); // Log giá trị role thực tế

        Call<PaymentAPI.PaymentResponse> call;
        if ("Admin".equalsIgnoreCase(role) || "4".equals(role)) {
            call = paymentAPI.getAllPayments(authToken);
        } else {
            call = paymentAPI.getMyPayments(authToken);
        }

        call.enqueue(new Callback<PaymentAPI.PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentAPI.PaymentResponse> call, Response<PaymentAPI.PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().Success) {
                    paymentList.clear();
                    paymentList.addAll(response.body().Data);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PaymentActivity.this, "Không lấy được danh sách payment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentAPI.PaymentResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
} 