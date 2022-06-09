import 'dart:io';

import 'package:flutter_db_creator_simple/app.dart';
import 'package:sqflite/sqflite.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';

import 'base_db.dart';

class DBUtil {

  static Future<Database> openDb() {
    //TODO:获取数据库
    return App.db.openDb();
  }


  /// Delete the table with the given name from the database.
  static Future<void> deleteTable(Database db, String tableName) {
    return db.execute("DROP TABLE '$tableName'");
  }

  ///创建数据库或者得到数据库
  static Future<Database> createOrGet(AbsDb absDb, Database? db, int dbVersion) async {
    if (db == null || !db.isOpen) {
      String dbPath = await absDb.getDbPath();
      File file = File(dbPath+absDb.getFileName());
      print("BaseDb：db path:${file.path}");
      bool exists = await file.exists();
      if (!exists) {
        await file.create(recursive: true);
      }
      //https://pub.dev/packages/sqflite_common_ffi
      sqfliteFfiInit();
      var databaseFactory = databaseFactoryFfi;
      db = await databaseFactory.openDatabase(file.path,
          options: OpenDatabaseOptions(
            version: dbVersion,
            onCreate: absDb.onCreate,
            onUpgrade: absDb.onUpgrade,
          ));
    }
    return db;
  }

  ///sql: rawQuery > SELECT [column] FROM [tableName] WHERE [column]=[columnValue]
  /// 如果结果不为空，表示数据存在，返回true ，否则返回false
  static Future<bool> columnEquals(
      Database db, String tableName, String column, Object columnValue) async {
    var map = await db.rawQuery("SELECT $column FROM $tableName WHERE $column=$columnValue");
    if (map.isNotEmpty) {
      return Future.value(true);
    }
    return Future.value(false);
  }

  static Future<List<Map<String, Object?>>> selectAll(Database db, String tableName) {
    return db.query(tableName);
  }

  static String listToString(List list, {String? split}) {
    StringBuffer sb = StringBuffer();
    var sp = "";
    for (var data in list) {
      sb.write(sp);
      sb.write(data);
      sp = split ?? "";
    }
    return sb.toString();
  }

  static bool isNullOrEmpty(String? s) =>
      (s == null || s.isEmpty) ? true : false;

}
