package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private String mResult;

    @Test
    public void verifyAsyncTaskResult() {
        final CountDownLatch signal = new CountDownLatch(1);

        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String s) {
                return;
            }
        };
        EndpointsAsyncTask asyncTask = new EndpointsAsyncTask(onTaskCompleted) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                mResult = result;
                signal.countDown();
            }
        };
        asyncTask.execute();
        try {
            signal.await();
        } catch (InterruptedException e) {
            Log.v("AsyncTaskTest", "Interrupted Exception");
        }
        assert mResult != null;
    }
}