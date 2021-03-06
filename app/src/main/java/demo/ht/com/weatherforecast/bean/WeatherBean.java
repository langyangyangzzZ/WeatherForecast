package demo.ht.com.weatherforecast.bean;

public class WeatherBean {

    @Override
    public String toString() {
        return "WeatherBean{" +
                "weatherinfo=" + weatherinfo +
                '}';
    }

    /**
     * weatherinfo : {"city":"太原","cityid":"101100101","temp":"25.9","WD":"东北风","WS":"小于3级","SD":"22%","AP":"919.9hPa","njd":"暂无实况","WSE":"<3","time":"17:55","sm":"1.3","isRadar":"1","Radar":"JC_RADAR_AZ9351_JB"}
     */


    private WeatherinfoBean weatherinfo;

    public WeatherinfoBean getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherinfoBean weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    public static class WeatherinfoBean {
        @Override
        public String toString() {
            return "WeatherinfoBean{" +
                    "city='" + city + '\'' +
                    ", cityid='" + cityid + '\'' +
                    ", temp='" + temp + '\'' +
                    ", WD='" + WD + '\'' +
                    ", WS='" + WS + '\'' +
                    ", SD='" + SD + '\'' +
                    ", AP='" + AP + '\'' +
                    ", njd='" + njd + '\'' +
                    ", WSE='" + WSE + '\'' +
                    ", time='" + time + '\'' +
                    ", sm='" + sm + '\'' +
                    ", isRadar='" + isRadar + '\'' +
                    ", Radar='" + Radar + '\'' +
                    '}';
        }

        /**
         * city : 太原
         * cityid : 101100101
         * temp : 25.9
         * WD : 东北风
         * WS : 小于3级
         * SD : 22%
         * AP : 919.9hPa
         * njd : 暂无实况
         * WSE : <3
         * time : 17:55
         * sm : 1.3
         * isRadar : 1
         * Radar : JC_RADAR_AZ9351_JB
         */


        private String city;
        private String cityid;
        private String temp;
        private String WD;
        private String WS;
        private String SD;
        private String AP;
        private String njd;
        private String WSE;
        private String time;
        private String sm;
        private String isRadar;
        private String Radar;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWD() {
            return WD;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public String getWS() {
            return WS;
        }

        public void setWS(String WS) {
            this.WS = WS;
        }

        public String getSD() {
            return SD;
        }

        public void setSD(String SD) {
            this.SD = SD;
        }

        public String getAP() {
            return AP;
        }

        public void setAP(String AP) {
            this.AP = AP;
        }

        public String getNjd() {
            return njd;
        }

        public void setNjd(String njd) {
            this.njd = njd;
        }

        public String getWSE() {
            return WSE;
        }

        public void setWSE(String WSE) {
            this.WSE = WSE;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSm() {
            return sm;
        }

        public void setSm(String sm) {
            this.sm = sm;
        }

        public String getIsRadar() {
            return isRadar;
        }

        public void setIsRadar(String isRadar) {
            this.isRadar = isRadar;
        }

        public String getRadar() {
            return Radar;
        }

        public void setRadar(String Radar) {
            this.Radar = Radar;
        }
    }
}
