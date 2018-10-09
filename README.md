# UnwrapperFactory
Retrofit Converter Factory for wrapped model classes

# Motivation

If you wrap models with ValueObject, you will have to unwrap it and pass it to retrofit services.

Increasing API access with retrofit, you have to write many unwrapping codes, like below.

```kotlin
class UserId(val value: String)
interface Service {
    @GET("/user/{userId}")
    fun get(@Path("userId") userId: String): Call<Response>

    @GET("/user/{userId}/detail")
    fun getDetail(@Path("userId") userId: String): Call<Response>

    ...
}

fun ServiceA.get(userId: UserId) = this.get(id.value)
fun ServiceA.getDetail(userId: UserId) = this.getDetail(id.value) // unwrapping again :(
...
```

So this library will resolve that repetition.

# Usage

```kotlin
// Create UnwrapperFactory, and add some unwrapper.
val factory = UnwrapperFactory.create {
   addUnwrapper<UserId> { it.value }
}

// Add it to retrofit converter factory
val retrofit = Retrofit.Builder()
   .addConverterFactory(factory)
   ...
   .build()

// You will not have to write more unwrapping code!! :tada:
val service = retrofit.create(Service::class.java)
val userId = UserId("aaaa")
service.get(userId) // request to /user/aaaa
service.getDetail(userId) // request to /user/aaaa/detail
```
