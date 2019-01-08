# rx_retrofit

rxjava2.0和retrofit2.0兼容的网络框架,即能使用仅基于retrofit的封装，又可以使用基本retrofit+rxjava的封装
------

# 使用

1.将其添加到project的build.gradle中。

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

或者添加maven

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

2.添加依赖到APP的build.gradle中。

	dependencies {
		implementation 'com.github.fanlcly:rx_retrofit:0.0.1'
	}
或者

	<dependency>
	    <groupId>com.github.fanlcly</groupId>
	    <artifactId>rx_retrofit</artifactId>
	    <version>0.0.1</version>
	</dependency>

3.在自己的项目中，创建自己的ApiService和http基类
ApiService如下(rxjava 格式)：

        public interface ApiService {
        
            @GET("url")
            Observable<Response<List<Entity>>> getData();
        }

或者（retrofit 格式）：

        public interface ApiService {
        
            @POST("url")
            Call<List<Entity> getData();
        }


http基类如下：

        public class HttpClient {
            private static HttpClient singleHttp = null;
            private HttpClient() {
            }
        
            public static HttpClient getInstance() {
                if (singleHttp == null) {
                    synchronized (HttpClient.class) {
                        if (singleHttp == null) {
                            singleHttp = new HttpClient();
                        }
                    }
                }
                return singleHttp;
            }
        
        
            public  ApiService getRetrofitService() {
        
                Retrofit retrofit = HttpRetrofit.getInstance().getRetrofit("baseUrl"
                        , new TokenInterceptor(), // 添加自己的过滤器
                        true);
        
                return retrofit.create(ApiService.class);
            }
        
        }

4，在activity中使用

rxjava方式：

         Observable observable = HttpClient.getInstance().getRetrofitService().getData();
            HttpRetrofit.getInstance().toSubscribe(observable, new RxBaseCallBack<List<Entity>>(getApplicationContext()) {
                    @Override
                    public void onSuc(Response<List<Entity>> response) {
                            // 自己的业务逻辑
                        }
                    }
        
                    @Override
                    public void onFail(Response response, String message, int failCode) {
                        ToastUtils.init(MainActivity.this).show("onFail");
                    }
        
                });


retrofit方式：

    Call<List<Entity>> call =  RetrofitHelper.getRetrofitService().getData();
    call.enqueue(new BaseCallBack<List<Entity>>(mActivity) {
        @Override
        public void onSuc(Response<BaseCallModel<UserInfo>> response) {
           // 自己的业务逻辑
        }
        @Override
        public void onFail(Response<BaseCallModel<UserInfo>> response,String message, int failCode) {
            ToastUtils.getInstance(mActivity).show(message);
        }
    });

