package com.kingoo.nhtrail.weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Description 自定义类：将
 * @author KINGOO
 * @date 2015/4/25
 */
public class HttpUtils {
    /**
     * Description 方法：利用URL建立HTTP连接，获取返回的json数据
     * @param url_path 百度天气接口URL
     * @return String 天气数据字符串
     */
    public static String getJsonContent(String url_path) {
        try {
            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//获取URLConnection对象
            connection.setConnectTimeout(3000);//设置连接超时为3s
            connection.setRequestMethod("GET");//向远程HTTP服务器发送请求的方式设置为get
            connection.setDoInput(true);//URL 连接可用于输入和/或输出。如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 true。

            int code = connection.getResponseCode();//获取HTTP服务器返回的响应码，无响应会返回-1；响应码为HTTP.OK，即200，表示一切正常，对GET和POST请求的应答文档跟在后面
            if (code == HttpURLConnection.HTTP_OK) {
                return changeInputStream(connection.getInputStream());//getInputStream()返回能从URL指向的数据源读取数据的输入流
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "";
    }

    private static String changeInputStream(InputStream inputStream) {
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//ByteArrayOutputStream把内存中的数据读到字节数组中,而ByteArrayInputStream又把字节数组中的字节以流的形式读出,实现了对同一个字节数组的操作.
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) { //读取到字节数据的末尾会返回-1
                outputStream.write(data, 0, len);
            }
            jsonString = new String(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        }
        return jsonString;
    }

}
