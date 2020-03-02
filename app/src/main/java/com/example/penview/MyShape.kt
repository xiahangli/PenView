package com.example.penview

/**
 * @author Henry
 *@Date 2020-01-27  00:38
 *@Email 2427417167@qq.com
 */
class MyShape(override var classA:ClassA):IShape{

    init {
        classA = ClassA();
        classA.varA = "MyShapeImpl"
    }

    fun getVarA():String{
        return classA.varA
    }


    //复写父类的字段，是强制的
//    override var classA: ClassA
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        set(value) {}
}