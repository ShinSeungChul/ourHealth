package com.example.wlgusdn.ourhealth;

public class HealthData {

    private static HealthData instance = new HealthData();
    private String kcal = null ;

    // Getter-Setters
    public static HealthData getInstance() {
        return instance;
    }

    public static void setInstance(HealthData instance) {
        HealthData.instance = instance;
    }

        public void SetKcal(String kcal)
    {
        this.kcal = kcal;
    }
    public String GetKcal()
    {
        return kcal;
    }

}
