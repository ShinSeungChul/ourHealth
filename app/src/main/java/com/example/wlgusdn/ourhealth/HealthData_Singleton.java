package com.example.wlgusdn.ourhealth;

import android.util.Log;
import com.example.wlgusdn.ourhealth.Day30List;
import com.example.wlgusdn.health.TodayWorkoutList;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class HealthData_Singleton {
    private static final HealthData_Singleton ourInstance = new HealthData_Singleton();
    private int samsungkcal;//samsung

    private int daily;//aws maxkcal
    private int eatkcal;//aws eatkcalsum

    private ArrayList<BarEntry> daylist;
    private ArrayList<Entrydata> weeklist;
    private ArrayList<Entrydata> monthlist;

    private ArrayList<TodayKcalList> todayKcalLists;
    private ArrayList<TodayWorkoutList> todayWorkoutLists;
    private Day30List day30Lists[];//dat30list workout -> chartdata
    private int steplist[] = new int[30];//for day30list
    private int floorlist[] = new int[30];//for day30list
    private int exerciselist[] = new int[30];//for day30list
    public static HealthData_Singleton getInstance() {
        Log.d("tag", "get instance");
        return ourInstance;
    }

    public void TimeToHour(String time,String kcal)
    {
        String hour = time.substring(0,2);
        float hour_float = Float.parseFloat(hour);//ex) 09:00 -> 09 -> 09.00
        float kcal_float = Float.parseFloat(kcal);//ex) 864 -> 864.00
        daylist.add(new BarEntry(hour_float,kcal_float));


    }
    public void SetWorkout_data(int index,int kcal)
    {
        switch (index)
        {
            case 0:
                for(int i = 0 ; i< 30 ; i++)
                {
                    if(steplist[i] == -1)
                    {
                        steplist[i] = kcal;
                        break;
                    }
                }
                break;
            case 1:
                for(int i = 0 ; i< 30 ; i++)
                {
                    if(floorlist[i] == -1)
                    {
                        floorlist[i] = kcal;
                        break;
                    }
                }
                break;
            case 2:
                for(int i = 0 ; i< 30 ; i++)
                {
                    if(exerciselist[i] == -1)
                    {
                        exerciselist[i] = kcal;
                        break;
                    }
                }
                break;
        }
    }
    public void Set30Workout()
    {
        for(int i = 0 ; i< day30Lists.length ; i++)
        {
            day30Lists[i].setWorkoutKcal(steplist[i]);
            day30Lists[i].setWorkoutKcal(floorlist[i]);
            day30Lists[i].setWorkoutKcal(exerciselist[i]);
            Log.d("result","result "+i+"  "+day30Lists[i].getWorkoutKcal());
        }
        samsungkcal = day30Lists[0].getWorkoutKcal();


    }

    public Day30List[] getDay30Lists() {
        return day30Lists;
    }

    public void SetData(String data)
    {
        samsungkcal = Integer.valueOf(data);
    }

    //it just call once when you launch the app... if you want add only a data you use another method :>
    //현우가 이거를 써야되는데 aws 에서 데이터를 받아올 떄 array 형태면 쓸수 있는데 단일 데이터면 다시 함수 만들어야함
    //json -> array -> ok          json -> data -> no
    public void SetTodayKcalList(ArrayList<TodayKcalList> item)
    {
        for(int i = 0 ; i < item.size() ; i++)
        {
            todayKcalLists.add(item.get(i));//add to list todaykcallist (for listview...)
            TimeToHour(item.get(i).getTime(),item.get(i).getKcal().toString());//also add to list dailylist (for chart...)
        }

    }

    public ArrayList<TodayKcalList> GetTodayKcalList()
    {
        return todayKcalLists;
    }
    public int getKcal() {

        return samsungkcal;


    }
    public int getDaily() {
        if(daily != 0 )
            return daily;
        else
            return 2700;
    }
    public int getEatkcal() {
        if(eatkcal !=0)
            return eatkcal;
        else
            return 1800;
    }
    public float Progress()
    {
        float sum = eatkcal - samsungkcal;
        if(eatkcal !=0)
            return (sum/daily)*100;
        else
            return 0;
    }
    public ArrayList<BarEntry> getDaylist() {
        return daylist;
    }

    private HealthData_Singleton() {

        day30Lists = new Day30List[30];
        for(int i = 0 ; i < 30 ; i++)
        {
            day30Lists[i] = new Day30List();
            exerciselist[i] = -1;
            floorlist[i] = -1;
            steplist[i] = -1;

        }

    }
}
