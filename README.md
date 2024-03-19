# wand-flex

- 动态加载、卸载bean；动态加载、卸载Controller自动完成路由映射。
- 提供自定义注解扫描要暴露的公共方法。

## 使用教程

1. 引入sdk

```xml

<dependency>
    <groupId>io.github.xddcode</groupId>
    <artifactId>wand-flex</artifactId>
    <version>${version}</version>
</dependency>
```
## 能力支持

#### 一. 获取引入工程的资源(包含pom的基本信息、依赖列表、数据源表信息、以及通过自定义注解暴漏的公共方法资源)

启动类标记注解@EnableExposeMethod以启用获取资源，可指定扫描包名，如果不制定则默认扫描启动类所在包及其子包。
```java
@SpringBootApplication
@EnableExposeMethod(scannerPackage = "com.insentek")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServer1Application.class, args);
    }

}
```
目标工程需暴漏的资源类添加@Expose注解，以及对指定要暴露的方法添加@ExposeMethod注解。
```java
@Expose(description = "STP工具服务", type = ExposeType.Static)
public class StpUtils {

    @ExposeMethod(returnType = DataType.LONG, description = "根据token获取用户id，返回Long类型")
    public static Long getUserAsLong() {
        return 1L;
    }
}



@Service
@Expose(description = "用户信息查询服务", type = ExposeType.SpringBean)
public class UserService {

    @ExposeMethod(parameters = {
            @ExposeParameter(name = "uid", dataType = DataType.INTEGER, description = "用户ID")
    }, returnType = DataType.OBJECT, description = "获取指定ID的用户信息")
    public Object getUserById(int uid) {
        return "user info";
    }
}
```

```java
//获取资源
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ExposeContext exposeContext;

    @Test
    void test() {
        ExposeResource exposeResource = exposeContext.init();
        System.out.println(JSON.toJSONString(exposeResource));
    }

}
```
返回示例：
```json
{
  "groupId": "com.insentek",
  "artifactId": "web-server1",
  "version": "0.0.1",
  "projectName": "web-server1",
  "jdkVersion": "17.0.6",
  "dependencies": [
    {
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot-starter-web",
      "version": null,
      "type": "jar",
      "classifier": null,
      "scope": null,
      "systemPath": null,
      "exclusions": [],
      "optional": null,
      "managementKey": "org.springframework.boot:spring-boot-starter-web:jar"
    }
    ...
  ],
  "exposedMethods": [
    {
      "packageName": "com.insentek.service",
      "name": "StpUtils",
      "description": "STP工具服务",
      "type": "Static",
      "methods": [
        {
          "name": "getUserAsLong",
          "returnType": null,
          "description": "根据token获取用户id，返回Long类型",
          "parameters": []
        }
      ]
    },
    {
      "packageName": "com.insentek.service",
      "name": "UserService",
      "description": "用户信息查询服务",
      "type": "SpringBean",
      "methods": [
        {
          "name": "getUserById",
          "returnType": null,
          "description": "获取指定ID的用户信息",
          "parameters": [
            {
              "name": "uid",
              "dataType": "int",
              "description": "用户ID"
            }
          ]
        }
      ]
    },
    ...
  ],
  "tableInfos": [
    {
      "tableName": "eco_api_auth",
      "tableDDL": "CREATE TABLE `eco_api_auth` (\n  `id` int NOT NULL AUTO_INCREMENT,\n  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'api名称',\n  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\n  `uid` int NOT NULL COMMENT '创建人',\n  `ip_allow` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ip白名单',\n  `debug` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'debug开关',\n  `create_time` datetime NOT NULL COMMENT '创建时间',\n  `update_time` datetime NOT NULL COMMENT '更改时间',\n  `appid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,\n  `appsecret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,\n  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回调地址',\n  `last_modify_time` datetime DEFAULT NULL COMMENT '修改回调地址时间',\n  `enable_push_v2` int NOT NULL DEFAULT '1',\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB AUTO_INCREMENT=1143 DEFAULT CHARSET=utf8mb3"
    },
    {
      "tableName": "eco_api_describe",
      "tableDDL": "CREATE TABLE `eco_api_describe` (\n  `id` int NOT NULL AUTO_INCREMENT,\n  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  `request_example` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  `response_example` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin"
    },
    ...
  ]
}
```

#### 二. 远程或本地动态加载、卸载controller(已经bean)
##### 此sdk默认实现了3个rest接口，可通过访问以下接口获取资源
- **POST** /wand/inject 动态加载controller
- **DELETE** /wand/eject 卸载controller
- **GET** /wand/context-exposed 获取暴露的方法列表
```java

@RestController
@RequestMapping("/wand")
public class DefaultRest {

    @PostMapping("inject")
    public void inject(@RequestParam("javaFilePath") String javaFilePath, @RequestParam("fullClassName") String fullClassName) {
        //从路径上获取.java文件内容
        DynamicClass dynamicClass = DynamicClass.init(SourceType.AliOSS, javaFilePath, fullClassName);
        DynamicController dynamicController = DynamicController.builder()
                .dynamicClass(dynamicClass)
                .build();
        dynamicController.load();
    }

    @DeleteMapping("eject")
    public void eject(@RequestParam("fullClassName") String fullClassName) {
        DynamicClass dynamicClass = DynamicClass.init("", fullClassName);
        DynamicController dynamicController = DynamicController.builder()
                .dynamicClass(dynamicClass)
                .build();
        dynamicController.unload();
    }

    @GetMapping("context-exposed")
    public ExposeContext getExposeMethods() {
        return ExposeContext.init();
    }
}
```
- **SourceType**目前支持加载本地文件（Local）和阿里云OSS文件（AliOSS）
- **javaFilePath**为要动态加载的Controller的源码路径
- **fullClassName**为Controller的全类名