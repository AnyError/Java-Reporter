package com.anyerror;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Random;

public class AnyError {

    public static String project_key = null;
    private static Random generator2 = new Random(19580427);
    private static String code_version = "Undefined";


    public static void notify(String message, HashMap data, Throwable throwable) {
        HashMap map = new HashMap();
        if (message != null) {
            map =  anyErrorHeader(message);
        } else {
            if (throwable != null)
                map = anyErrorHeader(throwable.getMessage());
            else
                map =  anyErrorHeader("Error Message Not found");
        }
        HashMap details = new HashMap();

        map.put("Logging Details", data);
        map.put("Jvm Information", jvmInfo());
        map.put("Host Information", hostInfo());
        map.put("Env Information", envInfo());
        map.put("Exception Details", exceptionInfo(throwable));

//        map.put("details",details);

        AnyError.sendData(map);
    }

    public static void notify(Throwable exception) {
        try {

            HashMap map = new HashMap();
            map = anyErrorHeader(getExceptionMessage(exception));
            HashMap details = new HashMap();
            map.put("Jvm Information", jvmInfo());
            map.put("Host Information", hostInfo());
            map.put("Env Information", envInfo());
            map.put("Exception Details", exceptionInfo(exception));
//            map.put("details",details);

            AnyError.sendData(map);

        }
        catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    private static String getExceptionMessage(Throwable exception)
    {
            return exception.toString();
    }
    

    private static HashMap anyErrorHeader(String message) {
        HashMap map = new HashMap();
        map.put("message", message);
        map.put("project", project_key);
        map.put("language", "Java");
        map.put("code_version", code_version);
        map.put("uid", java.util.UUID.randomUUID().toString());
        return map;
    }

    private static String stackData(Throwable t) {
        String result = "";
        if (t == null) {
            return result;
        }

        StackTraceElement[] stack = t.getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            result += stack[i].getClassName() + "." + stack[i].getMethodName() +
                    "(" + stack[i].getLineNumber() + ") - " +
                    stack[i].getFileName() + "\n";
        }

        return result;
    }

    private static void sendData(HashMap map) {
        try {
            JSONObject js = new JSONObject(map);
            // Construct data
            String data = URLEncoder.encode("error", "UTF-8") + "=" +
                    URLEncoder.encode(js.toString(), "UTF-8");
            data += "&" + URLEncoder.encode("format", "UTF-8") + "=" +
                    URLEncoder.encode("json", "UTF-8");

            // Send data
            URL url = new URL("http://www.ae.com/api/1/log_error");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
            }
            wr.close();
            rd.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    private static HashMap jvmInfo() {
        HashMap jvm = new HashMap();
        jvm.put("totalMemory", Runtime.getRuntime().totalMemory());
        jvm.put("maxMemory", Runtime.getRuntime().maxMemory());
        jvm.put("freeMemory", Runtime.getRuntime().freeMemory());
        jvm.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        return jvm;
    }

    private static HashMap hostInfo() {
        HashMap env = new HashMap();
        try {

            InetAddress addr = InetAddress.getLocalHost();

            byte[] ipAddr = addr.getAddress();
            // Convert to dot representation
            String ipAddrStr = "";
            for (int i = 0; i < ipAddr.length; i++) {
                if (i > 0) {
                    ipAddrStr += ".";
                }
                ipAddrStr += ipAddr[i] & 0xFF;
            }

            String hostname = addr.getHostName();
            env.put("hostname", hostname);
            env.put("ipaddress", ipAddrStr);
        }
        catch (Exception eeee) {

        }
        return env;
    }

    private static HashMap envInfo() {
        HashMap env = new HashMap();

        Properties pr = System.getProperties();
        TreeSet propKeys = new TreeSet(pr.keySet());  // TreeSet sorts keys
        for (Iterator it = propKeys.iterator(); it.hasNext();) {
            String key = (String) it.next();
            env.put(key, pr.get(key));
        }
        return env;
    }

    private static HashMap exceptionInfo(Throwable thrown) {
        HashMap exception_details = new HashMap();
        exception_details.put("Stack Trace", stackData(thrown));
        return exception_details;
    }

}

