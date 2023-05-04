package com.hnzz.commons.base.result;

/**
 * @author HB
 * @Classname Result
 * @Date 2023/1/3 15:27
 * @Description TODO
 */
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 执行成功返回结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodes.SUCCESS, data);
    }
    /**
     * 执行失败返回结果
     */
    public static <T> Result<T> error() {
        return new Result<>(ResultCodes.ERROR, null);
    }
    /**
     * 自定义返回结果
     */
    public static <T> Result<T> customize(Integer code, String message, T data){
        return new Result<>(code, message,data);
    }
    /**
     * 根据结果枚举类返回结果
     */
    public static <T> Result<T> withResultCodes(ResultCodes code,T data){
        return new Result<>(code,data);
    }
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(ResultCodes code,T data){
        this.code = code.getCode();
        this.message = code.getMsg();
        this.data = data;
    }

}
