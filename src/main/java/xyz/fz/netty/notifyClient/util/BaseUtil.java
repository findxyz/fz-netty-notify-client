package xyz.fz.netty.notifyClient.util;

import com.google.gson.Gson;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static xyz.fz.netty.notifyClient.util.Constants.DELIMITER_STR;

public class BaseUtil {

    public static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(2);

    private static Gson gson = new Gson();

    public static <T> T parseJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    // done 防止误用，屏蔽为private
    private static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static String toDelimiterJson(Object o) {
        return gson.toJson(o) + DELIMITER_STR;
    }
}
