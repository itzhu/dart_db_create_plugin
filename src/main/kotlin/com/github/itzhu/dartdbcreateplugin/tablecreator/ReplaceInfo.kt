package com.github.itzhu.dartdbcreateplugin.tablecreator

class ReplaceInfo {
    /**
     * 如：../../dbbeans/click_history.dart
     * ../../{lib之后的目录}
     * */
    var dartFilePath: String = ""

    /**
     * ClickHistory
     * */
    var className: String = ""

    /**
     * tb_click_history
     * */
    var tableName: String = ""

    /**
    ///int id = 0;
    static const String ID = "id";

    ///int times = 0;
    static const String TIMES = "times";

    ///String? lastClickTime;
    static const String LAST_CLICK_TIME = "lastClickTime";
     * */
    var columnList: String = ""

    /**
     *
    [ID, DbTable.TYPE_INTEGER, DbTable.NOT_NULL],
    [TIMES, DbTable.TYPE_INTEGER, DbTable.NOT_NULL],
    [LAST_CLICK_TIME, DbTable.TYPE_TEXT],
    [TAG, DbTable.TYPE_REAL, DbTable.NOT_NULL],
     * */
    var tabSql: String = ""

    /**
    data.id = DbTable.getDataOrNull(map, ID);
    data.times = DbTable.getDataOrNull(map, TIMES);
    data.lastClickTime = DbTable.getDataOrNull(map, LAST_CLICK_TIME);
    data.tag = DbTable.getDataOrNull(map, TAG);
     * */
    var mapTo: String = ""

    /**
    if (!allowId0 && data.id > 0) ID: data.id,
    if (!allowNullValue && data.times != null) TIMES: data.times,
    if (!allowNullValue && data.lastClickTime != null) LAST_CLICK_TIME: data.lastClickTime,
    if (!allowNullValue && data.tag != null) TAG: data.tag,
    if (!allowNullValue && data.ramark != null) RAMARK: data.ramark,
     * */
    var toMap: String = ""


    fun getMap(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map.put("{--dartFilePath--}", dartFilePath)
        map.put("{--className--}", className)
        map.put("{--tableName--}", tableName)
        map.put("{--columnList--}", columnList)
        map.put("{--tabSql--}", tabSql)
        map.put("{--mapTo--}", mapTo)
        map.put("{--toMap--}", toMap)
        return map
    }

}