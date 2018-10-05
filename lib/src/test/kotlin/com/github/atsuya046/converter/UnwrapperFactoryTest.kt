package com.github.atsuya046.converter

import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class UnwrapperFactoryTest {
    private val factory = UnwrapperFactory.create {
        addUnwrapper<UserId> { it.value }
        addUnwrapper<Animal> { it.name.toLowerCase() }
    }

    private val server = MockWebServer()

    private val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(factory)
            .build()

    interface Service {
        @GET("/user/{userId}")
        fun get(
                @Path("userId") userId: UserId,
                @Query("animal") animal: Animal
        ): Call<ResponseBody>
    }

    @Test
    fun stringConverter() {
        server.enqueue(MockResponse().setBody("{\"result\":\"ok\"}"))
        val service = retrofit.create(Service::class.java)
        val userId = UserId("abcd")
        val animal = Animal.DOG
        val call = service.get(userId, animal)
        val request = call.request()
        assertTrue(request.url().encodedPath().contains(userId.value))
        assertTrue(request.url().query() == "animal=dog")
    }

    class UserId(val value: String)

    enum class Animal {
        DOG, CAT
    }
}
