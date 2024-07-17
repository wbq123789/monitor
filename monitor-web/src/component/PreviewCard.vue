<script setup>

</script>

<template>
  <div class="instance-card">
    <div style="display: flex;justify-content: space-between">
      <div>
        <div class="name">
          <span class="fi fi-cn"></span>
          <span style="margin: 0 5px">flying后端服务器</span>
          <i class="fa-solid fa-pen-to-square"></i>
        </div>
        <div class="os">
          操作系统: Ubuntu 22.04
        </div>
      </div>
      <div class="status">
        <i style="color: #02ca02" class="fa-solid fa-circle-play"></i>
        &nbsp;<span>运行中</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0"/>
    <div class="network">
      <span style="margin-right: 10px">公网IP：192.168.0.112</span>
      <i class="fa-solid fa-copy" style="color: #2fa5a5"></i>
    </div>
    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin-right: 10px"> 2 CPU</span>
      <i class="fa-solid fa-memory"></i>
      <span> 2 GB</span>
    </div>
    <div class="progress">
      <span>CPU Usage: 2.5%</span>
      <el-progress status="success" :percentage="2.5" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="progress">
      <span>Memory Usage: <b>2.5</b>GB</span>
      <el-progress status="success" :percentage="2.5/8 *100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="network-flow">
      <div>网络流量</div>
      <div>
        <i class="fa-solid fa-arrow-up"></i>
        <span> 120 KB/s</span>
        <el-divider direction="vertical"/>
        <i class="fa-solid fa-arrow-down"></i>
        <span> 1024 KB/s</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
:deep(.el-progress-bar__inner){
  background-color: #02ca02;
}

.dark .instance-card{
  color: #d9d9d9;
}
.instance-card{
  width: 320px;
  border-radius: 5px;
  padding: 15px;
  box-sizing: border-box;
  background-color: var(--el-bg-color);
  color: #606060;

  .name{
    font-size: 15px;
    font-weight: bold;
  }
  .status{
    font-size: 14px;
  }
  .os{
    font-size: 13px;
    color: grey;
  }
  .network{
    font-size: 13px;
  }
  .hardware{
    margin-top: 3px;
    font-size: 13px;
  }
  .progress{
    margin-top: 10px;
    font-size: 12px;
  }
  .network-flow{
    font-size: 12px;
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
  }
}
</style>