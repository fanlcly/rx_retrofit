package com.fancy.retrofit_client.net;

import com.fancy.retrofit_client.entity.ListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public interface ApiService {

    /**
     * 统计表数据
     */
    @GET("open/xiaohua.json")
    Observable<Response<List<ListEntity>>> totalEmployInfo();
}
