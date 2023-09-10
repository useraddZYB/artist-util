package com.programmerartist.artist.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author 程序员Artist
 * @date 2019/3/26
 */
public class MachineUtil {
    private static final Logger log = LoggerFactory.getLogger(MachineUtil.class);

    private static String host = null;
    private static int port    = -1;

    /**
     *
     * @return
     */
    public static String getHost() {
        if(null != host) {
            return host;
        }else {
            try {
                host = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                host = "1_1_1_1";
                log.error("getHost error, ", e);
            }

            return host;
        }
    }


    /**
     *
     * @param name
     * @return
     */
    public static String getEnv(String name) {
        String value = "";
        try {
            value = System.getenv(name);
        } catch (Throwable e) {
            log.error("getEnv error, ", e);
        }

        return value;
    }
}
