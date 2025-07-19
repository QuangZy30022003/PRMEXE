package com.example.projectprmexe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.view.Gravity;
import android.view.View;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;

import com.example.projectprmexe.R;

public class PayOSWebViewActivity extends AppCompatActivity {
    public static final String EXTRA_PAYMENT_URL = "payment_url";
    public static final String EXTRA_RETURN_URL = "return_url";
    public static final String EXTRA_PAYMENT_RESULT = "payment_result";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tạo layout cha
        FrameLayout rootLayout = new FrameLayout(this);

        // Tạo WebView
        WebView webView = new WebView(this);
        FrameLayout.LayoutParams webParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        rootLayout.addView(webView, webParams);

        // Tạo nút Home
        ImageButton btnHome = new ImageButton(this);
        btnHome.setImageResource(R.drawable.ic_home);
        btnHome.setBackgroundColor(0x99FFFFFF); // Nền trắng mờ để nổi bật
        int size = (int) (56 * getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(size, size);
        btnParams.gravity = Gravity.TOP | Gravity.END;
        btnParams.topMargin = (int) (24 * getResources().getDisplayMetrics().density);
        btnParams.rightMargin = (int) (24 * getResources().getDisplayMetrics().density);
        btnHome.setLayoutParams(btnParams);
        btnHome.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(PayOSWebViewActivity.this, ProductListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        rootLayout.addView(btnHome);

        setContentView(rootLayout);

        String paymentUrl = getIntent().getStringExtra(EXTRA_PAYMENT_URL);
        String returnUrl = getIntent().getStringExtra(EXTRA_RETURN_URL);
        String cancelUrl = getIntent().getStringExtra("cancel_url");
        if (paymentUrl == null || returnUrl == null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // BỎ QUA lỗi SSL khi test local, KHÔNG dùng cho production!
                handler.proceed();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ((returnUrl != null && url.startsWith(returnUrl)) ||
                    (cancelUrl != null && url.startsWith(cancelUrl))) {
                    Intent intent = new Intent(PayOSWebViewActivity.this, ProductListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if ((returnUrl != null && url.startsWith(returnUrl)) ||
                    (cancelUrl != null && url.startsWith(cancelUrl))) {
                    Intent intent = new Intent(PayOSWebViewActivity.this, ProductListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        webView.loadUrl(paymentUrl);
    }
} 