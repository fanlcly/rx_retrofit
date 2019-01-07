package com.fancy.rxretrofit.error;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * 请求的错误信息
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public class Error {

    protected boolean success;
    private String errorMessage;


    public static <T> Error buildError(@NonNull Response<T> response) {
        try {
            Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
            if (error != null ){
                return error;  //  Error需对应服务端错误信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Error error = new Error();
        error.success = false;
        switch (response.code()) {
            case 400:
                error.errorMessage = "请求参数有误";
                break;
            case 404:
                error.errorMessage = "资源未找到";
                break;
            case 405:
                error.errorMessage = "请求方式不被允许";
                break;
            case 408:
                error.errorMessage = "请检查网络是否可用，再行尝试";// 请求超时
                break;
            case 422:
                error.errorMessage = "请求语义错误";
                break;
            case 500:
                error.errorMessage = "服务器逻辑错误";
                break;
            case 502:
                error.errorMessage = "服务器网关错误";
                break;
            case 504:
                error.errorMessage = "服务器网关超时";
                break;
            default:
                error.errorMessage = response.message();
                break;
        }
        return error;
    }


    public static Error buildError(@NonNull Throwable t) {
        Error error = new Error();
        error.success = false;
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            error.errorMessage = "请检查网络是否可用，再行尝试";// 网络无法连接
        } else if (t instanceof SocketTimeoutException) {
            error.errorMessage = "请检查网络是否可用，再行尝试";// 网络访问超时
        } else if (t instanceof RuntimeException) {
            error.errorMessage = "数据解析失败";              // 运行时异常
        } else {
            error.errorMessage = "未知错误：" + t.getLocalizedMessage();
            //响应数据格式错误
        }
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    //            200	OK	Success!
//            400	错误的请求	该请求是无效的。相应的描述信息会说明原因。
//            401	未验证	没有验证信息或者验证失败
//            403	被拒绝	理解该请求，但不被接受。相应的描述信息会说明原因。
//            404	无法找到	资源不存在，请求的用户的不存在，请求的格式不被支持。
//            405	请求方法不合适	该接口不支持该方法的请求。
//            410	已下线	请求的资源已下线。请参考相关公告。
//            429	过多的请求	请求超出了频率限制。相应的描述信息会解释具体的原因。
//            500	内部服务错误	服务器内部出错了。请联系我们尽快解决问题。
//            502	无效代理	业务服务器下线了或者正在升级。请稍后重试。
//            503	服务暂时失效	服务器无法响应请求。请稍后重试。
//            504	代理超时	服务器在运行，但是无法响应请求。请稍后重试。


}
