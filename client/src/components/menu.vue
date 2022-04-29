<template>
  <!-- 头部 -->
  <header class="navHeader" :style="{ backgroundColor: bgColors.bgHeader }">
    <div class="menu-btn">
      <i
        class="iconfont icon-zhankaicaidan btn-icon"
        v-show="hideMenu"
        @click="hideMenu = false"
      ></i>
      <i
        class="iconfont icon-shouqicaidan btn-icon"
        v-show="!hideMenu"
        @click="hideMenu = true"
      ></i>
    </div>
    <div class="title">WEB技术课程系统</div>
    <div class="logo">
      <div class="left"><img src="../assets/logo.jpg" alt="头像" /></div>
      <div class="right">
        <div class="name">{{ username }}</div>
        <div class="click" @click.stop="handleLogout">退出登录</div>
      </div>
    </div>
  </header>
  <!-- 菜单 -->
  <div class="main-container">
    <div :class="{ menu: true, hideMenu: hideMenu }">
      <el-menu
        class="elMenuVerticalDemo"
        text-color="#fff"
        :default-active="menuId"
        :backgroundColor="bgColors.bgMenu"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="1" @click="router.push('/menu/home')">
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="2" @click="router.push('/menu/personal')">
          <span>个人中心</span>
        </el-menu-item>
        <el-menu-item index="3" @click="router.push('/menu/userManage')">
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="4" @click="router.push('/menu/articleManage')">
          <span>文章管理</span>
        </el-menu-item>
        <el-sub-menu index="5">
          <template #title>
            <span>背景颜色</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="5-1" @click="store.commit('changeBg', 0)"
              >蓝色</el-menu-item
            >
            <el-menu-item index="5-2" @click="store.commit('changeBg', 1)"
              >绿色</el-menu-item
            >
            <el-menu-item index="5-3" @click="store.commit('changeBg', 2)"
              >灰色</el-menu-item
            >
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
    </div>

    <!-- 路由 -->
    <router-view class="content" />
  </div>
  <!-- 底部 -->
  <footer class="footer" :style="{ backgroundColor: bgColors.bgFoot }">
    cjf © 2022
  </footer>
</template>

<script>
import { computed, nextTick } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ref, watch, reactive, watchEffect, onMounted } from "vue";
import Cookies from "js-cookie";
import bus from "vue3-eventbus";
export default {
  name: "Menu",
  setup() {
    const store = useStore();
    const hideMenu = ref(false);
    let username=ref();
    onMounted(() => {
      username.value = Cookies.get("username");
    });

    const router = useRouter();
    const menuId = computed(() => {
      return router.currentRoute.value.meta.menuId + "";
    });
    // 获取当前背景颜色配色
    let bgColors = computed(() => store.getters.bgColors);

    // 监听屏幕宽度变化回调
    onMounted(() => {
      window.addEventListener("resize", () => {
        let screenWidth = document.body.clientWidth;
        if (screenWidth <= 800) {
          hideMenu.value = true;
        } else {
          hideMenu.value = false;
        }
      });
    });
    // 当隐藏或者展现菜单的时候，echart要重画
    watch(hideMenu, () => {
      setTimeout(() => {
        bus.emit("changeChart");
      });
    });

    // 退出登录
    const handleLogout=()=>{
      Cookies.remove('token')
      Cookies.remove('username')
      location.reload()
    }

    return{
      bgColors,
      store,
      menuId,
      hideMenu,
      router,
      username,
      handleLogout
    };
  },
};
</script>

<style lang='scss'>
$headHeight: 50px;
$footerHeight: 26px;
$menuFontSize: 16px;
$fontColor: #fff;
// 头部
.navHeader {
  height: $headHeight;
  width: 100%;
  position: fixed;
  top: 0;
  .title {
    position: absolute;
    left: 200px;
    line-height: $headHeight;
    height: $headHeight;
    font-weight: 700;
    color: $fontColor;
    font-size: 33px;
  }
  .menu-btn {
    width: 30px;
    height: 30px;
    position: absolute;
    top: 10px;
    left: 20px;
    overflow: hidden;
  }
  .btn-icon {
    cursor: pointer;
    transition: all 1s;
    font-size: 30px;
    color: $fontColor;
  }
  // 右边logo模块
  .logo{
    color: $fontColor;
    height: 100%;
    width: 150px;
    display: flex;
    float: right;
    .left{
      width: 50px;
      height: 50px;
      border-radius: 50px;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;

      }
      
      
    }
    .right{
      margin-left: 10px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .click{
          cursor: pointer;
          user-select: none;
        }
      }
  }
}

// 底部
.footer {
  height: $footerHeight;
  width: 100%;
  position: fixed;
  bottom: 0;
  line-height: $footerHeight;
  color: $fontColor;
  font-size: 20px;
}
.main-container {
  transition: all 0.4s;
  position: relative;
  top: $headHeight;
  height: calc(100% - $headHeight - $footerHeight);
  display: flex;
  .menu {
    position: relative;
    user-select: none;
    height: 100%;
    width: 200px;
    transition: all 0.4s;
    .el-menu {
      position: relative;
      width: 200px;
      height: 100%;
      .el-menu-item {
        font-size: $menuFontSize;
      }
      .el-sub-menu {
        .el-sub-menu__title * {
          font-size: $menuFontSize;
        }
      }
    }
  }
  .hideMenu {
    transform: translateX(-200px);
    width: 0;
  }
  .content {
    transition: all 0.4s;
    width: 100%;
    background-color: #f2f2f2;
    overflow: auto;
    position: relative;
  }
  
}
@media screen and (max-width: 800px) {
  .navHeader {
    .menu-btn {
      display: block;
    }
    .title {
      position: initial;
    }
  }
}
</style>