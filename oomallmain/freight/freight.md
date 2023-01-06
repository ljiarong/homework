1.对象模型和1-1小组基本一致

# 问题1 

![image-20221218195647750](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218195647750.png)

返回的creator和modifier再core中是以属性的方式存在不是以对象的形式

![image-20221218195754050](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218195754050.png)

我使用这个代替这个是core模块中的userDto 

![image-20221218195858442](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218195858442.png)

但是这样会使在封装dto的时候异常繁琐

![image-20221218200057983](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218200057983.png)

而且在api上是userName而不是name

![image-20221225164422054](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221225164422054.png)







# 问题2

![image-20221218214124315](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218214124315.png)

关于shoplogistics的api

![image-20221218214202389](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221218214202389.png![image-20221225164600534](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221225164600534.png)

这个type我认为0是平台 1是其他是商铺 同时和问题一一样封装dto很繁琐

# 问题3

api获得仓库物流 排序不知道为什么不生效 (我没有写测试用例)

![image-20221225164702664](C:\Users\ASUSS\AppData\Roaming\Typora\typora-user-images\image-20221225164702664.png)

在分页数据中要排序的基本都是根据优先级排序  优先级小的 代表优先 所以我们排序都是ASC 升序的

# 注意

.删除和更新操作的时候需要把redis缓存删除

# 启动

需要配置数据库 redis nacos  

