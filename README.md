# 正式完工:tada: :tada: :tada:

## 完结撒花 :tada: :tada: :tada:

---

​	把黑马商城所用到的所有技术栈全部使用了一遍，并仿造其结构和构造弄出了一个简单的微服务模版，并使其能正常部署在docker上

这里记录下主要的问题：

---

​	首先是父工程和子工程的问题：

​	父工程必须包含所有子工程的依赖，并指定其版本

​	父工程pom不能有 repackage的插件，因为父工程pom不会编译

​	子工程所有的依赖都不能指定版本，放到父工程处理

---

​	对于nacos，注意其需要mysql才能使用，请到官网下载其sql文件

​	并在虚拟机启动时手动重启一下，因为如果你设置了和mysql一起启动的话大概率会无法正常启动

---

​	对于seata，同样有mysql需求，配置相对简单，开箱即用

----

​	FeignClient的话有个大坑，就是client接口其中的参数必须带上@RequestBody之类的！不管你是不是post，必须带上，不然读不出来,启动类上记得加上其注解,后面自定义了其config也记得加上

---

​	sentinal更没什么好说的，很简单

---

​	rabbitmq的话，建议自己去摸索一下控制台里面每个选项是什么意思，每个配置有什么用就行，没什么坑

---

​	elasticsearch除了它的api很混乱之外没什么难度，可以尝试自己写个demo引入黑马商城里面的数据测试，里面有将近九万条复杂数据，很适合拿来测试

---

​	传递user-id的mvc和openfeign功能时多加些日志，方便debug

---

​	**全部部署到docker时记得给虚拟机多些内存！不然会因为内存不够随机杀进程&蹦出奇奇怪怪的bug！**

---

​	**打包成jar时用maven的package打包，不要自己用idea的构建工件打包！**