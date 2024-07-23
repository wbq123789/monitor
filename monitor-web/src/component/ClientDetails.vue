<script setup>
import {computed, reactive, watch} from "vue";
import {get, post} from "@/net";
import {copyIp, cpuNameToImage, fitByUnit, osNameToIcon, percentageToStatus, rename} from "@/tools";
import {ElMessage} from "element-plus";
import RuntimeHistory from "@/component/RuntimeHistory.vue";

const locations = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'hk', desc: '香港'},
  {name: 'jp', desc: '日本'},
  {name: 'us', desc: '美国'},
  {name: 'sg', desc: '新加坡'},
  {name: 'kr', desc: '韩国'},
  {name: 'de', desc: '德国'}
]
const props=defineProps({
  id:Number,
  update: Function
})
const details=reactive({
  base:{},
  runtime:{
    list: []
  },
  editNode:false
})
const nodeEdit=reactive({
  name:'',
  location: ''
})
const enableNodeEdit=()=>{
  details.editNode=true
  nodeEdit.name=details.base.node
  nodeEdit.location=details.base.location
}
const submitNodeEdit=()=>{
  post("/api/monitor/node",{
    id: props.id,
    node: nodeEdit.name,
    location: nodeEdit.location
  },()=>{
    details.editNode=false
    updateDetails()
    ElMessage.success("节点信息更新成功！")
  })
}
function updateDetails() {
  props.update()
  init(props.id)
}

setInterval(() => {
  if(props.id !== -1 && details.runtime) {
    get(`/api/monitor/runtime_now?clientId=${props.id}`, data => {
      if(details.runtime.list.length >= 360)
        details.runtime.list.splice(0, 1)
      details.runtime.list.push(data)
    })
  }
}, 10000)

const now = computed(()=>details.runtime.list[details.runtime.list.length -1])

const init = value =>{
  if(value!==-1){
    details.base={}
    details.runtime={ list:[] }
    get(`/api/monitor/details?clientId=${value}`,data=>Object.assign(details.base, data))
    get(`/api/monitor/runtime_history?clientId=${value}`,data=>Object.assign(details.runtime, data))

  }
}
watch(()=>props.id,init,{immediate:true})
</script>

<template>
  <el-scrollbar>
    <div class="client-details" v-loading="Object.keys(details.base).length===0">
      <div v-if="Object.keys(details.base).length">
        <div class="title">
          <i class="fa-solid fa-server"></i>
          服务器信息
        </div>
        <el-divider style="margin: 10px 0"/>
        <div class="details-list">
          <div>
            <span>服务器ID</span>
            <span>{{details.base.id}}</span>
          </div>
          <div>
            <span>服务器名称</span>
            <span style="margin-right: 10px">{{details.base.name}}</span>
            <i class="fa-solid fa-pen-to-square interact-item" @click.stop="rename(details.base.id,details.base.name,updateDetails)"></i>
          </div>
          <div>
            <span>运行状态</span>
            <span>
            <i style="color: #02ca02" class="fa-solid fa-circle-play" v-if="details.base.online"></i>
            <i style="color: #8a8a8a" class="fa-solid fa-circle-stop" v-else></i>
            {{details.base.online ? '运行中':'离线'}}
          </span>
          </div>
          <div>
            <span>公网IP地址</span>
            <span>
            {{details.base.ip}}
            <i class="fa-solid fa-copy interact-item" style="color: dodgerblue" @click.stop="copyIp(details.base.ip)"></i>
          </span>
          </div>
          <div v-if="!details.editNode">
            <span>服务器节点</span>
            <span :class="`fi fi-${details.base.location}`"></span>&nbsp;
            <span>{{details.base.node}}</span>&nbsp;
            <i @click.stop="enableNodeEdit"
               class="fa-solid fa-pen-to-square interact-item"/>
          </div>
          <div v-else>
            <span>服务器节点</span>
            <div style="display: inline-block;height: 15px">
              <div style="display: flex">
                <el-select v-model="nodeEdit.location" style="width: 80px" size="small">
                  <el-option v-for="item in locations" :value="item.name">
                    <span :class="`fi fi-${item.name}`"></span>&nbsp;
                    {{item.desc}}
                  </el-option>
                </el-select>
                <el-input v-model="nodeEdit.name" style="margin-left: 10px"
                          size="small" placeholder="请输入节点名称..."/>
                <div style="margin-left: 10px">
                  <i @click.stop="submitNodeEdit" class="fa-solid fa-check interact-item"/>
                </div>
              </div>
            </div>
          </div>
          <div style="display: flex">
            <span>处理器</span>
            <span>{{details.base.cpuName}}</span>
            <el-image style="margin-left: 10px;height: 20px"
                      :src="`/cpu-icons/${cpuNameToImage(details.base.cpuName)}`"/>
          </div>
          <div>
            <span>硬件信息</span>
            <i class="fa-solid fa-microchip"></i>
            <span>{{` ${details.base.cpuCore} CPU 核心数 / `}}</span>
            <i class="fa-solid fa-memory"></i>
            <span>{{` ${details.base.memory.toFixed(1)} GB 内存容量`}}</span>
          </div>
          <div>
            <span>操作系统</span>
            <i :style="{color: osNameToIcon(details.base.osName).color}"
               :class="`fa-brands ${osNameToIcon(details.base.osName).icon}`"
            />
            <span style="margin-left: 10px">{{`${details.base.osName} ${details.base.osVersion}`}}</span>
          </div>
        </div>
        <div class="title">
          <i class="fa-solid fa-gauge-high"></i>
          实时监控
        </div>
        <el-divider style="margin: 10px 0"/>
        <div v-if="details.base.online" v-loading="!details.runtime.list.length"
             style="min-height: 200px">
          <div style="display: flex" v-if="details.runtime.list.length">
            <el-progress type="dashboard" :width="100" :percentage="now.cpuUsage * 100"
                         :status="percentageToStatus(now.cpuUsage *100)">
              <div style="font-size: 17px;font-weight: bold;color: initial">CPU</div>
              <div style="font-size: 13px;color: grey;margin-top: 5px">{{ (now.cpuUsage * 100).toFixed(1) }}%</div>
            </el-progress>
            <el-progress style="margin-left: 20px" type="dashboard" :width="100"
                         :percentage="now.memoryUsage / details.runtime.memory * 100"
                         :status="percentageToStatus(now.memoryUsage / details.runtime.memory * 100)">
              <div style="font-size: 16px;font-weight: bold;color: initial">内存</div>
              <div style="font-size: 13px;color: grey;margin-top: 5px">{{ (now.memoryUsage).toFixed(1) }} GB</div>
            </el-progress>
            <div style="flex: 1;margin-left: 30px;display: flex;flex-direction: column;height: 80px">
              <div style="flex: 1;font-size: 14px">
                <div>实时网络速度</div>
                <div>
                  <i style="color: orange" class="fa-solid fa-arrow-up"></i>
                  <span>{{` ${fitByUnit(now.networkUpload, 'KB')}/s`}}</span>
                  <el-divider direction="vertical"/>
                  <i style="color: dodgerblue" class="fa-solid fa-arrow-down"></i>
                  <span>{{` ${fitByUnit(now.networkDownload, 'KB')}/s`}}</span>
                </div>
              </div>
              <div>
                <div style="font-size: 13px;display: flex;justify-content: space-between">
                  <div>
                    <i class="fa-solid fa-hard-drive"></i>
                    <span> 磁盘总容量</span>
                  </div>
                  <div>{{now.diskUsage.toFixed(1)}} GB / {{details.runtime.disk.toFixed(1)}} GB</div>
                </div>
                <el-progress type="line" :show-text="false"
                             :status="percentageToStatus(now.diskUsage / details.runtime.disk * 100)"
                             :percentage="now.diskUsage / details.runtime.disk * 100" />
              </div>
            </div>
          </div>
          <runtime-history style="margin-top: 20px" :data="details.runtime.list"/>
        </div>
        <el-empty description="服务器处于离线状态，请检查服务器是否正常运行" v-else/>
      </div>
    </div>
  </el-scrollbar>
</template>

<style scoped>

.interact-item{
  transition: .3s;

  &:hover{
    cursor: pointer;
    scale: 1.1;
    opacity: 0.8;
  }
}

.client-details{
  height: 100%;
  padding: 20px;
}

.title{
  color: var(--el-color-primary);
  font-size: 18px;
  font-weight: bold;
}
.details-list{
  font-size: 14px;

  & div{
    margin-bottom: 10px;
    & span:first-child{
      color: grey;
      font-size: 13px;
      font-weight: normal;
      width: 120px;
      display: inline-block;
    }
    & span{
      font-weight: bold;
    }
  }
}
</style>