package com.programmerartist.artist.util.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 参数工具集
 *
 * @author 程序员Artist
 * 2019/3/27
 */
public class ParamUtil {
    private static final Logger log = LoggerFactory.getLogger(ParamUtil.class);


    // =================================================== 1，parse or format string ===================================================

    public static List<String> parseList(String value, String sep) {
        List<String> result = doParseList(value, null, sep);
        return result;
    }
    public static <T> List<T> parseList(String value, String sep, ParseType parseType) {
        return doParseList(value, parseType.getType, sep);
    }

    public static Set<String> parseSet(String value, String sep) {
        Set<String> result = doParseSet(value, null, sep);
        return result;
    }
    public static <T> Set<T> parseSet(String value, String sep, ParseType parseType) {
        return doParseSet(value, parseType.getType, sep);
    }

    public static Map<String, String> parseMap(String value, String outSep, String inSep) {
        Map<String, String> result = doParseMap(value, null, outSep, inSep);
        return result;
    }
    public static <T> Map<String, T> parseMap(String value, String outSep, String inSep, ParseType parseType) {
        return doParseMap(value, parseType.getType, outSep, inSep);
    }

    /**
     * 将从string 通过 parseMap() 得到的map再格式化回去string
     *
     * @param map
     * @param outSep
     * @param inSep
     * @param <T>
     * @return
     */
    public static <T> String formatMap(Map<String, T> map, String outSep, String inSep) {
        if(ParamUtil.isBlank(map)) return "";

        return String.join(outSep, map.entrySet().stream().map(e -> e.getKey() + inSep + e.getValue()).collect(Collectors.toList()));
    }

    /**
     *
     * @param mapStr
     * @return
     */
    public static Map<String, String> mapStrToMap(String mapStr) {
        if(ParamUtil.isBlank(mapStr) || mapStr.length()<=2) { return null; }

        mapStr = mapStr.substring(1, mapStr.length()-1);
        return parseMap(mapStr, ", ", "=");
    }

    /**
     *
     * @param map
     * @param size
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<Map<K, V>> partition(Map<K, V> map, int size) {
        List<Map<K, V>> batchMaps = new ArrayList<>();

        int index = 0;
        Map<K, V> oneMap = new HashMap<>(size);
        for(Map.Entry<K, V> entry : map.entrySet()) {
            oneMap.put(entry.getKey(), entry.getValue());
            index++;

            if(index == size) {
                batchMaps.add(oneMap);
                oneMap = new HashMap<>(size);
                index  = 0;
            }
        }

        // 最后一批（不够size条）
        if(index > 0) {
            batchMaps.add(oneMap);
        }

        return batchMaps;
    }

    /**
     *
     * @param req
     * @param name
     * @param defVal
     * @param getType
     * @param outSep
     * @param inSep
     * @return
     */
    public static Object doGet(HttpServletRequest req, String name, Object defVal,
                                     GetType getType, String outSep, String inSep) {

        String val = req.getParameter(name);
        if(isNotBlank(val)) {
            return doParse(val.trim(), getType, null, outSep, inSep, defVal);
        }else {
            return defVal;
        }
    }

    /**
     *
     * @param val
     * @param getType
     * @param outSep
     * @param inGetType
     * @param inSep
     * @param defVal
     * @return
     */
    private static Object doParse(String val, GetType getType, GetType inGetType, String outSep, String inSep, Object defVal) {
        try {
            switch (getType) {
                case SHORT:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case BOOLEAN:
                    return doParseSimple(val, getType, defVal);
                case LIST:
                    return doParseList(val, inGetType, outSep);
                case SET:
                    return doParseSet(val, inGetType, outSep);
                case MAP:
                    return doParseMap(val, inGetType, outSep, inSep);

                default:
                    throw new RuntimeException("unSupport doGetNumber, value=" + val);
            }
        } catch (NumberFormatException e) {
            log.error("ParamUtil doParse error, val=" + val, e);
            return defVal;
        }
    }

    /**
     *
     */
    enum GetType {
        SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN, LIST, SET, MAP
    }

    /**
     *
     */
    public enum ParseType {
        SHORT(GetType.SHORT), INT(GetType.INT), LONG(GetType.LONG), FLOAT(GetType.FLOAT), DOUBLE(GetType.DOUBLE), BOOLEAN(GetType.BOOLEAN);

        private GetType getType;

        ParseType(GetType getType) {
            this.getType = getType;
        }
    }



    // ================================================== 2，check blank empty =====================================

    public static boolean isBlank(String str) {
        return null==str || "".equals(str.trim());
    }
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    public static boolean isAnyBlank(String... arr) {
        if(null == arr) return true;

        for(String str : arr) {
            if(isBlank(str)) return true;
        }

        return false;
    }
    public static boolean isAllBlank(String... arr) {
        if(null == arr) return true;

        for(String str : arr) {
            if(isNotBlank(str)) return false;
        }
        return true;
    }
    public static boolean isAnyNull(Object... arr) {
        if(null == arr) return true;

        for(Object obj : arr) {
            if(null == obj) return true;
        }
        return false;
    }
    public static boolean isAllNull(Object... arr) {
        if(null == arr) return true;

        for(Object obj : arr) {
            if(null != obj) return false;
        }
        return true;
    }


    /**
     * 对集合类做是否空的判断
     *
     * @param collection
     * @return
     */
    public static boolean isBlank(Collection collection) {
        return null==collection || collection.size()==0;
    }
    public static boolean isBlank(Map map) {
        return null==map || map.size()==0;
    }
    public static boolean isNotBlank(Collection collection) {
        return !isBlank(collection);
    }
    public static boolean isNotBlank(Map map) {
        return !isBlank(map);
    }




    // ================================================== 3，assert ==============================================


    /**
     * 断言空参数, 有空参数直接抛出异常
     *
     * @param params
     */
    public static void assertBlank(Object... params) {
        if(null==params || params.length==0) throwError("Object... params is null");

        for(Object param : params) {
            if(null==param
                    || (param instanceof String && (param).equals(""))
                    || (param instanceof Collection && ((Collection) param).size() == 0)
                    || (param instanceof Map && ((Map) param).size() == 0)
                    || (param.getClass().isArray() && ((Object[]) param).length == 0)) {

                throwError(getBlankLog(params));
            }
        }
    }
    /**
     * 断言null参数, 有null参数直接抛出异常
     *
     * @param params
     */
    public static void assertNull(Object... params) {
        if(null==params || params.length==0) throwError("Object... params is null");

        for(Object param : params) {
            if(null == param) {
                throwError(getBlankLog(params));
            }
        }
    }
    /**
     * 断言空参数, 有空参数直接抛出异常
     *
     * @param append 异常详细里追加此参数
     * @param params
     */
    public static void assertBlankAppend(String append, Object... params) {

        if(containsBlank(params)) {
            throwError(append + " | " + getBlankLog(params));
        }
    }
    /**
     * 断言空参数, 有空参数 return true
     *
     * @param params
     */
    public static boolean containsBlank(Object... params) {
        if(null==params || params.length==0) return true;

        for(Object param : params) {
            if(null==param
                    || (param instanceof String && (param).equals(""))
                    || (param instanceof Collection && ((Collection) param).size() == 0)
                    || (param instanceof Map && ((Map) param).size() == 0)
                    || (param.getClass().isArray() && ((Object[]) param).length == 0)) {

                return true;
            }
        }

        return false;
    }





    // ==================================================== 4，size ===========================================

    /**
     * 求大小
     *
     * @param str
     * @return
     */
    public static int size(String str) { return null!=str ? str.length() : 0; }
    public static int size(Collection collection) { return null!=collection ? collection.size() : 0; }
    public static int size(Map map) { return null!=map ? map.size() : 0; }




    // =================================================== 5，http get ===================================================

    public static String get(HttpServletRequest req, String name) {
        return req.getParameter(name);
    }
    public static String get(HttpServletRequest req, String name, String defVal) {
        String val = req.getParameter(name);
        return isBlank(val) ? defVal : val;
    }


    public static short getShort(HttpServletRequest req, String name, short defVal) {
        return (Short) doGet(req, name, defVal, GetType.SHORT, null, null);
    }
    public static Short getShort(HttpServletRequest req, String name) {
        return (Short) doGet(req, name, null, GetType.SHORT, null, null);
    }
    public static int getInt(HttpServletRequest req, String name, int defVal) {
        return (Integer) doGet(req, name, defVal, GetType.INT, null, null);
    }
    public static Integer getInt(HttpServletRequest req, String name) {
        return (Integer) doGet(req, name, null, GetType.INT, null, null);
    }
    public static long getLong(HttpServletRequest req, String name, long defVal) {
        return (Long) doGet(req, name, defVal, GetType.LONG, null, null);
    }
    public static Long getLong(HttpServletRequest req, String name) {
        return (Long) doGet(req, name, null, GetType.LONG, null, null);
    }
    public static float getFloat(HttpServletRequest req, String name, float defVal) {
        return (Float) doGet(req, name, defVal, GetType.FLOAT, null, null);
    }
    public static Float getFloat(HttpServletRequest req, String name) {
        return (Float) doGet(req, name, null, GetType.FLOAT, null, null);
    }
    public static double getDouble(HttpServletRequest req, String name, double defVal) {
        return (Double) doGet(req, name, defVal, GetType.DOUBLE, null, null);
    }
    public static Double getDouble(HttpServletRequest req, String name) {
        return (Double) doGet(req, name, null, GetType.DOUBLE, null, null);
    }
    public static boolean getBool(HttpServletRequest req, String name, boolean defVal) {
        return (Boolean) doGet(req, name, defVal, GetType.BOOLEAN, null, null);
    }
    public static Boolean getBool(HttpServletRequest req, String name) {
        return (Boolean) doGet(req, name, null, GetType.BOOLEAN, null, null);
    }
    public static List<String> getList(HttpServletRequest req, String name, String sep, List<String> defVal) {
        return (List<String>) doGet(req, name, defVal, GetType.LIST, sep, null);
    }
    public static List<String> getList(HttpServletRequest req, String name, String sep) {
        return (List<String>) doGet(req, name, null, GetType.LIST, sep, null);
    }
    public static Set<String> getSet(HttpServletRequest req, String name, String sep, Set<String> defVal) {
        return (Set<String>) doGet(req, name, defVal, GetType.SET, sep, null);
    }
    public static Set<String> getSet(HttpServletRequest req, String name, String sep) {
        return (Set<String>) doGet(req, name, null, GetType.SET, sep, null);
    }
    public static Map<String, String> getMap(HttpServletRequest req, String name, String outSep, String inSep, Map<String, String> defVal) {
        return (Map<String, String>) doGet(req, name, defVal, GetType.MAP, outSep, inSep);
    }
    public static Map<String, String> getMap(HttpServletRequest req, String name, String outSep, String inSep) {
        return (Map<String, String>) doGet(req, name, null, GetType.MAP, outSep, inSep);
    }




    // ==================================================== 6，encode ===========================================

    /**
     *
     * @param paramValue
     * @return
     */
    public static String encodeUrl(String paramValue) {
        if(isBlank(paramValue)) { return paramValue; }
        try {
            return URLEncoder.encode(paramValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("ParamUtil encodeUrl error: paramValue=" + paramValue, e);
        }

        return paramValue;
    }

    /**
     *
     * @param paramValue
     * @return
     */
    public static String decodeUrl(String paramValue) {
        if(isBlank(paramValue)) { return paramValue; }
        try {
            return URLDecoder.decode(paramValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("ParamUtil decodeUrl error: paramValue=" + paramValue, e);
        }

        return paramValue;
    }





    // ==================================================== 7，print ===========================================

    /**
     * 打印，如果不为null，则正常打印，否则打印空字符串
     *
     * @param obj
     * @return
     */
    public static String print(Object obj) { return null!=obj ? obj.toString() : ""; }




    // =================================================== 8，安全的add put ============================================

    /**
     *
     * @param map
     * @param key
     * @param oneValue
     * @param <K>
     * @param <V>
     */
    public static <K, V> void putMapList(Map<K, List<V>> map, K key, V oneValue) {
        List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(oneValue);
    }

    /**
     *
     * @param map
     * @param key
     * @param oneValue
     * @param <K>
     * @param <V>
     */
    public static <K, V> void putMapSet(Map<K, Set<V>> map, K key, V oneValue) {
        Set<V> set = map.computeIfAbsent(key, k -> new HashSet<>());
        set.add(oneValue);
    }







    // ====================================================== tool ====================================================

    /**
     *
     * @param val
     * @param getType
     * @param defVal
     * @return
     */
    private static Object doParseSimple(String val, GetType getType, Object defVal) {
        try {
            switch (getType) {
                case SHORT:
                    return Short.parseShort(val);
                case INT:
                    return Integer.parseInt(val);
                case LONG:
                    return Long.parseLong(val);
                case FLOAT:
                    return Float.parseFloat(val);
                case DOUBLE:
                    return Double.parseDouble(val);
                case BOOLEAN:
                    return Boolean.parseBoolean(val);

                default:
                    throw new RuntimeException("unSupport doParseSimple, value=" + val);
            }
        } catch (NumberFormatException e) {
            log.error("ParamUtil doParseSimple error, val=" + val, e);
            return defVal;
        }
    }

    /**
     *
     * @param val
     * @param getType
     * @param outSep
     * @param <T>
     * @return
     */
    private static <T> List<T> doParseList(String val, GetType getType, String outSep) {
        if(null == val) { return null; }
        if("".equals(val.trim())) { return new ArrayList<>(); }

        String[] arr = val.split(outSep);
        List<T> list = new ArrayList<>(arr.length);
        for(String str : arr) {
            if(null == getType) {
                list.add((T) str);
            }else {
                T strParse = (T) doParseSimple(str, getType, null);
                if(null != strParse) { list.add(strParse); }
            }
        }
        return list;
    }

    /**
     *
     * @param val
     * @param getType
     * @param outSep
     * @param <T>
     * @return
     */
    private static <T> Set<T> doParseSet(String val, GetType getType, String outSep) {
        if(null == val) { return null; }
        if("".equals(val.trim())) { return new HashSet<>(); }

        String[] array = val.split(outSep);
        Set<T> set = new HashSet<>(array.length);
        for(String str : array) {
            if(null == getType) {
                set.add((T) str);
            }else {
                T strParse = (T) doParseSimple(str, getType, null);
                if(null != strParse) { set.add(strParse); }
            }
        }
        return set;
    }

    /**
     *
     * @param val
     * @param getType
     * @param outSep
     * @param <T>
     * @return
     */
    private static <T> Map<String, T> doParseMap(String val, GetType getType, String outSep, String inSep) {
        if(null == val) { return null; }
        if("".equals(val.trim())) { return new LinkedHashMap<>(); }

        String[] arrOut = val.split(outSep);
        Map<String, T> map = new LinkedHashMap<>(arrOut.length);
        for(String str : arrOut) {
            String[] arrIn = str.split(inSep);
            if(arrIn.length>=2) {
                if(null == getType) {
                    map.put(arrIn[0], (T) arrIn[1]);
                }else {
                    T arrIn1 = (T) doParseSimple(arrIn[1], getType, null);
                    if(null != arrIn1) { map.put(arrIn[0], arrIn1); }
                }
            }
        }
        return map;
    }

    /**
     * 抛出数据应用组相关异常
     *
     * @param append
     */
    private static void throwError(String append) {

        if(isBlank(append)) {
            throw new IllegalArgumentException();
        }else {
            throw new IllegalArgumentException(append);
        }
    }

    /**
     * 打印参数是否为空的概要,空的话[], 不空就是[..]
     *
     * @param params
     * @return
     */
    public static String getBlankLog(Object... params) {
        if(null==params || params.length==0) return "";

        StringBuilder sb = new StringBuilder();
        for(Object param : params) {
            if(null == param) {
                sb.append("null, ");
            }else {
                if(param instanceof String){
                    if(param.equals("")){
                        sb.append("\"\", ");
                    }else{
                        sb.append("String, ");
                    }
                }else if(param instanceof Collection){
                    if(((Collection) param).size() == 0){
                        sb.append("Collection[], ");
                    }else{
                        sb.append("Collection[..], ");
                    }
                }else if(param instanceof Map){
                    if(((Map) param).size() == 0){
                        sb.append("Map[], ");
                    }else{
                        sb.append("Map[..], ");
                    }
                }else if(param.getClass().isArray()){
                    if(((Object[]) param).length == 0){
                        sb.append("Array[], ");
                    }else{
                        sb.append("Array[..], ");
                    }
                }else {
                    sb.append("Object, ");
                }
            }
        }
        String sbStr = sb.toString().trim();
        if(sbStr.length() > 0) sbStr = sbStr.substring(0, sbStr.length()-1);

        return sbStr;
    }




    /**
     * test
     *
     * @param args
     */
    public static void main(String[] args) {

        Map<String, String> map1 = parseMap("vtags:1,vcluster:2,from_id:3,a:4,b:5,c:6,d:7", ",", ":");
        System.out.println(map1);
        System.out.println(formatMap(map1, ",", ":"));

        Map<String, Integer> map2 = parseMap("vtags::1,vcluster::2,from_id::3", ",", "::", ParseType.INT);
        System.out.println(map2);
        System.out.println(formatMap(map2, ",", "::"));

        Map<String, String> map3 = new HashMap<>();
        map3.put("cb", "[1,2,3]");
        map3.put("cs", "[11,22,33]");
        map3.put("u2v", "[111,222,333]");

        String str = map3.toString();
        System.out.println(str);
        System.out.println(mapStrToMap(str));

        System.out.println(ParamUtil.size("123456"));

        String print1              = null;
        String print2              = "print2";
        List<String> print3        = null;
        List<String> print4        = new ArrayList<>();
        List<String> print5        = Arrays.asList("print5,print5");
        Map<String, String> print6 = null;
        Map<String, String> print7 = new HashMap<>();
        Map<String, String> print8 = new HashMap<String, String>(){{put("a", "b");put("a1","b1");}};

        System.out.println(ParamUtil.print(print1));
        System.out.println(ParamUtil.print(print2));
        System.out.println(ParamUtil.print(print3));
        System.out.println(ParamUtil.print(print4));
        System.out.println(ParamUtil.print(print5));
        System.out.println(ParamUtil.print(print6));
        System.out.println(ParamUtil.print(print7));
        System.out.println(ParamUtil.print(print8));

        Map<Integer, Integer> map = new HashMap<Integer, Integer>(){{
            put(1, 1);
            put(2, 2);
            put(3, 3);
            put(4, 4);
            put(5, 5);
            put(6, 6);
            put(7, 7);
            put(8, 8);
            put(9, 9);
            put(10, 10);
        }};

        List<Map<Integer, Integer>> batchMaps = partition(map, 1);
        List<Map<Integer, Integer>> batchMaps2 = partition(map, 2);
        List<Map<Integer, Integer>> batchMaps3 = partition(map, 3);
        List<Map<Integer, Integer>> batchMaps10 = partition(map, 10);
        List<Map<Integer, Integer>> batchMaps12 = partition(map, 12);

        System.out.println(batchMaps);
        System.out.println(batchMaps2);
        System.out.println(batchMaps3);
        System.out.println(batchMaps10);
        System.out.println(batchMaps12);
    }
}
