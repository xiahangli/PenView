package com.example.penview

/**
 * @author Henry
 *@Date 2020-01-26  17:51
 *@Email 2427417167@qq.com
 */
class Singleton private constructor(){

    private var cache :  HashMap<Int,String> = HashMap();

    companion object{
        //懒加载的方式
        var INSTANCE = SingletonHolder.instance;
    }

    fun getValue(key: Int) :String{
        //cache[key]表示得到map对应key的value
        return if (cache[key] == null) initCache(key)else cache[key]!!
    }

    private fun initCache(key: Int):String{
        var stringVal :String = Math.random().toString()
        //存入map中 或者也可以通过cache.put方式存入
//        cache[key]=stringVal
        cache.put(key,stringVal)
        return stringVal
    }

    //私有对象
    private object SingletonHolder{
        var instance = Singleton();
    }
}