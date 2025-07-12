package com.example.projectprmexe;

import android.content.Context;
import android.content.SharedPreferences;

public class MockAuthManager {
    private static final String PREFS_NAME = "MockUsers";
    private static final String TOKEN_PREFIX = "mock_token_";
    private SharedPreferences prefs;
    
    public MockAuthManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public MockLoginResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (prefs.contains(request.getEmail())) {
            return new MockLoginResponse(false, null, "Email đã được đăng ký");
        }
        
        // Lưu thông tin user
        String userData = request.getFullName() + "|" + request.getPhone() + "|" + 
                         request.getAddress() + "|" + request.getGender() + "|" + request.getBirthDate();
        prefs.edit().putString(request.getEmail(), request.getPassword() + "|" + userData).apply();
        
        // Tạo token
        String token = TOKEN_PREFIX + System.currentTimeMillis();
        return new MockLoginResponse(true, token, "Đăng ký thành công");
    }
    
    public MockLoginResponse login(LoginRequest request) {
        String storedData = prefs.getString(request.getEmail(), null);
        
        // DEBUG: In ra tất cả accounts đã đăng ký
        android.util.Log.d("MockAuth", "=== ALL REGISTERED ACCOUNTS ===");
        java.util.Map<String, ?> allPrefs = prefs.getAll();
        for (java.util.Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            android.util.Log.d("MockAuth", "Email: " + entry.getKey() + " | Data: " + entry.getValue());
        }
        android.util.Log.d("MockAuth", "Trying to login: " + request.getEmail());
        android.util.Log.d("MockAuth", "Stored data: " + storedData);
        
        if (storedData == null) {
            return new MockLoginResponse(false, null, "Email không tồn tại");
        }
        
        String[] parts = storedData.split("\\|");
        String storedPassword = parts[0];
        
        if (!storedPassword.equals(request.getPassword())) {
            return new MockLoginResponse(false, null, "Mật khẩu không đúng");
        }
        
        // Tạo token
        String token = TOKEN_PREFIX + System.currentTimeMillis();
        return new MockLoginResponse(true, token, "Đăng nhập thành công");
    }
    
    public static class MockLoginResponse {
        private boolean success;
        private String token;
        private String message;
        
        public MockLoginResponse(boolean success, String token, String message) {
            this.success = success;
            this.token = token;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getToken() { return token; }
        public String getMessage() { return message; }
    }
}