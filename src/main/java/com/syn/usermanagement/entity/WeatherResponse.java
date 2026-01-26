package com.syn.usermanagement.entity;

import java.util.ArrayList;

public class WeatherResponse {
    public class AirQuality{
        public String co;
        public String no2;
        public String o3;
        public String so2;
        public String pm2_5;
        public String pm10;
    }

    public class Astro{
        public String sunrise;
        public String sunset;
        public String moonrise;
        public String moonset;

    }

    public class Current{
        public String observation_time;
        public int temperature;
        public int weather_code;
        public Astro astro;
        public AirQuality air_quality;
        public int wind_speed;
        public int wind_degree;
        public String wind_dir;
        public int pressure;
        public double precip;
        public int humidity;
        public int cloudcover;
        public int feelslike;
        public int uv_index;
        public int visibility;
        public String is_day;
    }

    public class Location{
        public String name;
        public String country;
        public String region;
        public String lat;
        public String lon;
        public String timezone_id;
        public String localtime;
        public int localtime_epoch;
        public String utc_offset;
    }

    public class Request{
        public String type;
        public String query;
        public String language;
        public String unit;
    }

    public class Root{
        public Request request;
        public Location location;
        public Current current;
    }
}
