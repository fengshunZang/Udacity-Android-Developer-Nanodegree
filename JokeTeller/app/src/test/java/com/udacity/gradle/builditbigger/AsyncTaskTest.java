package com.udacity.gradle.builditbigger;
import android.util.Log;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by Zang on 2016-09-29.
 */

public class AsyncTaskTest {
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
