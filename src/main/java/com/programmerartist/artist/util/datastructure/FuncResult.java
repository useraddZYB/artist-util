package com.programmerartist.artist.util.datastructure;


/**
 * 普通方法，"复杂" 返回值
 *
 * @author 程序员Artist
 * @date 2019/5/28 19:30:00
 */
public class FuncResult {

    private boolean success;
    private String successDesc = "";
    private Object data;
    private String failedReason = "";

    /**
     *
     * @param success
     * @param successDesc
     * @param data
     * @param failedReason
     */
    public FuncResult(boolean success, String successDesc, Object data, String failedReason) {
        this.success = success;
        this.successDesc = successDesc;
        this.data = data;
        this.failedReason = failedReason;
    }


    public static FuncResult newDefault() { return new FuncResult(true, "", null, ""); }
    public static FuncResult newSuccess() { return new FuncResult(true, "", null, ""); }
    public static FuncResult newSuccess(String successDesc) { return new FuncResult(true, successDesc, null, ""); }
    public static FuncResult newSuccess(String successDesc, Object data) { return new FuncResult(true, successDesc, data, ""); }

    public static FuncResult newFailed() { return new FuncResult(false, "", null, ""); }
    public static FuncResult newFailed(String failedReason) { return new FuncResult(false, "", null, failedReason); }


    /**
     * 链式set
     *
     * @param success
     * @return
     */
    public FuncResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }
    public FuncResult setSuccessDesc(String successDesc) {
        this.successDesc = successDesc;
        return this;
    }
    public FuncResult setData(Object data) {
        this.data = data;
        return this;
    }
    public FuncResult setFailedReason(String failedReason) {
        this.failedReason = failedReason;
        return this;
    }

    public FuncResult appendSuccessDesc(String successDesc) {
        this.successDesc += successDesc + "; ";
        return this;
    }
    public FuncResult appendFailedReason(String failedReason) {
        this.failedReason += failedReason + "; ";
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSuccessDesc() {
        return successDesc;
    }

    public Object getData() {
        return data;
    }

    public String getFailedReason() {
        return failedReason;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "FuncResult{" +
                "success=" + success +
                ", successDesc='" + successDesc + '\'' +
                ", data=" + (null!=data ? data : "") +
                ", failedReason='" + failedReason + '\'' +
                '}';
    }


}
