package com.sptan.exec.rediscache.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Result entity.
 *
 * @param <T> the type parameter
 * @author liupeng
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("通用返回类型")
public class ResultEntity<T> {

    /**
     * The constant SUCCESS.
     */
    public static final int SUCCESS = 200;

    public static final String MSG_SUCCESS = "SUCCESS";

    @ApiModelProperty("返回值代码. 200:成功, 4XX:客户端错误, 5XX:服务端错误")
    private int code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回值")
    private T data;

    /**
     * Ok result entity.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the result entity
     */
    public static <T> ResultEntity<T> ok(T data) {
        ResultEntity<T> result = new ResultEntity<>(SUCCESS, MSG_SUCCESS, data);
        return result;
    }


    public static <T> ResultEntity<T> err(int errorCode, String message) {
        ResultEntity<T> result = new ResultEntity<>(errorCode, message, null);
        return result;
    }
}
