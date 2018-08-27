package com.example.administrator.school_design.diy_item;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 * http连接的架包
 */

public class http_connect {
    private static final int TIMEOUT = 10000;

    /**第一
     * 传送文本,例如Json,xml等
     */
    public static String sendTxt(String urlPath, String txt, String encoding)
            throws Exception {
        byte[] sendData = txt.getBytes();
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TIMEOUT);
        // 如果通过post提交数据，必须设置允许对外输出数据
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setRequestProperty("Content-Length", String
                .valueOf(sendData.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(sendData);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            in.close();
            return responseData;
        }
        return "sendText error!";
    }




    /**第二
     * 上传文件
     */
    public static String sendFile(String urlPath, String filePath,
                                  String newName) throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        URL url = new URL(urlPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		/* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
		/* 设置传送的method=POST */
        con.setRequestMethod("POST");
		/* setRequestProperty */

        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + boundary);
		/* 设置DataOutputStream */
        DataOutputStream ds = new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "
                + "name=\"file1\";filename=\"" + newName + "\"" + end);
        ds.writeBytes(end);

		/* 取得文件的FileInputStream */
        FileInputStream fStream = new FileInputStream(filePath);
		/* 设置每次写入1024bytes */
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = -1;
		/* 从文件读取数据至缓冲区 */
        while ((length = fStream.read(buffer)) != -1) {
			/* 将资料写入DataOutputStream中 */
            ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

		/* close streams */
        fStream.close();
        ds.flush();

		/* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }
		/* 关闭DataOutputStream */
        ds.close();
        return b.toString();
    }


    /**
     * 通过get方式提交参数给服务器
     */
    public static String sendGetRequest(String urlPath,
                                        Map<String, String> params, String encoding) throws Exception {

        // 使用StringBuilder对象
        StringBuilder sb = new StringBuilder(urlPath);
        sb.append('?');

        // 迭代Map
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append('=').append(
                    URLEncoder.encode(entry.getValue(), encoding)).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);
        // 打开链接
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setConnectTimeout(TIMEOUT);
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据

            BufferedReader in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;

            }
            System.out.println(responseData);
            in.close();
            return responseData;
        }
        return "sendGetRequest error!";

    }


    /**
     * 通过Post方式提交参数给服务器,也可以用来传送json或xml文件
     */
    public static String sendPostRequest(String urlPath,
                                         Map<String, String> params, String encoding) throws Exception {
        StringBuilder sb = new StringBuilder();
        // 如果参数不为空
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // Post方式提交参数的话，不能省略内容类型与长度
                sb.append(entry.getKey()).append('=').append(
                        URLEncoder.encode(entry.getValue(), encoding)).append(
                        '&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        // 得到实体的二进制数据
        byte[] entitydata = sb.toString().getBytes();
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TIMEOUT);
        // 如果通过post提交数据，必须设置允许对外输出数据
        conn.setDoOutput(true);
        // 这里只设置内容类型与内容长度的头字段
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setRequestProperty("Content-Length", String
                .valueOf(entitydata.length));
        OutputStream outStream = conn.getOutputStream();
        // 把实体数据写入是输出流
        outStream.write(entitydata);
        // 内存中的数据刷入
        outStream.flush();
        outStream.close();
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据
            System.out.println("ok");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            in.close();
            return responseData;
        }
        return "sendText error!";
    }

    /**
     * 根据URL直接读文件内容，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
     */
    public static String readTxtFile(String urlStr, String encoding)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            // 创建一个URL对象
            URL url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(urlConn
                    .getInputStream(), encoding));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
     */
    public static int downloadFile(String urlStr, String path, String fileName)
            throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = getInputStreamFromUrl(urlStr);
            File resultFile = write2SDFromInput(path, fileName, inputStream);
            if (resultFile == null) {
                return -1;
            }

        } catch (Exception e) {
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                throw e;
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String urlStr)
            throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    private static File write2SDFromInput(String directory, String fileName,
                                          InputStream input) {
        File file = null;
        String SDPATH = Environment.getExternalStorageDirectory().toString();
        FileOutputStream output = null;
        File dir = new File(SDPATH + directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file = new File(dir + File.separator + fileName);
            file.createNewFile();
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
