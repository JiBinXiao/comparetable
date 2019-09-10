# comparetable
比较两个不同的表，输出针对第二张表增加删除修改的数据。可配置不同数据库不同表，可配置查询字段，表字段
<br>
配置文件说明如下<br>
*数据库地址*<br>
url1=jdbc:mysql://localhost:3306/<br>
*数据库*<br>
database1=dbgirl?useUnicode=true&characterEncoding=UTF-8<br>
*用户名*<br>
user1=root<br>
*密码*<br>
password1=<br>
*查询的表名*<br>
tableName1=address<br>
*查询语句*<br>
sql1=select name,tel,type from address<br>
*需要比对的字段  全部应存在于 查询语句的 查询字段中*<br>
paramName1=name,tel,type<br>
*表中唯一字段   应存在于 需要比对字段中*<br>
idParamName1=name<br>
*表中主键，便于查询*<br>
idIndexParam1=id
*paramName1中第三个参数需要多值处理*<br>
handleParamIndex1=3<br>
*paramName1中第三个参数处理方式是将值按;分割   这里主要是处理某个字段里的值乱序排列的问题。*<br>
splitString1=;<br>
<br>
## 2配置相同<br>
<br>
* 运行结果将输出三个文件 *<br>
tableName.add      * 比对主键 2 有，1 没有*<br>
tableName.update   * 2 和 1 中主键相同，内容不同的*<br>
tableName.delete   * 比对主键 1 有 ， 2 没有*<br>
