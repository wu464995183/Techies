package com.techies.plugin.utils

public class MethodCell {
    // 原方法名
    String name
    // 原方法参数
    String desc
    // 方法所在的接口或类
    String parent
    // 注入方法名
    String agentName
    // 注入方法描述
    String agentDesc
    // 0：this，1+：普通参数 ）
    int paramsStart
    // 采集数据的方法参数个数
    int paramsCount
    // 参数类型对应的ASM指令
    List<Integer> opcodes

    MethodCell(String name, String desc, String parent, String agentName, String agentDesc, int paramsStart, int paramsCount, List<Integer> opcodes) {
        this.name = name
        this.desc = desc
        this.parent = parent
        this.agentName = agentName
        this.agentDesc = agentDesc
        this.paramsStart = paramsStart
        this.paramsCount = paramsCount
        this.opcodes = opcodes
    }
}
