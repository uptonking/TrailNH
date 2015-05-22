package com.kingoo.nhtrail;

/**
 * Description 自定义算法类：GPS坐标偏移
 * @author ZHX
 * @date 2014/4/11
 */
public class GpsCorrect {
	final static double pi = 3.14159265358979324;  
    final static double a = 6378245.0;  
    final static double ee = 0.00669342162296594323;

    /**
     * 描述：纠正经纬度的方法
     * @param wgLat 原始纬度
     * @param wgLon 原始经度
     * @param latlng 返回的经纬度
     */
    public static void transform(double wgLat, double wgLon, double[] latlng) {  
        if (outOfChina(wgLat, wgLon)) {  
            latlng[0] = wgLat;  
            latlng[1] = wgLon;  
            return;  
        }  
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);  
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);  
        double radLat = wgLat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        latlng[0] = wgLat + dLat;  
        latlng[1] = wgLon + dLon;  
    }

    /**
     * 描述：判断是否在中国境内
     * @param lat 纬度
     * @param lon 经度
     * @return 是否在中国境内
     */
    private static boolean outOfChina(double lat, double lon) {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }

    /**
     * 描述：转换纬度
     * @param x
     * @param y
     * @return 转换后的纬度
     */
    private static double transformLat(double x, double y) {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }

    /**
     * 描述：转换经度
     * @param x
     * @param y
     * @return 转换后的经度
     */
    private static double transformLon(double x, double y) {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;  
        return ret;  
    }
}
