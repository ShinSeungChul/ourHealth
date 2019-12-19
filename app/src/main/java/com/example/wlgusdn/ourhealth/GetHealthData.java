package com.example.wlgusdn.ourhealth;

import android.os.Debug;
import android.os.Handler;
import android.widget.Toast;
import com.samsung.android.sdk.healthdata.*;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetHealthData extends Activity {

    public static final String APP_TAG = "SimpleHealth";
    HealthData_Singleton singleton = HealthData_Singleton.getInstance();
    public boolean one = false;
    public boolean two = false;
    public boolean three = false;
    public long start = 0;
    public long end = 0;
    public float exkcal = 0f;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    int semp = 0;
    private HealthDataStore mStore;
    private StepCountReporter mReporter_step;
    private FloorCountReporter mReporter_floor;
    private ExerciseReporter mReporter_exercise;
    private HealthDataResolver resolver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.loading);
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        resolver = new HealthDataResolver(mStore,null);

        // = getStartTimeOfToday(0);
        //end = start + ONE_DAY_IN_MILLIS;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mStore.connectService();
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            mReporter_step = new StepCountReporter(mStore);
            mReporter_floor = new FloorCountReporter(mStore);
            mReporter_exercise = new ExerciseReporter(mStore);

            if (isPermissionAcquired()) {//승인된 상태면 바로 걸음 수를 계산

                //start = getStartTimeOfToday(i*ONE_DAY_IN_MILLIS);

                for(int i = 0 ; i<30 ; i++)//당일부터 29일 전까지 총 30일 간의 기록 0- 29
                {
                    Log.d(APP_TAG,i+" count start");
                    mReporter_step.start(mStepCountObserver,i);
                    mReporter_floor.start(mFloorCountObserver,i);
                    mReporter_exercise.start(mExerciseObserver,i);
                    Log.d(APP_TAG,i+" count end");

                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        singleton.Set30Workout();
                        Intent i = new Intent(getApplication(),AuthenticationActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);


                        //날짜 차이 구하기 샘플(나중에 디비에 사용할 코드)

                        /*try {
                            String date1 = "2019/12/14 17:37:30";
                            String date2 = "2019/10/05 08:37:30";
                            //String current = Calendar.getInstance(Locale.KOREA).getTime().toString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd ");
                            Date firstdate = format.parse(date1);
                            Date seconddate = format.parse(date2);
                            //Date currenttime = format.parse(current);
                            //Toast.makeText(getApplicationContext(),current,Toast.LENGTH_LONG).show();
                            Date currentTime = Calendar.getInstance().getTime();
                            String date_text = new SimpleDateFormat("yyyy/MM/dd ", Locale.KOREA).format(currentTime);
                            currentTime = format.parse(date_text);

                            long calDate = currentTime.getTime() - firstdate.getTime();
                            long calDateday = calDate / (24*60*60*1000);
                            calDateday = Math.abs(calDateday);
                            Log.d("tag",currentTime+ "     \n"+calDateday+"days");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("error",e.toString()+"    error....");
                        }*/

                    }

                }, 3000);

                /*;*/


            } else {//승인이 없으면 requestpermission
                if(!one)
                {
                    requestPermission(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ),1);
                }
                if(!two)
                {
                    requestPermission(new PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, PermissionType.READ),2);
                }
                if(!three)
                {
                    requestPermission(new PermissionKey(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, PermissionType.READ),3);
                }

            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
            if (!isFinishing()) {
                mStore.connectService();
            }
        }
    };

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(GetHealthData.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(GetHealthData.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private boolean isPermissionAcquired() {
        PermissionKey permKey = new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ);
        PermissionKey permKey_exercise =  new PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, PermissionType.READ);
        PermissionKey permKey_floor =  new PermissionKey(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, PermissionType.READ);

        PermissionKey keys[] = {permKey,permKey_exercise,permKey_floor};
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(Collections.singleton(permKey));
            Map<PermissionKey, Boolean> resultMap2 = pmsManager.isPermissionAcquired(Collections.singleton(permKey_exercise));
            Map<PermissionKey, Boolean> resultMap3 = pmsManager.isPermissionAcquired(Collections.singleton(permKey_floor));
            one = resultMap.get(permKey);
            two = resultMap2.get(permKey_exercise);
            three = resultMap3.get(permKey_floor);

            if(one && two && three)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {
            Log.e(APP_TAG, "Permission request fails.", e);
        }
        return false;
    }

    private void requestPermission(PermissionKey key,Integer num) {
        PermissionKey permKey = key;


        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(Collections.singleton(permKey), GetHealthData.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            updateStepCountView("");
                            showPermissionAlarmDialog();
                        } else {
                            // Get the current step count and display it
                            switch (num)
                            {
                                case 1:
                                    mReporter_step.start(mStepCountObserver,0);
                                    break;
                                case 2:
                                    String cal = HealthConstants.Exercise.CALORIE;
                                    Toast.makeText(this,cal,Toast.LENGTH_LONG).show();
                                    break;
                                case 3:
                                    String floor = HealthConstants.FloorsClimbed.FLOOR;
                                    Toast.makeText(this,floor,Toast.LENGTH_LONG).show();
                                    break;

                            }

                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }


    private StepCountReporter.StepCountObserver mStepCountObserver = count -> {
        Log.d(APP_TAG, "Step reported : " + count);
        updateStepCountView(String.valueOf(count));
    };

    private FloorCountReporter.FloorCountObserver mFloorCountObserver = count -> {
        Log.d(APP_TAG, "Step reported : " + count);
        updateStepCountView(String.valueOf(count));
    };
    private ExerciseReporter.ExerciseObserver mExerciseObserver = count -> {
        Log.d(APP_TAG, "Step reported : " + count);
        updateStepCountView(String.valueOf(count));
    };

    private void updateStepCountView(final String count) {

        runOnUiThread(() -> singleton.SetData(count));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.connect) {
            requestPermission(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ),1);
            requestPermission(new PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, PermissionType.READ),2);
            requestPermission(new PermissionKey(HealthConstants.FloorsClimbed.HEALTH_DATA_TYPE, PermissionType.READ),3);

        }

        return true;
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mRdResult =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {

                @Override
                public void onResult(HealthDataResolver.ReadResult result) {

                    try {
                        Iterator<HealthData> iterator = result.iterator();

                        if (iterator.hasNext()) {

                            HealthData data = iterator.next();
                            exkcal += data.getFloat(HealthConstants.Exercise.CALORIE);
                            Log.d(APP_TAG,"next iterator" + exkcal + "in exercise...");
                        }
                    } finally {
                        Log.d(APP_TAG,"close iterator");
                        result.close();
                    }
                }
            };

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mRdResult_step =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {

                @Override
                public void onResult(HealthDataResolver.ReadResult result) {

                    try {
                        Iterator<HealthData> iterator = result.iterator();

                        if (iterator.hasNext()) {

                            HealthData data = iterator.next();
                            exkcal += data.getFloat(HealthConstants.StepCount.CALORIE);
                            Log.d(APP_TAG,exkcal + "in stepcount...");

                        }
                    } finally {
                        //Log.d(APP_TAG,"close iterator");
                        result.close();
                    }
                }
            };

    private long getStartTimeOfToday(long day) {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Log.d(APP_TAG,today.toString()+"        123123");
        return (today.getTimeInMillis()-day);
    }
    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<HealthDataResolver.ReadResult> mRdResult_floor =
            new HealthResultHolder.ResultListener<HealthDataResolver.ReadResult>() {

                @Override
                public void onResult(HealthDataResolver.ReadResult result) {

                    try {
                        Iterator<HealthData> iterator = result.iterator();

                        if (iterator.hasNext()) {

                            HealthData data = iterator.next();
                            exkcal += data.getFloat(HealthConstants.FloorsClimbed.FLOOR)*7;
                            Log.d(APP_TAG,"next iterator" + exkcal + "in floor...");

                        }
                    } finally {
                        Log.d(APP_TAG,"close iterator");
                        result.close();
                    }
                }
            };




}
