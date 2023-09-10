package com.programmerartist.artist.util.limiter;

import java.util.Map;

/**
 * 限流
 *
 * @author 程序员Artist
 * @date 2019/8/12
 */
public interface Limiter {

    /**
     * 返回值：true被限流，false正常放行
     *
     * @param service
     * @return
     */
    boolean qpsLimit(String service);

    /**
     * 返回值：在限流范围内能取到的大小，比如：限制100，若只有1qps下，传了10则返回10，传了110则返回100
     *
     * @param service
     * @param size
     * @return
     */
    int sizeLimit(String service, int size);

    /**
     * 回调函数：更新了限流配置后的回调
     *
     * @param service2Max
     * @param othersMax
     */
    void maxUpdate(Map<String, Integer> service2Max, Integer othersMax);

}
