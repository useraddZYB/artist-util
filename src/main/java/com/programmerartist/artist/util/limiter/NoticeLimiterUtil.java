package com.programmerartist.artist.util.limiter;

import java.util.ArrayList;
import java.util.List;

/**
 * 频控简单工具类
 *
 * @author 程序员Artist
 * @date 2022-12-19
 */
public class NoticeLimiterUtil {

    private static final double DEDUP_RATIO = 0.85;

    /**
     * 判同：采用字符的严格匹配，按匹配率是否满足阈值
     *
     * @param key
     * @param content
     * @return
     */
    public static boolean isDedup(String key, String content) {
        int minLen = Math.min(key.length(), content.length());

        key     = key.substring(0, minLen);
        content = content.substring(0, minLen);

        char[] keyChar     = key.toCharArray();
        char[] contentChar = content.toCharArray();

        List<String> keyCharList = new ArrayList<>();
        for(char keyC : keyChar) { keyCharList.add(String.valueOf(keyC)); }

        List<String> contentCharList = new ArrayList<>();
        for(char contentC : contentChar) { contentCharList.add(String.valueOf(contentC)); }

        contentCharList.retainAll(keyCharList);
        double dedupRatio = (contentCharList.size()*1.0) / keyCharList.size();

        return dedupRatio >= DEDUP_RATIO;
    }
}
