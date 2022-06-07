package com.github.itzhu.dartdbcreateplugin

import ColumnInfo
import java.util.regex.Matcher
import java.util.regex.Pattern


/**String name = "a";*/
const val VARARGS_REGEX2 = """^\s*(String|int|double)\s*(\??)\s+(\w+)\s*((=\s*((\S+))\s*)?);.*${'$'}"""
 val pattern = Pattern.compile(VARARGS_REGEX2)
fun main(vararg cmd: String) {

    var list = listOf(
        """  String name = "tony";  """,
        """  String? name = "tony1" ;  """,
        """  String ? name = 'tony2';  """,
        """  int ? num = 0 ;  """,
        """  int num = 100 ;  """,
        """  double dn = 100.0 ;  """,
        """  double? dn = 100.0 ;  """,
        """  double? dn;  """,
        """  String? dn ;  """,
    )

    for (str in list) {

        val matcher: Matcher = pattern.matcher(str)
        val find = matcher.find()
        if (!find) continue
        //分组个数是‘(’的个数
        val gpCount = matcher.groupCount()
        //println("gpCount:$gpCount")

//        val sb = StringBuilder()
//        for (i in 0 until gpCount) {
//            sb.append("$i:${matcher.group(i)}")
//            sb.append("|")
//        }
        //0:  String? name = "tony1" ;  |1:String|2:?|3:name|4:= "tony1" |5:= "tony1" |6:"tony1"|
        // println(sb.toString())
        val explain = matcher.group(0).trim()
        val type = matcher.group(1)
        val notNull = matcher.group(2) != "?"
        val name = matcher.group(3)
        val varargInfo = ColumnInfo(explain, type, name)
        varargInfo.notNull = notNull
        varargInfo.defaultValue = matcher.group(6)

        println(varargInfo.toString())
    }
}
