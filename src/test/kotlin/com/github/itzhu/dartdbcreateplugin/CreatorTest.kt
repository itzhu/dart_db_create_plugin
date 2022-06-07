package com.github.itzhu.dartdbcreateplugin

import com.github.itzhu.dartdbcreateplugin.tablecreator.Creator

fun main(){
    LogUtil.log("start==========")
    Creator.start("G:\\AASOFT\\dart_db_create_plugin\\testdart\\",
        "G:\\AASOFT\\dart_db_create_plugin\\testdart\\lib\\click_history.dart",
        true)
    LogUtil.log("finish==========")

}