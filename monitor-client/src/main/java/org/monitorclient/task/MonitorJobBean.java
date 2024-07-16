package org.monitorclient.task;

import jakarta.annotation.Resource;
import org.monitorclient.entity.RuntimeDetail;
import org.monitorclient.utils.MonitorUtils;
import org.monitorclient.utils.NetUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @program: monitor
 * @description: 实时监控任务
 * @author: 王贝强
 * @create: 2024-07-15 18:28
 */
@Component
public class MonitorJobBean extends QuartzJobBean {
    @Resource
    MonitorUtils monitor;
    @Resource
    NetUtils net;
    @Override
    protected void executeInternal(JobExecutionContext context) {
        RuntimeDetail runtimeDetail=monitor.monitorRuntimeDetail();
        net.updateRuntimeDetails(runtimeDetail);
    }
}
