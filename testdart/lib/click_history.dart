class ClickHistory {
  ///id，自动增长
  int id = 0;

  ///次数
  int times = 0;

  ///最后更新时间,数据库自动更新,yyyy-MM-dd HH:mm:ss
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
