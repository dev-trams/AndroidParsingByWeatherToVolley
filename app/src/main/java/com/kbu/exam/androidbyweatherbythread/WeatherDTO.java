package com.kbu.exam.androidbyweatherbythread;

public class WeatherDTO {
    private String country;
    private String flag;
    private String weather;
    private String temperature;

    public WeatherDTO(String country, String flag, String weather, String temperature) {
        this.country = country;
        this.flag = flag;
        this.weather = weather;
        this.temperature = temperature;
    }
    public WeatherDTO(){};

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
