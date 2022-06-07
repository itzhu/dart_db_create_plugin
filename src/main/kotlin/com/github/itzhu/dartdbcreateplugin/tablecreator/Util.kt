package com.github.itzhu.dartdbcreateplugin.tablecreator


object Util {

    ///是否为大写字符
    @JvmStatic
    fun isUpperCase(c: Char?): Boolean {
        val cU = c?.toInt() ?: 0
        return cU in 65..90
    }

    /**dbKey->DB_KEY*/
    @JvmStatic
    fun getDbKey(key: String): String {
        val length = key.length
        val sb = StringBuffer()
        var lastChar: Char? = null
        for (i in 0 until length) {
            val charStr = key.get(i)
            if (isUpperCase(charStr)) {
                if (i != 0 && lastChar != '_' && !isUpperCase(lastChar)) {
                    sb.append("_${charStr.toUpperCase()}")
                    continue
                }
            }
            sb.append(if (charStr != '_') charStr.toUpperCase() else sb.append(charStr))
            lastChar = charStr
        }
        return sb.toString()
    }

    /**dbName->db_name*/
    @JvmStatic
    fun getDbName(className: String): String {
        val length = className.length
        val sb = StringBuffer()
        var lastC: Char? = null
        for (i in 0 until length) {
            val c = className.get(i)
            if (isUpperCase(c)) {
                if (i != 0 && lastC != '_') {
                    sb.append("_${c.toLowerCase()}")
                    continue
                }
            }
            sb.append(if (c != '_') c.toLowerCase() else c)
            lastC = c
        }
        return sb.toString()
    }

    @JvmStatic
    fun getDbType(type: String, notNull: Boolean): String {
        return when (type) {
            "int" -> "DbTable.TYPE_INTEGER"
            "double" -> "DbTable.TYPE_REAL"
            else -> "DbTable.TYPE_TEXT"
        } + if (notNull) ", DbTable.NOT_NULL" else ""
    }
}