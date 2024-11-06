package com.test;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class InsecureApiModule extends ReactContextBaseJavaModule {

  public InsecureApiModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return "InsecureApi";
  }

  private OkHttpClient getUnsafeOkHttpClient() {
    try {
      // Trust manager that does not validate certificate chains
      final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
          }
        }
      };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
      builder.hostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @ReactMethod
  public void fetchUrl(String url, Promise promise) {
    OkHttpClient client = getUnsafeOkHttpClient();

    Request request = new Request.Builder()
      .url(url)
      .build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        promise.reject("Error", e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          promise.reject("Error", "Request failed with code: " + response.code());
          return;
        }
        String responseData = response.body().string();
        promise.resolve(responseData);
      }
    });
  }
}
