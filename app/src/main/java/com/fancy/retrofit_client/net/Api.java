package com.fancy.retrofit_client.net;


/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public class Api {
    private static Api singleHttp = null;

    private Api() {
    }

    public static Api getInstance() {
        if (singleHttp == null) {
            synchronized (Api.class) {
                if (singleHttp == null) {
                    singleHttp = new Api();
                }
            }
        }
        return singleHttp;
    }


    public ApiService getRetrofitService() {

//        Retrofit retrofit = HttpClient.getInstance().getRetrofit("http://api.laifudao.com/"
//                , new TokenInterceptor(),
//                true);
//
//        return retrofit.create(ApiService.class);
        return null;
    }

}
