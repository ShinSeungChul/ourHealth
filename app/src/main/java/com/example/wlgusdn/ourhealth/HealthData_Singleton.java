package com.example.wlgusdn.ourhealth;

import java.util.ArrayList;

public class HealthData_Singleton {
    private static final HealthData_Singleton ourInstance = new HealthData_Singleton();
    private int kcal;//samsung
    private int daily;//aws maxkcal
    private int eatkcal;//aws eatkcalsum
    private ArrayList<Entrydata> daylist;
    private ArrayList<Entrydata> weeklist;
    private ArrayList<Entrydata> monthlist;

    public static HealthData_Singleton getInstance() {
        return ourInstance;
    }

    public void SetData(String data)
    {
        kcal = Integer.valueOf(data);
    }
    public void SetDayList(Entrydata entrydata)
    {
        daylist.add(entrydata);
    }
    public int getKcal() {

        return kcal;


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
        float sum = eatkcal - kcal;
        if(eatkcal !=0)
            return (sum/daily)*100;
        else
            return 0;
    }
    public ArrayList<Entrydata> getDaylist() {
        return daylist;
    }

    private HealthData_Singleton() {
    }
}
