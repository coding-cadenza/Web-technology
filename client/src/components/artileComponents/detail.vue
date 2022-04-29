<template>
  <div class="detail">
    <el-card class="infoCard"
      ><el-form :model="userInfo" hide-required-asterisk>
        <h2 :style="{ color: bgColors.bgHeader }">用户文章信息维护</h2>

        <el-form-item label="用户名">
          <el-input :value="userInfo.username" :disabled="true" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input :value="userInfo.email" :disabled="true" />
        </el-form-item>
        <el-form-item label="生日">
          <el-input
            :value="formatDate(userInfo.birthday)"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="余额">
          <el-input :value="userInfo.balance" :disabled="true" />
        </el-form-item> </el-form
    ></el-card>
    <el-card class="articleCard">
      <template #header>
        <div class="card-header" style="text-align: left">
          <h3 :style="{ color: bgColors.bgHeader }">文章列表</h3>
        </div>
      </template>
      <div class="content">
        <span class="btn"
          ><el-button type="primary" @click="handleAddArticle()"
            ><i class="iconfont icon-jia1"></i> 新增</el-button
          ></span
        >
          <!-- 右下角的返回上一页的按钮 -->
    <el-button type="primary" class="back" @click="handleChangeView"><i class="iconfont icon-fanhui1"></i>返回</el-button>
        <span class="search"
          ><el-input
            v-model="searchValue"
            placeholder="请输入文章标题"
          ></el-input
          ><el-button type="primary" @click="handleReGetDetile()"
            ><i class="iconfont icon-sousuo"/>搜索</el-button
          ></span
        >
      </div>
      <!-- 表格 -->
      <el-table
        :data="articleDetail"
        class="articleTable"
        border
        :style="`--el-table-row-hover-bg-color:${bgColors.bgHover}`"
      >
        <el-table-column label="序号" type="index" width="80" align="center">
        </el-table-column>
        <el-table-column label="文章标题">
          <template #default="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="文章内容">
          <template #default="scope">
            <span>{{ scope.row.content }}</span>
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
              @click="handleDeleteArticle(scope.row.articleId)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div class="editorContainer" v-show="showEditor">
      <el-card class="editorCard">
        <template #header>
          <div class="card-header">
            <span>信息</span>
            <el-button class="button" type="text" @click="handleHideEditor"
              >取消</el-button
            >
          </div>
        </template>
        <el-form
          :model="newData"
          :rules="rules"
          ref="ruleFormRef"
          hide-required-asterisk
        >
          <el-form-item label="文章标题" prop="title">
            <el-input v-model="newData.title" />
          </el-form-item>
          <el-form-item label="文章内容" prop="content">
            <el-input v-model="newData.content" />
          </el-form-item>
          <el-button
            type="primary"
            @click="handleSubmit(ruleFormRef)"
            :loading="loading"
            >提交</el-button
          >
        </el-form>
      </el-card>
    </div>
  
  </div>
</template>

<script>
import { computed, toRefs, ref, reactive, onMounted, nextTick } from "vue";
import { useStore } from "vuex";
import { formatDate } from "../../utils/dateFormater";
import { ElMessage, ElMessageBox } from "element-plus";
export default {
  name: "detail",
  props: {
    data: Object,
  },
  emits: ["reGetDetile", "deleteArticle", "handleSubmitArticle","changeView"],
  setup(props, { emit }) {
    // 获取当前背景颜色配色
    const store = useStore();
    let bgColors = computed(() => {
      return store.getters.bgColors;
    });
    let articleDetail = computed(() => {
      return props.data.articleDetail;
    });
    let userInfo = computed(() => {
      return props.data.userInfo;
    });
    let searchValue = ref("");
    let showEditor = ref(false);
    let ruleFormRef = ref();
    let newData = reactive({
      title: "",
      content: "",
      articleId: "",
      authorId: "",
    });
    let nullData = { title: "", content: "", articleId: "", authorId: "" };
    let loading = ref(false);
    // 删除文章操作
    const handleDeleteArticle = (articleId) => {
      ElMessageBox.confirm("您确定要永久删除该文章吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          emit("deleteArticle", articleId, userInfo.value.id);
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "取消删除",
          });
        });
    };
    // 表单校验规则
    const rules = reactive({
      title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
      content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
    });
    const handleReGetDetile = () => {
      emit("reGetDetile", searchValue.value);
    };

    // 新增文章
    const handleAddArticle = () => {
      newData.authorId = userInfo.value.id; // 这里要将authorId记录进去
      showEditor.value = true;
    };

    // 关掉编辑页面
    const handleHideEditor = () => {
      showEditor.value = false;
      Object.assign(newData, nullData); // 清空消息
    };

    // 修改文章
    const handleEdit = (article) => {
      Object.assign(newData, article);
      showEditor.value = true;
    };

    // 提交更新文章操作
    const handleSubmit = async (ruleF) => {
      await ruleF.validate(async (valid, fields) => {
        if (valid) {
          try {
            loading.value = true;
            await emit("handleSubmitArticle", newData);
            nextTick(() => {
              handleHideEditor();
            });
            loading.value = false;
          } catch (e) {
            loading.value = false;
            return;
          }
        } else {
          return;
        }
      });
    };

    // 改变
    const handleChangeView=()=>{
      emit('changeView')

    }
    return {
      handleChangeView,
      bgColors,
      articleDetail,
      formatDate,
      userInfo,
      handleEdit,
      handleDeleteArticle,
      searchValue,
      handleReGetDetile,
      showEditor,
      rules,
      ruleFormRef,
      newData,
      handleAddArticle,
      handleHideEditor,
      handleSubmit,
      loading,
    };
  },
};
</script>

<style lang="scss">
.detail {
  position: relative;
  h2 {
    height: 30px;
    line-height: 30px;
  }

  .el-form {
    .el-form-item {
      .el-form-item__label {
        padding: 0;
        margin-right: 5px;
        background-color: #e8e8e8;
        text-align: center;
        width: 80px;
        font-weight: 500;
      }
      .el-input {
        width: 100%;
      }
    }
  }
  // 下面列表部分
  // 列表的中间部分
  .content {
    background-color: #ffffff;
    padding: 10px 0;
    overflow: hidden;
    .btn {
      float: left;
    }
    .back{
      float: left;
      margin-left: 5px;
    }
    .search {
      float: right;
      display: flex;
      .el-input {
        margin-right: 5px;
      }
    }
  }

  //文章列表部分
  .articleCard {
    box-shadow: none;
  }
  .el-card__body {
    padding-top: 0;
  }
  .infoCard {
    .el-card__body {
      padding: 10px;
    }
    margin-bottom: 10px;
  }

  //文章列表部分
  .articleTable {
    th {
      background-color: #f2f2f2;
    }
  }

  // 编辑信息部分
  .editorContainer {
    width: 100%;
    height: 100%;
    position: fixed;
    left: 0;
    top: 0;
    background-color: rgba($color: #000000, $alpha: 0.3);
    z-index: 999;
    .editorCard {
      opacity: 1;
      width: 500px;
      height: 400px;
      margin: auto;
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
      .el-card__header {
        padding: 3px 15px;
        background-color: #f8f8f8;
        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
      }
      .el-card__body {
        padding: 10px 10px;
      }
      .el-form-item {
        .el-form-item__label {
          padding: 0;
          margin-right: 5px;
          background-color: #f8f8f8;
          text-align: center;
          width: 80px;
          font-weight: 500;
        }
        .email {
          z-index: 999999;
        }
      }
    }
  }

  i{
    margin-right: 5px;
  }


  
}
</style>