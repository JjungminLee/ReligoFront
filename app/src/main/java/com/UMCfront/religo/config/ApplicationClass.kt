package com.UMCfront.religo.config

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {

    val API_URL = "https://jmlee.shop/"

    // 코틀린의 전역변수 문법
    companion object {
        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var SharedPreferences: SharedPreferences
        lateinit var SPEditor: SharedPreferences.Editor
        // JWT Token Header 키 값
        //val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"

        // Retrofit 인스턴스, 앱 실행시 한번만 생성하여 사용합니다.
        lateinit var sRetrofit: Retrofit
    }
    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()


        // 카카오 소셜로그인
        KakaoSdk.init(this, "6b4c422835c948c8624f39254f00aa31")

        SharedPreferences =
            applicationContext.getSharedPreferences("JWT", MODE_PRIVATE)
        applicationContext.getSharedPreferences("JWT", MODE_PRIVATE)
        SPEditor = SharedPreferences.edit()
        // 레트로핏 인스턴스 생성
        initRetrofitInstance()
    }

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    private fun initRetrofitInstance() {

        Log.d("ApplicationClass","application**")
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            //.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        // sRetrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        sRetrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}