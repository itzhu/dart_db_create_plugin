import '../common/db/base_table.dart';
import '../common/db/db_table.dart';

import "../../click_history.dart";

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
          [LAST_CLICK_TIME, DbTable.TYPE_TEXT],
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