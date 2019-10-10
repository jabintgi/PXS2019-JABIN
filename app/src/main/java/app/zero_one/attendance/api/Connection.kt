package app.zero_one.attendance.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Connection {
    fun create(): ApiCall {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://parxsys.com/accounting/att_rprt_api.php/")
            .build()
            .create(ApiCall::class.java)
    }
}