package com.example.wlgusdn.ourhealth;

import android.util.Log;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
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
    public ArrayList<TodayKcalList> todayKcalLists;
    private ArrayList<TodayWorkoutList> todayWorkoutLists;
    private Day30List day30Lists[];//dat30list workout -> chartdata
    public ArrayList<AllFood> allFoods;

    private int steplist[] = new int[30];//for day30list
    private int floorlist[] = new int[30];//for day30list
    private int exerciselist[] = new int[30];//for day30list

    public AWSAppSyncClient mClient;
    public ClientData clientData;

    public int GetTodayEatKcal()
    {
        int total = 0;
        for(int i = 0 ; i < todayKcalLists.size() ; i++)
        {
            total += todayKcalLists.get(i).getKcal();

        }
        return total;
    }
    public static HealthData_Singleton getInstance() {
        Log.d("tag", "get instance");
        return ourInstance;
    }
    public void SetClient(AWSAppSyncClient client)
    {
        mClient = client;
    }


    public void Compute_balance()
    {
        float totalfat=0;
        float totalpro=0;
        float totalcar=0;
        float totalcal=0;
        int breakfast = 0;
        int dinner = 0;
        int launch = 0;
        int extracount = 0;
        for(int i = 0 ; i <30 ; i++)//0이 오래된거
        {
            totalfat += day30Lists[i].getFat();
            totalcar += day30Lists[i].getCarbohydrate();
            totalpro += day30Lists[i].getProtein();
            totalcal += day30Lists[i].getKcal();

            int time = Integer.parseInt(day30Lists[i].getTime());

            if(time>=04 && time <=10)
            {
                breakfast += time;
            }
            else if(time >=11 && time <= 16)
            {
                launch += time;
            }
            else if(time >= 17 && time <=21)
            {
                dinner += time;
            }
            else
            {
                extracount++;
            }


        }
        totalfat = totalfat/30;
        totalcal = totalcal/30;
        totalcar = totalcar/30;
        totalpro = totalpro/30;

        float fatpercent = (totalfat/(totalcar+totalfat+totalpro))*100;
        float carpercent = (totalcar/(totalcar+totalfat+totalpro))*100;
        float propercent = (totalcar/(totalcar+totalfat+totalpro))*100;
        clientData.setCarper(carpercent);
        clientData.setFatper(fatpercent);
        clientData.setProper(propercent);
        clientData.setTcar(totalcar);
        clientData.setTfat(totalfat);
        clientData.setTpro(totalpro);
        clientData.setCalavg(totalcal);

        breakfast = breakfast/30;
        launch = launch/30;
        dinner = dinner/30;

        clientData.setBreakfasttime(breakfast);
        clientData.setLaunchtime(launch);
        clientData.setDinnertime(dinner);
        clientData.setExtracount(extracount);



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
        float sum = GetTodayEatKcal() - samsungkcal;
        if(GetTodayEatKcal()>samsungkcal)
            return (sum/clientData.getKcal())*100;
        else
            return 0;
    }


    private HealthData_Singleton() {

        todayKcalLists = new ArrayList();
        day30Lists = new Day30List[30];
        allFoods = new ArrayList();
        clientData = new ClientData("","",0,0,0,0,0,0,0);
        for(int i = 0 ; i < 30 ; i++)
        {
            day30Lists[i] = new Day30List();
            exerciselist[i] = -1;
            floorlist[i] = -1;
            steplist[i] = -1;

        }

    }

}
