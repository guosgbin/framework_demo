package com.peijun.sharding.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 代码生成工具类  文档地址:https://mp.baomidou.com/config/generator-config.html#url
 */
public class CodeGenerator {

    //生成配置
    private static final Boolean generatorController = false; // 生成Controller
    private static final Boolean generatorService = true; // 生成 Service
    private static final Boolean generatorServiceImpl = true; // 生成 ServiceImpl
    private static final Boolean generatorMapper = true; // 生成 Mapper
    private static final Boolean generatorEntity = true; // 生成 Entity
    private static final Boolean generatorXML = false; // 生成 XML

    private static final Boolean overWrite = false; // 覆盖已经存在的文件

    // 基本配置
    private static final String url = "jdbc:mysql://127.0.0.1:3306/order_db?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
    private static final String driverName = "com.mysql.cj.jdbc.Driver"; // 驱动
    private static final String userName = "root"; // 账号
    private static final String passWord = "123456"; // 密码

    private static final String parent = "com.peijun.sharding"; // 工程父类包名
    private static final String author = "Kwok Dylan GSGB"; // 作者

    private static final String entityName = "%s"; // 实体命名方式
    private static final String mapperName = "%sDao"; // mapper 命名方式
    private static final String xmlName = "%s"; // xml 命名方式
    private static final String serviceName = "%sService"; // service 命名方式
    private static final String serviceImplName = "%sServiceImpl"; // service impl 命名方式
    private static final String controllerName = "%sController"; // controller 命名方式
    private static final IdType idType = IdType.AUTO; // 主键生成方式

    private static final String projectPath = System.getProperty("user.dir") + "\\sharding-demo"; // 项目的目录
    private static final String path = "/src/main/java"; // 输出目录
    private static final String mapperLocation = "/src/main/resources/dao/"; // mapper文件位置

    private static final NamingStrategy naming = NamingStrategy.underline_to_camel; // 数据库表映射到实体的命名策略
    private static final NamingStrategy columnNaming = NamingStrategy.underline_to_camel; // 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
    private static final Boolean entityLombokModel = false; // 是否为lombok模型
    private static final String tablePrefix = "t_"; // 表前缀


    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) {
        startCodeGenerator();
    }

    /**
     * 生成器配置
     */
    private static void startCodeGenerator() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 1.全局配置
        GlobalConfig gc = new GlobalConfig();
        getGlobalConfig(gc);
        mpg.setGlobalConfig(gc);

        // 2.数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        getDataSource(dsc);
        mpg.setDataSource(dsc);

        // 3.包配置
        PackageConfig pc = new PackageConfig();
        getPackAgeConfig(pc);
        mpg.setPackageInfo(pc);

        // 4.自定义输出配置

        InjectionConfig cfg = getInjectionConfig();
        mpg.setCfg(cfg);


        // 5.配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null); // 取消xml的生成
        if (!generatorController)
            templateConfig.setController(null);
        if (!generatorService)
            templateConfig.setService(null);
        if (!generatorServiceImpl)
            templateConfig.setServiceImpl(null);
        if (!generatorMapper)
            templateConfig.setMapper(null);
        if (!generatorEntity)
            templateConfig.setEntity(null);
        mpg.setTemplate(templateConfig);

        // 6.策略配置
        StrategyConfig strategy = new StrategyConfig();
        getStrategyConfig(strategy);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

    private static void getPackAgeConfig(PackageConfig pc) {
        pc.setParent(parent);
        pc.setMapper("dao");
        pc.setEntity("pojo");
    }

    /**
     * 自定义配置
     *
     * @return
     */
    private static InjectionConfig getInjectionConfig() {
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        if (generatorXML) {
            // 自定义配置会被优先输出
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    return projectPath + mapperLocation + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
        }
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    /**
     * 策略配置
     *
     * @param
     * @param strategy
     */
    private static void getStrategyConfig(StrategyConfig strategy) {
        strategy.setNaming(naming); // 数据库表映射到实体的命名策略
        strategy.setColumnNaming(columnNaming); // 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setTablePrefix(tablePrefix); // 表前缀
//        strategy.setFieldPrefix(); // 字段前缀
        strategy.setEntityLombokModel(entityLombokModel); // 是否为lombok模型
        strategy.setRestControllerStyle(true); // 生成 @RestController 控制器
        strategy.setControllerMappingHyphenStyle(false); // 驼峰转连字符
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(",")); // 需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategy.setEntityTableFieldAnnotationEnable(true); // 是否生成实体时，生成字段注解
    }

    /**
     * 数据源配置
     *
     * @param dsc
     */
    private static void getDataSource(DataSourceConfig dsc) {
        dsc.setUrl(url); // 设置数据库地址
        dsc.setDriverName(driverName); // 设置驱动
        dsc.setUsername(userName); // 设置 用户名
        dsc.setPassword(passWord); // 设置密码
//        dsc.setDbType(DbType.MYSQL); // 设置数据库类型
//        dsc.setSchemaName() // 数据库 schema name 例如 PostgreSQL 可指定为 public
//        dsc.setTypeConvert() // 数据库信息查询类 默认由 dbType 类型决定选择对应数据库内置实现 实现 IDbQuery 接口自定义数据库查询 SQL 语句 定制化返回自己需要的内容
    }

    /**
     * 全局策略
     *
     * @param gc
     */
    private static void getGlobalConfig(GlobalConfig gc) {
        gc.setOutputDir(projectPath + path); // 生成文件的输出目录
        gc.setFileOverride(overWrite); // 是否覆盖已有文件
        gc.setOpen(false); // 是否打开输出目录
        gc.setAuthor(author); // 开发人员
        gc.setKotlin(false); // 默认false
        gc.setSwagger2(false); // 开启swagger文档注释
        gc.setBaseResultMap(false); // 开启 BaseResultMap
        gc.setBaseColumnList(false); // 开启 baseColumnList
        gc.setDateType(DateType.TIME_PACK); // 默认 TIME_PACK
        gc.setEntityName(entityName); // 实体命名方式   默认值：null 例如：%sEntity 生成 UserEntity
        gc.setMapperName(mapperName); // mapper 命名方式 默认值：null 例如：%sDao 生成 UserDao
        gc.setXmlName(xmlName); // Mapper xml 命名方式  默认值：null 例如：%sDao 生成 UserDao.xml
        gc.setServiceName(serviceName); // service 命名方式   默认值：null 例如：%sBusiness 生成 UserBusiness
        gc.setServiceImplName(serviceImplName); // service impl 命名方式  默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
        gc.setControllerName(controllerName); // controller 命名方式  默认值：null 例如：%sAction 生成 UserAction
        gc.setIdType(idType); // 指定生成的主键的ID类型
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
