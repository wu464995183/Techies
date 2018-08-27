package com.techies.plugin

import com.android.build.gradle.BaseExtension
import com.techies.plugin.utils.InjectConfig
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Techies 指的是dota中的炸弹人, 我希望这个程序能像炸弹人一样可以随处埋雷, 无CD.  哈哈哈.
 * author: wudi
 */
class PointPlugin implements Plugin<Project> {
    public static final NAME = "Techies"

    @Override
    void apply(Project project) {
        project.extensions.create('techiesConfig', Config)
        BaseExtension android = project.extensions.getByType(BaseExtension)
        PointTransform transform = new PointTransform(project)
        android.registerTransform(transform)
        printHelper()
    }

    private void printHelper() {
        def stream = getClass().getClassLoader().getResourceAsStream('helpContent.groovy')
        def helpContent = new String(IOUtils.toByteArray(stream), 'UTF-8')
        println(helpContent)
    }

    static class Config {
        String agentName = 'ime/demo11/PluginAgent'
        //指定扫描的包名
        HashSet<String> targetPaths = []
        //指定扫描的类
        HashSet<String> targetClass = []
        //注入的接口类型方法
        String injectIFEvents = '{}'
        //注入的生命周期类型方法   也可以扫描自定义方法.
        String injectFAEvents = '{}'
    }
}
