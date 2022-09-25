package top.seiei.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "返回信息VO", description = "所有响应都会返回这个ServerResponse对象")
public class ServerResponse<T> implements Serializable {

    @ApiModelProperty(value = "返回信息数据")
    private T data;
    @ApiModelProperty(value = "返回信息状态码")
    private int status;
    @ApiModelProperty(value = "返回信息文本")
    private String msg;
    public T getData() {
        return data;
    }
    public int getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    private ServerResponse(int status) {
        this.status = status;
    }
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ServerResponse<T> createdBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createdBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createdBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createdBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createdByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDec());
    }

    public static <T> ServerResponse<T> createdByError(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createdByError(T data) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), data);
    }

    public static <T> ServerResponse<T> createdByError(int status, String msg) {
        return new ServerResponse<T>(status, msg);
    }

    public static void main(String[] args) {
        ServerResponse<String> serverResponse = ServerResponse.createdBySuccess();
        ServerResponse<Object> serverResponse2 = ServerResponse.createdBySuccess();
        serverResponse = ServerResponse.createdBySuccess();
    }
}
