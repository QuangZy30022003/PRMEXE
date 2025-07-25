package com.example.projectprmexe.data.api;

import com.example.projectprmexe.data.model.PaymentDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PaymentAPI {
    @GET("api/Payment")
    Call<PaymentResponse> getAllPayments(@Header("Authorization") String token);

    @GET("api/Payment/my")
    Call<PaymentResponse> getMyPayments(@Header("Authorization") String token);

    class PaymentResponse {
        public boolean Success;
        public List<PaymentDto> Data;
    }
} 