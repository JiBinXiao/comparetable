# comparetable
比较两个不同的表，输出针对第二张表增加删除修改的数据。可配置不同数据库不同表，可配置查询字段，表字段

配置文件说明如下
*数据库地址*
url1=jdbc:mysql://localhost:3306/
*数据库*
database1=dbgirl?useUnicode=true&characterEncoding=UTF-8
*用户名*
user1=root
*密码*
password1=
*查询的表名*
tableName1=address
*查询语句*
sql1=select name,tel,type from address
*需要比对的字段  全部应存在于 查询语句的 查询字段中*
paramName1=name,tel,type
*表中唯一字段 主键  应存在于 需要比对字段中*
idParamName1=name
*paramName1中第三个参数需要多值处理*
handleParamIndex1=3
*paramName1中第三个参数处理方式是将值按;分割   这里主要是处理某个字段里的值乱序排列的问题。*
splitString1=;

## 2配置相同

* 运行结果将输出三个文件 *
tableName.add      * 比对主键 2 有，1 没有*
tableName.update   * 2 和 1 中主键相同，内容不同的*
tableName.delete   * 比对主键 1 有 ， 2 没有*
