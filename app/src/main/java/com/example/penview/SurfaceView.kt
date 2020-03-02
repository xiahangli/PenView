package com.example.penview


/**
 * @author Henry
 *@Date 2020-01-20  01:11
 *@Email 2427417167@qq.com
 */
open class Person(var name : String, var age : Int){// 基类

}

class Student(name : String, age : Int, var no : String, var score : Int) : Person(name, age) {

}

open class B{
    constructor(){

    }

    //默认final
  open  fun a(){

    }

}

class Bex : B() {
    override fun a() {
//        println("我在读大学")
    }
}
//https://www.jianshu.com/p/96c2d0a68b15
interface MyInterface {
    val prop: Int // 抽象的

    val propertyWithImplementation: String
        get() = "foo"

    fun foo() {
        print(prop)
    }
}

class Child : MyInterface {
    override val prop: Int = 29
}

fun main(args :Array<String> ){

//    var myShape:MyShape=MyShape();

//    val value:String = Singleton.INSTANCE.getValue(1)
//    println(value)
//    val value1:String = Singleton.INSTANCE.getValue(1)
//    print(value1)
//    var shanghaier = Shanghaier(false,"sh")
//    print(shanghaier.skin)
//    val x4 = 1;
//    var x5 = 2;
//    when{
//        x4 == 1->{
//            print("x4=1")
//        }
//        x5 == 2->{
//            print("x5=2")
//        }
//        else->{
//            print("other")
//        }
//    }


//    when(x4) {
//        1 -> x5 = 2
//        2 -> x5 = 2
//        else -> {
//            x5 = 5
//        }
//    }
//    println(x5);
}