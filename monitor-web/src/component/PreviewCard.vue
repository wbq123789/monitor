<script setup>
import {fitByUnit} from "@/tools";
import {useClipboard} from "@vueuse/core";
import {ElMessage, ElMessageBox} from "element-plus";
import {post} from "@/net";

const props=defineProps({
  data:Object,
  update:Function
})

const { copy }=useClipboard()
const copyIp=()=>copy(props.data.ip).then(()=>ElMessage.success("IP地址拷贝成功"))

function rename() {
  ElMessageBox.prompt('请输入新的服务器主机名称', '修改名称', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputValue: props.data.name,
    inputPattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{1,20}$/,
    inputErrorMessage: '名称只能包含中英文字符、数字和下划线',
  }).then(({ value }) => post('/api/monitor/rename', {
        id: props.data.id,
        name: value
      }, () => {
        ElMessage.success('主机名称已更新')
        props.update()
      })
  )
}
</script>

<template>
  <div class="instance-card">
    <div style="display: flex;justify-content: space-between">
      <div>
        <div class="name">
          <span :class="`fi fi-${data.location}`"></span>
          <span style="margin: 0 5px">{{data.name}}</span>
          <i class="fa-solid fa-pen-to-square interact-tem" @click.stop="rename"></i>
        </div>
        <div class="os">
          操作系统: {{ `${data.osName} ${data.osVersion}` }}
        </div>
      </div>
      <div class="status" v-if="data.online">
        <i style="color: #02ca02" class="fa-solid fa-circle-play"></i>
        &nbsp;<span>运行中</span>
      </div>
      <div class="status" v-else>
        <i style="color: #8a8a8a" class="fa-solid fa-circle-stop"></i>
        &nbsp;<span>离线</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0"/>
    <div class="network">
      <span style="margin-right: 10px">公网IP：{{ data.ip }}</span>
      <i class="fa-solid fa-copy interact-tem" @click.stop="copyIp" style="color: #2fa5a5"></i>
    </div>
    <div class="cpu">
      <span style="margin-right: 10px">处理器：{{ data.cpuName }}</span>
    </div>
    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin-right: 10px">{{` ${data.cpuCore} CPU`}}</span>
      <i class="fa-solid fa-memory"></i>
      <span>{{` ${data.memory.toFixed(1)} GB`}}</span>
    </div>
    <div class="progress">
      <span>{{ `CPU Usage: ${(data.cpuUsage * 100).toFixed(1)} %` }}</span>
      <el-progress status="success" :percentage="data.cpuUsage * 100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="progress">
      <span>Memory Usage: <b>{{ data.memoryUsage.toFixed(1) }}</b>GB</span>
      <el-progress status="success" :percentage="data.memoryUsage/data.memory *100" :stroke-width="5" :show-text="false"/>
    </div>
    <div class="network-flow">
      <div>网络流量</div>
      <div>
        <i class="fa-solid fa-arrow-up"></i>
        <span>{{ ` ${fitByUnit(data.networkUpload,'KB')} /s `}}</span>
        <el-divider direction="vertical"/>
        <i class="fa-solid fa-arrow-down"></i>
        <span>{{ ` ${fitByUnit(data.networkDownload,'KB')} /s `}}</span>
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

.interact-tem{
  transition: .3s;

  &:hover{
    cursor: pointer;
    scale: 1.1;
    opacity: 0.8;
  }
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
  .cpu{
    font-size: 13px;
  }
  .network-flow{
    font-size: 12px;
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
  }
}
</style>