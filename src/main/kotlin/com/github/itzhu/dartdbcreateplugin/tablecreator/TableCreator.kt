import com.github.itzhu.dartdbcreateplugin.LogUtil
import com.github.itzhu.dartdbcreateplugin.tablecreator.Creator
import com.github.itzhu.dartdbcreateplugin.tablecreator.Util
import java.io.File

object TableCreator {

    /**
     * @param create  不要在子线程操作
     * */
    @JvmStatic
    fun start(dartFilePath: String, createTable: (classInfo: ClassInfo) -> Unit) {

        val beansFile = File(dartFilePath)

        if (!beansFile.exists()) {
            print("文件不存在")
            return
        }

        val lineTextList = beansFile.readLines()
        var classInfo: ClassInfo? = null
        var ignoreNext = false

        for (lineText in lineTextList) {
          //  LogUtil.log("lineText：$lineText")
            val text = lineText.trim()

            //类解析开始
            if (text.startsWith("class")) {
                classInfo = ClassInfo()
                classInfo.className = (text.split(Creator.CHAR_SPACE)[1].replace("{", ""))
                LogUtil.log("开始：${classInfo.className}")
                continue
            }

            //解析结束
            if (lineText.trimEnd() == "}") {
                if (classInfo != null) {
                    LogUtil.log("结束：${classInfo.className}")
                    createTable(classInfo)
                    classInfo = null
                    continue
                }
            }

            //是否忽略行,这里的忽略表示的是：忽略直到读取到下一个空行为止
            if (text.contains(Creator.DB_IGNORE)) {
                ignoreNext = true
                continue
            }

            //停止忽略
            if (text.isEmpty()) {
                ignoreNext = false
                continue
            }

            if (ignoreNext) {
                continue
            }

            //可以解析字段
            if (classInfo == null) return

            val matcher = Creator.VARARGS_PATTERN.matcher(text)
            val find = matcher.find()
            if (!find) continue
            val explain = matcher.group(0).trim()
            val type = matcher.group(1)
            val notNull = matcher.group(2) != "?"
            val name = matcher.group(3)
            val varargInfo = ColumnInfo(explain, type, name)
            varargInfo.notNull = notNull
            varargInfo.defaultValue = matcher.group(6)
            classInfo.addColumn(varargInfo)

            LogUtil.log("字段：$varargInfo")
        }
    }

}

class ClassInfo {
    var className: String? = null
    var idColumn: ColumnInfo? = null
    var columnList = mutableListOf<ColumnInfo>()

    fun addColumn(columnInfo: ColumnInfo) {
        val dbKey = Util.getDbKey(columnInfo.name)
        if (dbKey == "ID") {
            idColumn = columnInfo
        }
        columnList.add(columnInfo)
    }

    override fun toString(): String {
        return "ClassInfo(className=$className, idColumn=$idColumn, columnList=$columnList)"
    }


}

class ColumnInfo {

    /**注释*/
    var explain: String

    /**类型*/
    var type: String

    /**名称*/
    var name: String
    var dbName: String

    /**默认值*/
    var defaultValue: String? = null

    /**是否允许为null*/
    var notNull = false

    constructor(explain: String, type: String, name: String) {
        this.explain = explain
        this.type = type
        this.name = name
        this.dbName = Util.getDbKey(name)
    }

    override fun toString(): String {
        return "VarargInfo(explain='$explain', type='$type'," +
                " name='$name', " +
                "defaultValue=$defaultValue, notNull=$notNull)"
    }
}
