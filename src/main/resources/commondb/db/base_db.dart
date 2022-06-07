import 'db_util.dart';
import 'package:sqflite/sqflite.dart';

abstract class AbsDb {
  Database? _db;

  ///打开数据库
  Future<Database> openDb() async {
    _db = await DBUtil.createOrGet(this, _db, getDbVersion());
    return Future.value(_db);
  }

  ///数据库文件名称
  String getFileName();

  ///数据库存放路径
  Future<String> getDbPath();

  ///数据库版本
  int getDbVersion();

  ///创建数据库
  Future onCreate(Database db, int version);

  ///升级数据库
  Future onUpgrade(Database db, int oldVersion, int newVersion);
}
