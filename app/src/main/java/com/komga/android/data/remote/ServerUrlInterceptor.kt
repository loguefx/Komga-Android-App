package com.komga.android.data.remote

import com.komga.android.data.local.PreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerUrlInterceptor @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val serverUrl = runBlocking { preferencesDataStore.getServerUrl().first() }

        if (serverUrl.isBlank()) {
            return chain.proceed(chain.request())
        }

        val parsedUrl = serverUrl.trimEnd('/').toHttpUrlOrNull()
            ?: return chain.proceed(chain.request())

        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .scheme(parsedUrl.scheme)
            .host(parsedUrl.host)
            .port(parsedUrl.port)
            .build()

        val newRequest = originalRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}
