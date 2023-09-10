package com.programmerartist.artist.util.datastructure;


/**
 * 带描述的字段
 * 用于画页面等需要用到字段备注描述的场景
 *
 * @author 程序员Artist
 * 2020/3/18
 */
public class DescField {

    private String name;
    private Object value;
    private String desc;

    public DescField(String name, Object value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
