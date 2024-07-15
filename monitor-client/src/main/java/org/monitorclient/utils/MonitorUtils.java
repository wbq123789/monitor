package org.monitorclient.utils;

import lombok.extern.slf4j.Slf4j;
import org.monitorclient.entity.BaseDetail;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * @program: monitor
 * @description: 获取主机信息工具类
 * @author: 王贝强
 * @create: 2024-07-14 22:32
 */
@Slf4j
@Component
public class MonitorUtils {
    private final double GB_TO_BYTES =1024*1024*1024.0;
    private final SystemInfo info=new SystemInfo();
    private final Properties properties=System.getProperties();

    public BaseDetail monitorBaseDetail(){
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();
        double memory=hardware.getMemory().getTotal() / GB_TO_BYTES;
        double diskSize= Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / GB_TO_BYTES;
        String ip = Objects.requireNonNull(this.findNetworkInterface(hardware)).getIPv4addr()[0];
        return new BaseDetail()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(os.getFamily())
                .setOsVersion(os.getVersionInfo().getVersion())
                .setOsBit(os.getBitness())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCore(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(memory)
                .setDisk(diskSize)
                .setIp(ip);

    }

    private NetworkIF findNetworkInterface(HardwareAbstractionLayer hardware) {
        try {
            for (NetworkIF network : hardware.getNetworkIFs()) {
                String[] ipv4Addr = network.getIPv4addr();
                NetworkInterface ni = network.queryNetworkInterface();
                if(!ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                        && (ni.getName().startsWith("eth") || ni.getName().startsWith("en"))
                        && ipv4Addr.length > 0) {
                    return network;
                }
            }
        } catch (IOException e) {
            log.error("读取网络接口信息时出错", e);
        }
        return null;
    }
}
