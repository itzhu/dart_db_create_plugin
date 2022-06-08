
<!-- Plugin description -->

# DartDBTableCreator
[![Downloads](https://img.shields.io/badge/download-pagelist-brightgreen)](https://github.com/itzhu/dart_db_create_plugin/blob/main/jars)


在dart实体类里面，点击右键，使用CreateDBTable菜单栏，可以生成table操作类。

PS:只支持int,String,double类型数据，bool请使用int代替。只建议用于demo快速开发，参考。

使用说明

创建Table类之后，会生成
```
//base文件位置
lib/dbcreate/common/db

//table生成位置
lib/dbcreate/table
```

在pubspec.ymal添加库
```
  #sqflite android\ios\macos  https://pub.dev/packages/sqflite
  sqflite: ^2.0.2
  #sqflite_common_ffi android\ios\macos\linux\windows https://pub.dev/packages/sqflite_common_ffi
  sqflite_common_ffi: ^2.1.0+2
```

如下类生成table：
```dart
class ClickHistory {
  ///id，自动增长
  int id = 0;

  ///次数
  int times = 0;

  ///最后更新时间,数据库自动更新,yyyy-MM-dd HH:mm:ss。
  ///需要在生成ClickHistoryTable里面， DbTable.TYPE_TEXT之后，加上DbTable.defaultDateTimeNow()。
  ///如：[LAST_CLICK_TIME, DbTable.TYPE_TEXT,DbTable.defaultDateTimeNow()],
  String? lastClickTime;

  ///double 演示
  double tag = 0.0;

  ///String 演示
  String? ramark;

  ///db-ignore 生成tab时忽略此字段
  String? ignoreKey;

  ///默认构造函数，一定要有
  ClickHistory();
}
```
使用CreateDbTable会生成
```dart
class ClickHistoryTable extends BaseTable<ClickHistory> {
  static const String TB_NAME = "tb_click_history";

  ///int id = 0;
  static const String ID = "id";

  ///int times = 0;
  static const String TIMES = "times";

  ///String? lastClickTime;
  static const String LAST_CLICK_TIME = "lastClickTime";

  ///double tag = 0.0;
  static const String TAG = "tag";

  ///String? ramark;
  static const String RAMARK = "ramark";

  static String createTableSql() {
    return DbTable.createTable(
        TB_NAME,
        [
          [ID, DbTable.TYPE_INTEGER, DbTable.NOT_NULL],
          [TIMES, DbTable.TYPE_INTEGER, DbTable.NOT_NULL],
          //这里的默认值需要后面生成后自己加上
          [LAST_CLICK_TIME, DbTable.TYPE_TEXT ,DbTable.defaultDateTimeNow()],
          [TAG, DbTable.TYPE_REAL, DbTable.NOT_NULL],
          [RAMARK, DbTable.TYPE_TEXT],
        ],
        primaryKey: ID,
        autoIncrement: true);
  }

  @override
  String getIdKey() => ID;

  @override
  String? getIdValue2String(ClickHistory data) => data.id.toString();

  @override
  String getTableName() => TB_NAME;

  @override
  ClickHistory mapTo(Map<String, Object?> map) {
    ClickHistory data = ClickHistory();
    data.id = DbTable.getDataOrNull(map, ID);
    data.times = DbTable.getDataOrNull(map, TIMES);
    data.lastClickTime = DbTable.getDataOrNull(map, LAST_CLICK_TIME);
    data.tag = DbTable.getDataOrNull(map, TAG);
    data.ramark = DbTable.getDataOrNull(map, RAMARK);
    return data;
  }

  @override
  Map<String, Object?> toMap(ClickHistory data) {
    return <String, Object?>{
      ID: data.id,
      TIMES: data.times,
      LAST_CLICK_TIME: data.lastClickTime,
      TAG: data.tag,
      RAMARK: data.ramark,
    };
  }
}


```

### DB的管理类需要自己创建,继承AbsDb。

示例：
```dart

///数据库管理
class AppDb extends AbsDb {
  ///只支持android\ios\windows
  @override
  Future<String> getDbPath() async {
    if (Platform.isWindows) {
      return "G:\\aaa\\";
    } else {
      return getDatabasesPath();
    }
  }

  @override
  int getDbVersion() => 1;

  @override
  String getFileName() => "app_db.db";

  @override
  Future onCreate(Database db, int version) async {
    db.execute(ClickHistoryTable.createTableSql());
  }

  @override
  Future onUpgrade(Database db, int oldVersion, int newVersion) async {
    DBUtil.deleteTable(db, ClickHistoryTable.TB_NAME);
    onCreate(db, newVersion);
  }

  var cache = {};

  T? getTable<T>(String tbName) {
    var tb = cache[tbName];
    if (tb == null) {
      switch (tbName) {
        case ClickHistoryTable.TB_NAME:
          cache[tbName] = ClickHistoryTable();
          return cache[tbName];
      }
    }
    return null;
  }
}


```

生成Table之后，如：save方法
```dart
//存入数据库
var data = await App.db.getTable<ClickHistoryTable>(ClickHistoryTable.TB_NAME)?.save(clickHistory);
if (data?.first == 2) {
  clickHistory.id = data!.second;
}
```


<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "dart_db_create_plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/itzhu/dart_db_create_plugin/blob/main/jars) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
