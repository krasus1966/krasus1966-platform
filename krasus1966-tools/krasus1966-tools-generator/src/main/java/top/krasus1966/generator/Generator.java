package top.krasus1966.generator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.core.db.service.IBaseService;
import top.krasus1966.generator.engine.OtherFileFreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/11/19 19:34
 **/
public class Generator {
    // 父包名称，以基础项目为例，应填写top.krasus1966
    private static final String PARENT_PACKAGE = "top.krasus1966";
    // 模块名 以基础项目系统模块为例，应填写system
    private static final String MODULE_NAME = "system";
    // 输出地址
    private static final String OUT_PUT_PATH = "/Users/krasus1966/temp/generator";
    // 表名，多个用逗号分隔，全部则不填
    private static final String TABLE_NAME = "sys_user,sys_url";
    // 表前缀，没有则空着
    private static final String[] TABLE_NAME_PREFIX = {};
    // 是否开启Swagger2注解
    private static final boolean IS_SWAGGER2 = true;
    // 数据库地址
    private static final String DATABASE_URL = "jdbc:mysql://localhost:11003/base_backend" +
            "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true" +
            "&serverTimezone=Asia/Shanghai";
    // 数据库用户名
    private static final String USER_NAME = "root";
    // 数据库密码
    private static final String PASSWORD = "196610121";
    // 代码作者
    private static final String AUTHOR = "krasus1966";

    public static void main(String[] args) {
        FastAutoGenerator.create(DATABASE_URL, USER_NAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR);
                    // 开启 swagger 模式
                    if (IS_SWAGGER2) {
                        builder.enableSwagger();
                    }
                    builder.dateType(DateType.TIME_PACK);
                    builder.commentDate("yyyy-MM-dd HH:mm:ss");
                    builder.outputDir(OUT_PUT_PATH); // 指定输出目录
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent(PARENT_PACKAGE);
                    // 设置父包模块名
                    builder.moduleName(MODULE_NAME);
                    // 设置Entity包名
                    builder.entity("entity");
                    builder.service("service");
                    builder.serviceImpl("service.impl");
                    builder.mapper("mapper");
                    builder.controller("facade");

                    Map<OutputFile, String> pathInfo = new HashMap<OutputFile, String>();
                    pathInfo.put(OutputFile.entity, OUT_PUT_PATH + "/" + PARENT_PACKAGE.replace(
                            ".", "/") + "/" + MODULE_NAME + "/entity/po/");
                    pathInfo.put(OutputFile.controller, OUT_PUT_PATH + "/" + PARENT_PACKAGE.replace(
                            ".", "/") + "/" + MODULE_NAME + "/facade/admin/");
                    pathInfo.put(OutputFile.other, OUT_PUT_PATH + "/" + PARENT_PACKAGE.replace(
                            ".", "/") + "/" + MODULE_NAME + "/");
                    pathInfo.put(OutputFile.xml, OUT_PUT_PATH + "/resources/mapper/");
                    builder.pathInfo(pathInfo);
                })
                .templateConfig(builder -> {
                    builder.entity("/templates/entity.java");
                    builder.service("/templates/service.java");
                    builder.serviceImpl("/templates/serviceImpl.java");
                    builder.mapper("/templates/mapper.java");
                    builder.controller("/templates/controller.java");
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude(TABLE_NAME);
                    // 设置过滤表前缀
                    builder.addTablePrefix(TABLE_NAME_PREFIX);

                    /**
                     * 实体配置
                     */
                    Entity.Builder entityBuilder = builder.entityBuilder();
                    entityBuilder.superClass(AbstractPersistent.class);
                    entityBuilder.disableSerialVersionUID();
                    entityBuilder.enableColumnConstant();
                    entityBuilder.enableChainModel();
                    entityBuilder.enableLombok();
                    entityBuilder.enableTableFieldAnnotation();
                    entityBuilder.addSuperEntityColumns("id", "crt_id", "crt_ip", "crt_time",
                            "upd_id", "upd_ip", "upd_time", "tenant_id");
                    entityBuilder.versionColumnName("version");
                    entityBuilder.logicDeleteColumnName("deleted");
                    entityBuilder.formatFileName("%s");
                    entityBuilder.enableFileOverride();
                    entityBuilder.build();

                    /**
                     * Service配置
                     */
                    Service.Builder serviceBuilder = builder.serviceBuilder();
                    serviceBuilder
                            .superServiceClass(IBaseService.class)
                            .superServiceImplClass(AbstractMybatisBaseService.class)
                            .enableFileOverride()
                            .build();
                    /**
                     * Mapper配置
                     */
                    Mapper.Builder mapperBuilder = builder.mapperBuilder();
                    mapperBuilder.superClass(BaseMapper.class)
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("I%sMapper")
                            .formatXmlFileName("%sMapper")
                            .enableFileOverride()
                            .build();
                    /**
                     * Controller配置
                     */
                    Controller.Builder controllerBuilder = builder.controllerBuilder();
                    controllerBuilder.superClass("BaseController")
                            .enableHyphenStyle()
                            .enableRestStyle()
                            .formatFileName("%sFacade")
                            .enableFileOverride()
                            .build();
                })
                .injectionConfig(consumer -> {
                    Map<String, String> customFile = new HashMap<>();
                    consumer.beforeOutputFile((tableInfo, stringObjectMap) -> {
                        customFile.put("/facade/front/" + tableInfo.getEntityName() + "Facade" +
                                ".java", "/templates/frontcontroller.java.ftl");
                        customFile.put("/entity/dto/" + tableInfo.getEntityName() + "DTO.java",
                                "/templates/entityDTO.java.ftl");
                        customFile.put("/entity/vo/" + tableInfo.getEntityName() + "VO.java",
                                "/templates/entityVO.java.ftl");
                    });
                    consumer.customFile(customFile);
                })
                .templateEngine(new OtherFileFreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
