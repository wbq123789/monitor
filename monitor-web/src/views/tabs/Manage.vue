<script setup>
import PreviewCard from "@/component/PreviewCard.vue";
import {reactive, ref} from "vue";
import {get} from "@/net";
import ClientDetails from "@/component/ClientDetails.vue";
import RegisterCard from "@/component/RegisterCard.vue";
import {Plus} from "@element-plus/icons-vue";

const list=ref([])
const updateList=()=>get("/api/monitor/list",data => list.value = data)
setInterval(updateList,10000)
updateList()
const register = reactive({
  show: false,
  token: ''
})
const detail=reactive({
  show:false,
  id:-1
})
const displayClientDetails=(id)=>{
  detail.show=true
  detail.id=id
}
const refreshToken=()=>get('/api/monitor/register',token=>register.token=token);
</script>

<template>
<div class="manage-main">
  <div style="display: flex;justify-content: space-between;align-items: end">
    <div>
      <div class="title"><i class="fa-solid fa-server"></i> 管理主机列表</div>
      <div class="desc">在这里你可以管理你的各个服务器，并快速查看其内存、CPU等实时监控数据</div>
    </div>
    <div>
      <el-button :icon="Plus" type="primary" plain
                 @click="register.show =true">添加新主机</el-button>
    </div>
  </div>
  <el-divider style="margin: 10px 0 "/>
  <div class="card-list" v-if="list.length">
    <preview-card v-for="item in list" :data="item" :update="updateList" @click="displayClientDetails(item.id)"/>
  </div>
  <el-empty description="当前无主机连接，请点击添加主机按钮" v-else/>
  <el-drawer size="520" :show-close="false" v-model="detail.show"
             :with-header="false" v-if="list.length" @close="detail.id=-1">
    <client-details :id="detail.id" :update="updateList" @delete="updateList"/>
  </el-drawer>
  <el-drawer v-model="register.show" direction="btt"
             style="width: 600px;margin: 10px auto"
             :with-header="false" size="320" @open="refreshToken">
    <register-card :token="register.token"/>
  </el-drawer>
</div>
</template>

<style scoped>
:deep(.el-drawer){
  margin: 10px;
  height: calc(100% - 20px);
  border-radius: 10px;
}
:deep(.el-drawer__body){
  padding: 0;
}
.manage-main{
  margin: 0 50px;
  .title{
    font-size: 22px;
    font-weight: bold;
  }
  .desc{
    font-size: 15px;
    color: grey;
  }
  .card-list{
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
  }
}
</style>