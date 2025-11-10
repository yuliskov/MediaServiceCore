package com.liskovsoft.misc;

import static org.junit.Assert.fail;

import android.util.Log;

import com.liskovsoft.sharedutils.helpers.Helpers;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManagerTest {
    private static final String TAG = OkHttpManagerTest.class.getSimpleName();

    @Ignore("Wrong credentials")
    @Test
    public void testSocks5Proxy() throws InterruptedException {
        String[] testUrls = {"https://www.youtube.com", "https://www.google.com"};
        Builder builder = new Builder();
        setupProxy(builder, "85.195.81.170", "10542", "VAQW8t", "0qj2sz", Type.SOCKS);
        OkHttpClient client = builder.build();

        CountDownLatch waitForRegister = new CountDownLatch(2);

        for (String url : testUrls) {
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    fail("Proxy not working...");
                }

                @Override
                public void onResponse(Call call, Response response) {
                    String status = response.message();
                    Log.d(TAG, status);
                    waitForRegister.countDown();
                }
            });
        }

        waitForRegister.await();
    }

    // https://stackoverflow.com/questions/35554380/okhttpclient-proxy-authentication-how-to
    private static void setupProxy(Builder builder, String host, String port, String username, String password, Type proxyType) {
        if (host == null || port == null) {
            return;
        }

        if (username != null && password != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }

        builder.proxy(new Proxy(proxyType, new InetSocketAddress(host, Helpers.parseInt(port))));
        //builder.proxyAuthenticator(proxyAuthenticator);
    }
}