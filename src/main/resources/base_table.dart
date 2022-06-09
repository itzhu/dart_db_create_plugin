/*摆脱冷气，只是向上走，不必听自暴自弃者流的话。*/

import 'package:flutter_db_creator_simple/dbcreate/common/db/db_util.dart';
import 'package:sqflite/sqflite.dart';

///@name  : base_table
///@author: create by  itzhu |  2022/6/7
///@desc  :
abstract class BaseTable<T> {
  String getTableName();

  String getIdKey();

  ///[allowNullValue] 允许包含空数据
  ///
  ///[allowId0] id允许为0.避免默认为0的值，导致插入变成更新。
  Map<String, Object?> toMap(T data, {bool allowNullValue = false, bool allowId0 = false});

  T mapTo(Map<String, Object?> map);

  ///根据id去判断，插入新数据或更新数据.
  ///
  ///[allowNullValue] 允许包含空数据
  ///
  ///[allowId0] allowId0=true时，如果id为int,id需要大于0. 如果是string,则id不能为null或空
  ///
  ///return pair:first=1 update;first=2 insert;
  Future<Pair<int, int>> save(T data, {bool allowNullValue = false, bool allowId0 = false, Database? db}) async {
    db ??= await DBUtil.openDb();
    Map<String, Object?> map = toMap(data, allowNullValue: allowNullValue, allowId0: allowId0);
    var id = map[getIdKey()];
    if (id != null) {
      if (await DBUtil.columnEquals(db, getTableName(), getIdKey(), "$id")) {
        var count = await db.update(getTableName(), toMap(data), where: "${getIdKey()}=$id");
        return Pair(1, count);
      }
    }
    var insertId = await db.insert(getTableName(), map, conflictAlgorithm: ConflictAlgorithm.replace);
    return Pair(2, insertId);
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

  @override
  String toString() {
    return 'Pair{first: $first, second: $second}';
  }
}
