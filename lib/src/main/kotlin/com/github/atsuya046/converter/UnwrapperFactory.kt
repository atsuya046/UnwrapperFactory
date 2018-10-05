package com.github.atsuya046.converter

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class UnwrapperFactory private constructor(): Converter.Factory() {

    private val factories: MutableList<Converter.Factory> = mutableListOf()

    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
        factories.forEach { factory ->
            factory.stringConverter(type, annotations, retrofit)
                    ?.let { return it }
        }
        return null
    }

    fun <T> addUnwrapper(clazz: Class<T>, converter: Converter<T, String>) {
        val factory = object : Converter.Factory() {
            override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
                return if (clazz == type) {
                    converter
                } else {
                    null
                }
            }
        }
        factories.add(factory)
    }


    inline fun <reified T> addUnwrapper(converter: Converter<T, String>) = addUnwrapper(T::class.java, converter)

    inline fun <reified T> addUnwrapper(crossinline converter: (T) -> String) {
        addUnwrapper(Converter<T, String> { converter(it) })
    }

    companion object {
        fun create(appendix: UnwrapperFactory.() -> Unit): UnwrapperFactory {
            return UnwrapperFactory().apply(appendix)
        }
    }
}