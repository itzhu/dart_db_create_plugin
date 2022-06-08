package com.github.itzhu.dartdbcreateplugin.tablecreator

import com.github.itzhu.dartdbcreateplugin.FileUtil
import com.github.itzhu.dartdbcreateplugin.LogUtil
import java.io.File
import java.lang.StringBuilder
import java.util.regex.Pattern

object Creator {

    /**是否忽略行,这里的忽略表示的是：忽略直到读取到下一个空行为止.如果是标记在类上，则此类不会被生成table*/
    const val DB_IGNORE = "db-ignore"
    const val CHAR_SPACE = " "

    const val VARARGS_REGEX2 = """^\s*(String|int|double)\s*(\??)\s+(\w+)\s*((=\s*((\S+))\s*)?);.*${'$'}"""
    val VARARGS_PATTERN = Pattern.compile(VARARGS_REGEX2)

    var tableFileDir: String = ""
    var dartFilePath: String = ""

    var tableModel: String = ""

    var TEST = false

    fun start(projectPath: String, dartFilePath: String, test: Boolean = false) {
        this.TEST = test
        this.dartFilePath = dartFilePath
        this.tableFileDir = "$projectPath/lib/dbcreate/table"
        if (!TEST) {
            addCommonFile(projectPath)
            this.tableModel = FileUtil.read(javaClass.classLoader.getResourceAsStream("table_model.txt"))
        } else {
            this.tableModel =
                File("G:\\AASOFT\\dart_db_create_plugin\\src\\main\\resources\\table_model.txt").readText(charset("utf8"))
        }
        TableCreator.start(dartFilePath) { classInfo ->
            createFile(classInfo)
        }
    }


    /**复制文件到common/db目录*/
    private fun addCommonFile(projectPath: String) {
        val outDir = File(projectPath, "/lib/dbcreate/common/db")
        if (outDir.exists()) return
        outDir.mkdirs()
        val filePathList = listOf(
            "base_db.dart",
            "base_table.dart",
            "db_table.dart",
            "db_util.dart",
        )

        for (name in filePathList) {
            val outFile = File(outDir, name)
            outFile.createNewFile()
            FileUtil.copyFile(javaClass.classLoader.getResourceAsStream(name), outFile)
        }
    }


    private fun createFile(classInfo: ClassInfo) {
        LogUtil.log("createFile：$classInfo")
        var text = String(tableModel.toCharArray())
        val replaceInfo = ReplaceInfo()

        val libIndex = this.dartFilePath.indexOf("lib")
        replaceInfo.dartFilePath = "../../${this.dartFilePath.substring(libIndex + 4)}"
            .replace("\\", "/")

        replaceInfo.className = classInfo.className!!
        replaceInfo.tableName = "tb_" + Util.getDbName(classInfo.className!!)


        val columnListSb = StringBuilder()
        val tabSqlSb = StringBuilder()
        val mapToSb = StringBuilder()
        val toMapSb = StringBuilder()


        classInfo.columnList.forEach { info ->

            //int id = 0;
            //static const String ID = "id";
            columnListSb.append("///" + info.explain)
                .append("\n")
                .append("""  static const String ${info.dbName} = "${info.name}";  """)
                .append("\n")

            // [ID, DbTable.TYPE_INTEGER, DbTable.NOT_NULL],
            tabSqlSb.append(""" [${info.dbName},${Util.getDbType(info.type, info.notNull)}], """)
                .append("\n")

            //data.id = DbTable.getDataOrNull(map, ID);
            mapToSb.append(""" data.${info.name} =  DbTable.getDataOrNull(map, ${info.dbName} ); """)
                .append("\n")
            //ID: data.id,
            toMapSb.append(""" ${info.dbName}:data.${info.name}, """)
                .append("\n")

        }

        replaceInfo.columnList = columnListSb.toString()
        replaceInfo.tabSql = tabSqlSb.toString()
        replaceInfo.mapTo = mapToSb.toString()
        replaceInfo.toMap = toMapSb.toString()

        replaceInfo.idStr = "${classInfo.idColumn?.name}" + if (classInfo.idColumn?.type == "int") ".toString()" else ""

        replaceInfo.getMap().forEach { key, value ->
            text = text.replace(key, value)
        }
        val dir = File(tableFileDir)
        if (!dir.isDirectory) {
            dir.mkdirs()
        }
        val file = File(dir, replaceInfo.tableName + ".dart")
        file.appendText(text, charset("utf8"))
    }

}

