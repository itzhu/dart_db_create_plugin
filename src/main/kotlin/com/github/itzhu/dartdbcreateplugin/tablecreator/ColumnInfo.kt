package com.github.itzhu.dartdbcreateplugin.tablecreator

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
