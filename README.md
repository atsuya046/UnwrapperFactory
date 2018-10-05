# UnwrapperFactory
Retrofit Converter Factory for wrapped model classes

# Usage

```kotlin

class UserId(val value: String)

interface Service {
    @GET("/user/{userId}")
    fun get(@Path("userId") userId: UserId) // userId will be unwrapped!!
}

val factory = UnwrapperFactory.create {
   addUnwrapper<UserId> { it.value }
}

val retrofit = Retrofit.Builder()
   .addConverterFactory(factory)
   ...
   .build()
   
val service = retrofit.create(Service::class.java)
val userId = UserId("aaaa")
service.get(userId) // request to /user/aaaa

```
