# UnwrapperFactory
Retrofit Converter Factory for wrapped model classes

## Motivation

If you use wraped models with ValueObject, you will have to unwrap it when pass it to retrofit services.

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

fun Service.get(userId: UserId) = this.get(id.value)
fun Service.getDetail(userId: UserId) = this.getDetail(id.value)
...
// makes me tired :(
```

This library will resolve that repetition.

# Usage

```build.gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```app/build.gradle
dependencies {
    implementation 'com.github.atsuya046:UnwrapperFactory:0.1'
}
```

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
