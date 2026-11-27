package com.liskovsoft.googlecommon.common.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(RobolectricTestRunner.class)
public class RetrofitHelperTest {

    @Test
    public void testGetResponseDoesNotDumpStackTraceOnIOException() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errBuffer));
        try {
            SocketTimeoutException timeout = new SocketTimeoutException("connect timed out");
            try {
                RetrofitHelper.getResponse(new FailingCall<>(timeout));
                Assert.fail("Expected IllegalStateException");
            } catch (IllegalStateException e) {
                Assert.assertSame(timeout, e.getCause());
            }

            Assert.assertNull(RetrofitHelper.getResponse(new FailingCall<>(new ConnectException("refused"))));
            Assert.assertEquals("Should log via Log.e rather than dumping stack traces to System.err",
                    "", errBuffer.toString().trim());
        } finally {
            System.setErr(originalErr);
        }
    }

    private static class FailingCall<T> implements Call<T> {
        private final IOException mError;

        FailingCall(IOException error) {
            mError = error;
        }

        @Override
        public Response<T> execute() throws IOException {
            throw mError;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<T> clone() {
            return new FailingCall<>(mError);
        }

        @Override
        public Request request() {
            return new Request.Builder().url("https://www.youtube.com").build();
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }
    }
}
