<template>
  <div class="articleManage">
    <overview
      v-show="isOverview"
      :data="articleOverview"
      @changeView="changeView"
    ></overview>
    <detail
      v-show="!isOverview"
      :data="articleDetail"
      @reGetDetile="reGetDetile"
      @deleteArticle="deleteArticle"
      @handleSubmitArticle="handleSubmitArticle"
      @changeView="changeView"
    ></detail>
    <!-- 分页 -->
     <el-pagination
      class="page"
      style="border: 1px solid #ebeef5; border-top: none;border-radius:2px"
      v-model:currentPage="pageInfo.page"
      v-model:page-size="pageInfo.pageSize"
      :total="pageInfo.total"
      :page-sizes="[5, 10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
   
  </div>
</template>

<script>
import { reactive, ref, onMounted } from "vue";
import overview from "./artileComponents/overview.vue";
import detail from "./artileComponents/detail.vue";
import {
  getArticleOverview,
  getArticleList,
  deleteArticleById,
  addArticle,
  updateArticle,
} from "../axios/article";
import { ElMessage } from "element-plus/lib/components";
export default {
  name: "articleManage",
  components: {
    overview,
    detail,
  },

  setup() {
    let isOverview = ref(true); // 展示的是第一个组件
    let articleOverview = reactive([]);
    let articleDetail = reactive({ userInfo: {}, articleDetail: [], total: 0 });
    // 分页信息
    let pageInfo = reactive({
      total: 0,
      page: 1,
      pageSize: 5,
    });
    let authorId = ref(0);
    let searchValue = ref('');

    // 获取文章总览
    const handleGetArticleOverview = async () => {
      try {
        let res = await getArticleOverview({
          page: pageInfo.page,
          pageSize: pageInfo.pageSize,
        });
        // 没有返回数据则尝试向前翻页
        if (res.data.articleOverview.length == 0 && pageInfo.page != 0) {
          pageInfo.page--;
          handleGetArticleOverview();
        } else {
          pageInfo.total = res.data.total;
          console.log(pageInfo.total)
          articleOverview.splice(
            0,
            articleOverview.length,
            ...res.data.articleOverview
          );
        }
      } catch (e) {
        return;
      }
    };

    // 获取文章列表
    const handleGetArticleDetail = async () => {
      try {
        let res = await getArticleList({
          authorId: authorId.value,
          searchValue: searchValue.value,
          page: pageInfo.page,
          pageSize: pageInfo.pageSize,
        });
        // 返回为空向前翻页
        if (res.data.articleDetail.length == 0 && pageInfo.page > 1) {
          pageInfo.page--;
          handleGetArticleDetail();
        } else {
          pageInfo.total = res.data.total;
          Object.assign(articleDetail, res.data); // 重新给articleDetail赋值
        }
      } catch (e) {
        return;
      }
    };

    // 删除某篇文章
    const deleteArticle = async (articleId,authorId) => {
      try {
        await deleteArticleById({ articleId,authorId}); // 把athoorId加上
        ElMessage.success("删除成功");
        handleGetArticleDetail(); // 重新获取数据
      } catch (e) {
        return;
      }
    };

    // 更新文章
    const handleSubmitArticle = async (newData) => {
      try {
        // 有articleId则是更新文章
        if (newData.articleId) {
          // 更新文章
          await updateArticle(newData);
          console.log('更新文章成功')
          ElMessage.success("更新文章成功");
        } else {
          // 新增文章
          await addArticle(newData);
          ElMessage.success("添加文章成功");
        }
        //重新获取文章
        handleGetArticleDetail() 
      } catch (e) {
        return;
      }
    };

    onMounted(() => {
      handleGetArticleOverview(); // 获取文章总览
    });

    // 页码数改变的时候
    const handleSizeChange = () => {
      if (isOverview.value) {
        handleGetArticleOverview();
      } else {
        handleGetArticleDetail();
      }
    };
    // 当前页改变的时候
    const handleCurrentChange = () => {
      if (isOverview.value) {
        handleGetArticleOverview();
      } else {
        handleGetArticleDetail();
      }
    };

    // 改变当前的显示
    const changeView = (Id) => {
      isOverview.value = !isOverview.value; // 改变
      // 重置一下分页器
      pageInfo.page = 1;
      pageInfo.pageSize = 5;
      pageInfo.total = 0;
      searchValue.value = ""
      // 如果是总览页面
      if (isOverview.value==true) {
        handleGetArticleOverview(); // 直接获取数据
      } else {   // 进入的是文章管理
        authorId.value = Id;
       
        handleGetArticleDetail();
      }
    };

    // 重新获取文章详细
    const reGetDetile = (SearchValue) => {
      searchValue.value = SearchValue;
      handleGetArticleDetail();
    };
    return {
      isOverview,
      articleOverview,
      pageInfo,
      handleSizeChange,
      handleCurrentChange,
      articleDetail,
      changeView,
      reGetDetile,
      deleteArticle,
      handleSubmitArticle,
    };
  },
};
</script>

<style lang = "scss">
.articleManage {
  width: 100%;
  overflow: hidden;
  padding: 10px;
  padding-bottom: 0;
  .page {
    background-color: #ffffff;
  }
}
</style>