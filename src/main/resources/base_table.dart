/*摆脱冷气，只是向上走，不必听自暴自弃者流的话。*/

import 'package:flutter_db_creator_simple/dbcreate/common/db/db_util.dart';
import 'package:sqflite/sqflite.dart';

///@name  : base_table
///@author: create by  itzhu |  2022/6/7
///@desc  :
abstract class BaseTable<T> {
  String getTableName();

  String getIdKey();

  String? getIdValue2String(T data);

  Map<String, Object?> toMap(T data);

  T mapTo(Map<String, Object?> map);

  ///根据id去判断，插入新数据或更新数据
  ///return pair:
  ///first=1 update;
  ///first=2 insert;
  Future<Pair<int, int>> save(T data, {Database? db}) async {
    db ??= await DBUtil.openDb();
    var idStr = getIdValue2String(data);
    if (!DBUtil.isNullOrEmpty(idStr)) {
      if (await DBUtil.columnEquals(db, getTableName(), getIdKey(), idStr!)) {
        var count = await db.update(getTableName(), toMap(data), where: "${getIdKey()}=$idStr");
        return Pair(1, count);
      }
    }
    var id = await db.insert(getTableName(), toMap(data), conflictAlgorithm: ConflictAlgorithm.replace);
    return Pair(2, id);
  }

  Future<List<T>> find(
      {Database? db,
        bool? distinct,
        List<String>? columns,
        String? where,
        List<Object?>? whereArgs,
        String? groupBy,
        String? having,
        String? orderBy,
        int? limit,
        int? offset}) async {
    db ??= await DBUtil.openDb();
    List<T> dataList = [];
    var list = await db.query(getTableName(),
        distinct: distinct,
        columns: columns,
        where: where,
        whereArgs: whereArgs,
        groupBy: groupBy,
        having: having,
        orderBy: orderBy,
        limit: limit,
        offset: offset);
    for (var map in list) {
      dataList.add(mapTo(map));
    }
    return Future.value(dataList);
  }

  Future<T?> findFirst({Database? db, List<String>? where}) async {
    String? sw;
    if (where != null) {
      sw = DBUtil.listToString(where);
    }
    var data = await find(db: db, where: sw);
    if (data.isNotEmpty) {
      return Future.value(data[0]);
    } else {
      return Future.value(null);
    }
  }
}

class Pair<T, D> {
  T first;
  D second;

  Pair(this.first, this.second);
}
