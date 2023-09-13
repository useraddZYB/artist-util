# artist-util
java开发工具集合，包含：日期格式化、参数解析、智能日志、线程池、计时器、频控。10几年开发经验沉淀、零依赖的java开发工具集合。


## 二，集成使用

#### 1 common 通用工具  

* ParamUtil：parse&format、check&assert、http get、size等等（5星推荐）
* MachineUtil

#### 2 concurrent 多线程并发  

* ThreadPoolFactory：便捷构造线程池（4星推荐）
* BatchExecuteUtil：并发执行

#### 3 data structure 数据结构

* FuncResult：方法"复杂"返回值（4星推荐）
* DoubleHashMap：双重map

#### 4 date 线程安全的日期工具  

* DateUtill：日期parse format 计算（5星推荐）
* Watch：多段计时工具（5星推荐）

#### 5 limiter 限流工具  

* NoticeLimiterLocal：通知告警限流

#### 6 log 日志工具  

* SmartLogger：智能打印日志
* LogControl：简单次数限制打印

#### 7 sample 采样工具  

* LocalSample


## 二，集成使用

#### 1 maven引入

jar包已上传到github package仓库中：  
https://github.com/useraddZYB?tab=packages   
https://github.com/useraddZYB/artist-util/packages/1941045

```
<dependency>
    <groupId>com.programmerartist.artistutil</groupId>
    <artifactId>artist-util</artifactId>
    <version>1.0.1</version>
</dependency>
```  
