///sqlite 数据类型https://www.runoob.com/sqlite/sqlite-data-types.html
class DbTable {
  static T getData<T>(Map<String, Object?> data, String key, T defaultValue) {
    try {
      var value = data[key] as T?;
      return value ?? defaultValue;
    } catch (e) {
      print("DbTable:e:$e");
    }
    return defaultValue;
  }

  static T? getDataOrNull<T>(Map<String, Object?> data, String key) {
    try {
      var value = data[key] as T?;
      return value;
    } catch (e) {
      print("DbTable:e:$e");
    }
    return null;
  }

  ///唯一
  static const String UNIQUE = "UNIQUE";

  ///不为空
  static const String NOT_NULL = "NOT NULL";

  ///主键
  static const String _PRIMARY_KEY = "PRIMARY KEY";

  ///自增
  static const String _AUTOINCREMENT = "AUTOINCREMENT";

  ///默认值
  static const String _DEFAULT = "DEFAULT";

  ///设置默认值
  static String defaultValue(Object data) {
    if (data is String) {
      return "$_DEFAULT '$data'";
    } else {
      return "$_DEFAULT $data";
    }
  }


  ///日期时间,默认存储为：yyyy-MM-dd HH:mm:ss，本地时间。
  static String defaultDateTimeNow(){
    return "$_DEFAULT (datetime('now','localtime'))";
  }

  //----------------------------------------------

  ///对应dart int类型。值是一个带符号的整数，根据值的大小存储在 1、2、3、4、6 或 8 字节中。
  static const String TYPE_INTEGER = "INTEGER";

  ///对应dart String类型.
  static const String TYPE_TEXT = "TEXT";

  ///值是一个 blob 数据，完全根据它的输入存储。
  static const String TYPE_BOLD = "BOLD";

  ///对应dart double类型。值是一个浮点值，存储为 8 字节的 IEEE 浮点数字。
  static const String TYPE_REAL = "REAL";

  ///时间数据，对应dart String,日期格式 yyyy-MM-dd HH:mm:ss
  static const String TYPE_DATETIME = "DATETIME";

  ///创建表
  ///
  /// [tableName] 表名
  ///
  /// [columnList] [字段名，类型，附加设置，默认值],[字段名，类型，附加设置，默认值]...
  ///
  ///[primaryKey]主键，不要写空字符串
  ///
  ///[autoIncrement]主键自增
  static String createTable(String tableName, List<List<String>> columnList,
      {String? primaryKey, bool? autoIncrement}) {
    var sb = StringBuffer();
    sb.write("CREATE TABLE \"$tableName\" (");
    var columnIndex = 0;
    var columnSize = columnList.length;
    var columnValueIndex = 0;
    for (List<String> column in columnList) {
      columnValueIndex = 0;
      for (String value in column) {
        sb.write(columnValueIndex == 0 ? "\"$value\"" : value);
        sb.write(" ");
        columnValueIndex++;
      }
      columnIndex++;
      if (columnIndex < columnSize) sb.write(",");
    }

    if (primaryKey?.isNotEmpty == true) {
      sb.write(",");
      sb.write(_PRIMARY_KEY);
      sb.write("(");
      sb.write(primaryKey);
      if (autoIncrement == true) {
        sb.write(" ");
        sb.write(_AUTOINCREMENT);
      }
      sb.write(")");
    }

    sb.write(")");
    return sb.toString();
  }

}
