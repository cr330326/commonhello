package com.cryallen.commonlib.manager;

import android.content.Context;
import android.text.TextUtils;

import com.cryallen.commonlib.rxhttp.RetrofitHttpUtils;
import com.cryallen.commonlib.rxhttp.config.OkHttpConfig;
import com.cryallen.commonlib.rxhttp.cookie.store.SPCookieStore;
import com.cryallen.commonlib.rxhttp.factory.ApiFactory;
import com.cryallen.commonlib.rxhttp.gson.GsonAdapter;
import com.cryallen.commonlib.rxhttp.interfaces.BuildHeadersListener;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/***
 * 网络请求工具类
 * @author Allen
 * @DATE 2019-11-06
 ***/
public class RetrofitHttpManager {
	private volatile static RetrofitHttpManager mInstance;

	private Context mContext;
	private OkHttpConfig.Builder mBuilder;
	private Proxy mProxy = null;
	private ApiFactory mApiFactory;

	/**
	 * 获取 RetrofitHttpManager 实例
	 */
	public static RetrofitHttpManager getInstance() {
		if (null == mInstance) {
			synchronized (RetrofitHttpManager.class) {
				if (null == mInstance) {
					mInstance = new RetrofitHttpManager();
				}
			}
		}
		return mInstance;
	}

	/**
	 * 全局请求的统一配置（以下配置根据自身情况选择性的配置即可）
	 */
	public void init(Context context,String baseUrl,String proxyIp,int proxyPort) {
		mContext = context;
		if(!TextUtils.isEmpty(proxyIp)){
			mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp,proxyPort));
		}

		initBuilder();
		RetrofitHttpUtils.getInstance()
				.init(context)
				.config()
				//自定义factory的用法
				.setCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.setConverterFactory(ScalarsConverterFactory.create(),
						GsonConverterFactory.create(GsonAdapter.buildGson()),
						JaxbConverterFactory.create())
				//配置全局baseUrl
				.setBaseUrl(baseUrl)
				.setOkClient(new OkHttpClient());
		initApiFactory();
	}

	/**
	 * 初始化配置OkHttpConfig.Builder
	 */
	private void initBuilder() {
		/** TODO 后续增加HTTPS证书用
		 //        InputStream cerInputStream = null;
		 //        InputStream bksInputStream = null;
		 //        try {
		 //            cerInputStream = getAssets().open("YourSSL.cer");
		 //            bksInputStream = getAssets().open("your.bks");
		 //        } catch (IOException e) {
		 //            e.printStackTrace();
		 //        }*/
		mBuilder = new OkHttpConfig.Builder(mContext)
				//添加公共请求头
				.setHeaders(new BuildHeadersListener() {
					@Override
					public Map<String, String> buildHeaders() {
						HashMap<String, String> hashMap = new HashMap<>();
						hashMap.put("Client", "Android");
						hashMap.put("AppVersion", "1.0");
						hashMap.put("User-Agent","Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
						hashMap.put("Accept-Languag","zh-CN,en-US");
						hashMap.put("Accept-Charset","UTF-8");
						hashMap.put("Chartset","UTF-8");
						hashMap.put("Cache-Control","no-cache");
						hashMap.put("Connection","Keep-Alive");
						return hashMap;
					}
				})
				//开启缓存策略(默认false)
				//1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
				//2、在没有网络的时候，去读缓存中的数据。
				.setCache(true)
				//默认有网络时候缓存60秒
				.setHasNetCacheTime(60)
				//全局持久话cookie,保存到内存（new MemoryCookieStore()）或者保存到本地（new SPCookieStore(this)）
				//不设置的话，默认不对cookie做处理
				.setCookieType(new SPCookieStore(mContext))
				//可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
				//.setAddInterceptor(null))
				//全局ssl证书认证
				//1、信任所有证书,不安全有风险（默认信任所有证书）
				//.setSslSocketFactory()
				//2、使用预埋证书，校验服务端证书（自签名证书）
				//.setSslSocketFactory(cerInputStream)
				//3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
				//.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
				//设置Hostname校验规则，默认实现返回true，需要时候传入相应校验规则即可
				//.setHostnameVerifier(null)
				//全局超时读配置
				.setReadTimeout(10)
				//全局超时写配置
				.setWriteTimeout(10)
				//全局连接超时配置
				.setConnectTimeout(10)
				//全局是否打开请求log日志
				.setDebug(true)
				.build();
	}

	/**
	 * 初始化一个ApiFactory实例
	 */
	private void initApiFactory(){
		mApiFactory = ApiFactory.getInstance().setOkClient(mBuilder.create());
	}

	/**
	 * 创建一个OkHttpClient
	 */
	private OkHttpClient createOkHttp(boolean isProxy){
		if(mBuilder != null){
			if(isProxy && mProxy!= null){
				mBuilder.setProxy(mProxy);
			} else {
				mBuilder.setProxy(null);
			}
			return mBuilder.create();
		}

		return null;
	}

	/**
	 * 获取一个ApiFactory实例
	 */
	public ApiFactory getApiFactory(boolean isProxy){
		return ApiFactory.getInstance().setOkClient(createOkHttp(isProxy));
	}

	/**
	 * 使用全局参数创建请求
	 *
	 * @param cls Class
	 * @param <K> K
	 * @return 返回
	 */
	public <K> K createApi(Class<K> cls) {
		return mApiFactory.createApi(cls);
	}

	/**
	 * 切换baseUrl
	 *
	 * @param baseUrlKey   域名的key
	 * @param baseUrlValue 域名的url
	 * @param cls          class
	 * @param <K>          k
	 * @return k
	 */
	public <K> K createApi(String baseUrlKey, String baseUrlValue, Class<K> cls) {
		return mApiFactory.createApi(baseUrlKey, baseUrlValue, cls);
	}
}


