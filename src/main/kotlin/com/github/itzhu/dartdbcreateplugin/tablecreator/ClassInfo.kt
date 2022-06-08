package com.github.itzhu.dartdbcreateplugin.tablecreator

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