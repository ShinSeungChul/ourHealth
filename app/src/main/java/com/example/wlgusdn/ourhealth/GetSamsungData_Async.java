package com.example.wlgusdn.ourhealth;

import android.content.Context;
import android.util.Log;
import com.samsung.android.sdk.healthdata.*;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.Iterator;

public class GetSamsungData_Async {

    // The state of connection
    HealthDataStore mStore;

    public static final String APP_TAG = "MyApp";

    public void readStepKcalAsynchronously(long startTime, long endTime , Context context) {
        mStore = new HealthDataStore(context, mConnectionListener);
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        startTime, endTime)
                .build();

        try {
            resolver.read(request).setResultListener(mRdResult);
        } catch (Exception e) {
            Log.d(APP_TAG, "Reading health data fails." + e.toString());
        }
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mRdResult =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {

                @Override
                public void onResult(HealthDataResolver.ReadResult result) {

                    try {
                        Iterator<com.samsung.android.sdk.healthdata.HealthData> iterator = result.iterator();

                        if (iterator.hasNext()) {
                            HealthData data = iterator.next();
                            float stepKcal = data.getFloat(HealthConstants.StepCount.CALORIE);
                            Log.d("Samsung",stepKcal + " kcal to step...");
                        }
                    } finally {
                        result.close();
                    }
                }
            };
    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");

        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");

        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");

        }
    };
}
