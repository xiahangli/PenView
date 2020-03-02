package com.example.penview

/**
 * @author Henry
 *@Date 2020-01-26  00:40
 *@Email 2427417167@qq.com
 */
open class Chinese constructor(var sex:Boolean ,var region: String) {
    //protected不能被其他地方调用
    public open val skin = "yellow"
    val avgLife: Double
        get() {
            when (this.region) {
                "sh" -> {
                    return 82.4
                }
                "ah" -> {
                    return 77.8
                }
                else -> {
                    return 73.4
                }
            }
        }
}

class  Shanghaier(sex :Boolean,region: String="sh"): Chinese(sex,region){
    //子类中的属性
    public var dialect="上海话"
    //属性覆盖
    override var skin="上海黄"
}

