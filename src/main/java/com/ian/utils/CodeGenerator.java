package com.ian.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * mp代码生成器
 *
 * @author ianhau
 * @since 2023-02-18
 */
public class CodeGenerator {

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://43.142.10.47:6033/ianhau?serverTimezone=GMT%2b8", "root", "123456")
                .globalConfig(builder -> builder.author("ianhau")
                        .enableSwagger()
                        .fileOverride()
                        .outputDir("/Users/ianhau/StudyProject/data-center/src/main/java"))
                .packageConfig(builder -> builder.parent("com.ian")
                        .moduleName("")
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "/Users/ianhau/StudyProject/data-center/src/main/resources/mapper")))
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.controllerBuilder().enableHyphenStyle()
                            .enableRestStyle();
                    builder.addInclude("sys_role", "sys_menu", "sys_role_menu")
                            .addTablePrefix("t_", "sys_");
                })
                .execute();

    }
}
