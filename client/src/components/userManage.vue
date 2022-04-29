<template>
  <span>
    <div class="userManageContainer">
      <div class="userManage" style="border-bottom: 1px solid #ebeef5">
        <h2 :style="{ color: bgColors.bgHeader }">用户信息管理</h2>
      </div>
      <div class="searchContainer">
        <span
          ><el-input
            v-model="searchValue"
            placeholder="请输入学生姓名"
          ></el-input
          ><el-button type="primary" @click="handleGetUserList"
            ><i class="iconfont icon-sousuo" style="margin-right:3px"/>搜索</el-button
          ></span
        >
      </div>
      <div class="table">
        <el-table
          :data="userData"
          border
          :style="`width:100%;--el-table-row-hover-bg-color:${bgColors.bgHover}`"
        >
          <el-table-column label="序号" type="index" width="80" align="center">
          </el-table-column>
          <el-table-column label="学生姓名" width="150" show-overflow-tooltip>
            <template #default="scope">
              <span>{{ scope.row.username }}</span>
            </template>
          </el-table-column>
          <el-table-column label="学生密码" width="150"  show-overflow-tooltip>
            <template #default="scope">
              <span>{{ scope.row.password }}</span>
            </template>
          </el-table-column>
          <el-table-column label="学生邮箱" width="200"  show-overflow-tooltip>
            <template #default="scope">
              <span>{{ scope.row.email }}</span>
            </template>
          </el-table-column>
          <el-table-column label="学生生日" width="120">
            <template #default="scope">
              <span>{{ formatDate(scope.row.birthday) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="学生余额" width="150">
            <template #default="scope">
              <span>{{ scope.row.balance }}</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" width="200">
            <template #default="scope">
              <el-button size="small" @click="handleEdit(scope.row)"
                >编辑</el-button
              >
              <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
          style="border: 1px solid #ebeef5; border-top: none"
          v-model:currentPage="pageInfo.page"
          v-model:page-size="pageInfo.pageSize"
          :total="pageInfo.total"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <!-- 编辑用户层 -->
    <userEditor
      :userData="subUser"
      v-show="showEditor"
      @hideEditor="hideEdit"
      @handleUpdate="handleUpdate"
    ></userEditor>
  </span>
</template>

<script>
import { computed, reactive, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { getUserList } from "../axios/user";
import { formatDate } from "../utils/dateFormater";
import { ElMessage, ElMessageBox } from "element-plus";
import { deleteUserById, updateUser } from "../axios/user";
import userEditor from "./userEditor.vue";
export default {
  name: "userManage",
  components: {
    userEditor,
  },
  setup() {
    // 获取当前背景颜色配色
    const store = useStore();
    let bgColors = computed(() => store.getters.bgColors);
    let searchValue = ref("");
    const showEditor = ref(false);

    let pageInfo = reactive({
      total: 100,
      page: 1,
      pageSize: 5,
    });
    let userData = reactive([]);
    let subUser = reactive({});
    // 打开编辑页面
    const handleEdit = (info) => {
      Object.assign(subUser, info);
      showEditor.value = true;
    };
    // 隐藏编辑页面
    const hideEdit = () => {
      showEditor.value = false;
    };
    // 获取用户列表
    const handleGetUserList = async () => {
      try {
        let res = await getUserList({
          page: pageInfo.page,
          pageSize: pageInfo.pageSize,
          searchValue: searchValue.value,
        });
        pageInfo.total = res.data.total;
        // 如果获取不到，则往前翻页
        if (res.data.userData.length == 0 && pageInfo.page > 1) {
          pageInfo.page--;
          handleGetUserList();
        } else {
          userData.splice(0, userData.length, ...res.data.userData);
        }
      } catch (e) {
        console.log(e);
        return;
      }
    };
    onMounted(() => {
      handleGetUserList(); // 获取用户列表
    });
    // 删除用户
    const handleDelete = (id) => {
      ElMessageBox.confirm("您确定要永久删除该用户吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          try {
            await deleteUserById({ id });
            handleGetUserList(); // 重新获取数据
            ElMessage({
              type: "success",
              message: "删除成功",
            });
          } catch (e) {
            return;
          }
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "取消删除",
          });
        });
    };
    // 更新用户
    const handleUpdate = async (data) => {
      try {
        await updateUser(data);
        showEditor.value = false;
        handleGetUserList();
        ElMessage.success("更新用户成功");
      } catch (e) {
        console.log(e);
        return
      }
    };

    /**
     * 处理分页逻辑
     */
    // 当页数发生变化的时候，要重新获数据
    const handleSizeChange = () => {
      handleGetUserList(); // 获取用户列表
    };
    // 当前页发生变化的时候，也要重新获取页码
    const handleCurrentChange = () => {
      handleGetUserList(); // 获取用户列表
    };
    return {
      handleGetUserList,
      handleCurrentChange,
      pageInfo,
      bgColors,
      userData,
      searchValue,
      handleEdit,
      handleDelete,
      handleSizeChange,
      formatDate,
      subUser,
      showEditor,
      hideEdit,
      handleUpdate,
    };
  },
};
</script>

<style lang = 'scss' >
.userManageContainer {
  margin: 10px auto;
  display: inline-block;
  background-color: #ffffff;
  overflow: hidden;

  h2 {
    padding-left: 15px;
    text-align: left;
  }
  .userManage {
    height: 50px;
    line-height: 50px;
  }
  .searchContainer {
    padding: 0 15px;
    overflow: hidden;
    margin-top: 10px;
    height: 40px;
    line-height: 50px;
    span {
      line-height: 50px;
      float: right;
      display: flex;
      .el-input {
        margin-right: 5px;
        width: 200px;
        height: 50px;
      }
    }
  }
  .table {
    padding: 0 15px 15px;
    th {
      background-color: #f2f2f2;
    }
    .el-table__inner-wrapper {
      font-size: 17px;
    }
    .el-button {
      font-size: 17px;
    }
    .el-pagination {
      height: 50px;
      line-height: 50px;
      padding-left: 21px;
    }
  }
}
</style>