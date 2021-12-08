package com.fengjx.reload.agent;

import com.fengjx.reload.common.utils.VersionUtils;

import java.lang.instrument.Instrumentation;

/**
 * agent 启动类
 *
 * @author FengJianxin
 * @since 2021-11-12
 */
public class AgentBootstrap {

    /**
     * 启动时加载
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("[premain] agent start: " + VersionUtils.getLatestVersion());
    }

    /**
     * 运行时加载（attach）
     */
    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("[agentmain] agent start: " + VersionUtils.getLatestVersion());
        Handler handler = new ReloadClassHandler();
        handler.process(args, inst);
    }


}
