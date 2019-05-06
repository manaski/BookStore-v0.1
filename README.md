# BookStore
一个JavaWeb项目练习

#主要功能

1、分页显示数据库中图书信息，包括title，price

2、输入价格范围，分页显示对应价格内的图书信息

3、将图书购买添加购物车，购物车对象保存在session中

4、查看购物车，修改购物车内的商品数量等

5、结账操作，保证图书数量、账号变化等都是事务性的

6、根据人名查看其交易记录，按照时间逆序排序显示

#总体架构
MVC 设计模式

基于MVC模式设计，控制器servlet作为中心，Service和DAO实现类完成模型层，JSP作为视图层

#技术实现
1. 数据库：MySQL，主要包括了五个表，分别为Account,Book,User,Trade,TradeItem
2. 数据源：C3P0连接池
3. JDBC工具：DBUtils，利用QueryRunner和statement进行查询修改等操作
4. 事务解决方案：Filter + ThreadLocal，配置事务过滤器，对数据连接，事务提交，连接关闭进行管理，TreadLocal进行数据隔离，保证每个线程执行过程中使用同一个数据库连接
5. Ajax 解决方案：jQuery + JavaScript + JSON + google-gson，执行页面数据的动态更新







