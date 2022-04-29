<template>
  <div class="login">
    <header><h2>登录</h2></header>
    <section>
      <div class="by-other">
        <div class="qq">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-QQ"></use></svg
          >QQ登录
        </div>
        <div class="weixin">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-weixin1"></use></svg
          >微信登录
        </div>
        <div class="zhifubao">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-zhifubao"></use></svg
          >支付宝登录
        </div>
      </div>
      <div class="cut"></div>
      <div class="or" style="display: none">或 手动登录</div>
      <div class="by-passwod">
        <el-input placeholder="用户名" v-model="loginDate.name">
          <template #prefix>
            <i
              class="iconfont icon-ziyuanxhdpi"
              style="font-size: 16px"
            ></i> </template
        ></el-input>

        <el-input
          type="password"
          placeholder="密码"
          v-model="loginDate.password"
          show-password
          @keyup.enter="handleLogin"
          ><template #prefix>
            <i
              class="iconfont icon-mima"
              style="font-size: 20px"
            ></i> </template
        ></el-input>

        <div class="submit" @click="handleLogin">登录</div>
      </div>
    </section>
    <footer>
      <div @click="$router.push('/register')">注册</div>
      <div class="forget" @click="$router.push('/changePass')">忘记密码?</div>
    </footer>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import { reactive} from "vue";
import { useRouter } from "vue-router";
import {userLogin} from "../axios/user"
export default {
  name: "login",
  setup() {
    const loginDate = reactive({
      name: "",
      password: "",
    });
    const router = useRouter();
    const handleLogin = async () => {
      if (!loginDate.name || !loginDate.password) {
        ElMessage({
          type: "error",
          message: "账号密码不能为空",
        });
        return;
      }
      // 登录逻辑
      try{
        await userLogin({username:loginDate.name,password:loginDate.password})
      }catch(err){
        return
      }
      ElMessage.success(`${loginDate.name},欢迎您`)
      router.push("/menu");
    };
    return {
      loginDate,
      handleLogin,
    };
  },
};
</script>

<style lang="scss">
.login {
  width: 650px;
  height: 300px;
  position: absolute;
  margin: auto;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  padding-top: 30px;
  background-color: #f2f2f2;
  border-radius: 5px;
  overflow: hidden;
  // 中间部分
  section {
    display: flex;
    justify-content: center;
    // 其他方式登录
    .by-other {
      width: 320px;
      padding: 15px 35px 35px 35px;
      display: flex;
      flex-direction: column;
      div {
        cursor: pointer;
        width: 250px;
        height: 40px;
        line-height: 40px;
        text-align: left;
        padding-left: 10px;
        border-radius: 4px;
        margin-bottom: 8px;
        &:hover {
          box-shadow: 1px 1px 1px 1px #ccc;
          transform: translate(-0.5px, -0.5px);
        }
      }
      svg {
        font-size: 20px;
        margin-right: 5px;
      }
      .qq {
        background-color: #2e86c5;
        color: #ffffff;
      }
      .weixin {
        background-color: #7ab73e;
        color: #ffffff;
      }
      .zhifubao {
        padding-left: 7px;
        background-color: #d8d8d8;
        color: #6c6c6c;
        svg {
          font-size: 23px;
          vertical-align: middle;
        }
      }
    }
    // 中间的分割模块
    .cut {
      position: relative;
      &::after {
        content: "";
        display: block;
        width: 4px;
        height: 155px;
        position: absolute;
        top: 10px;
        border-radius: 2px;
        background-color: #dddddd;
      }
      &::before {
        content: "or";
        display: block;
        width: 30px;
        height: 30px;
        position: absolute;
        left: -13px;
        top: 67px;
        border: 1px solid #dddddd;
        background-color: #f2f2f2;
        z-index: 999999;
        border-radius: 50%;
        text-align: center;
        line-height: 30px;
      }
    }
    // 账号密码登录
    .by-passwod {
      width: 320px;
      padding: 15px 35px 35px 35px;
      display: flex;
      flex-direction: column;
      margin-top: 7px;
      .el-input {
        .el-input__inner {
          height: 35px;
          line-height: 35px;
          margin-bottom: 8px;
        }
        i::before {
          line-height: 33px;
          margin-left: -5px;
        }
        .icon-ziyuanxhdpi::before {
          margin-left: -3px;
        }
      }
      .submit {
        height: 35px;
        line-height: 35px;
        width: 250px;
        margin-bottom: 8px;
        border-radius: 4px;
        padding: 0;
        background-color: #66b969;
        cursor: pointer;
        color: #ffffff;
        &:hover {
          box-shadow: 1px 1px 1px 1px #ccc;
        }
      }
    }
  }
  // 底部
  footer {
    display: flex;
    height: 45px;
    color: #ccc;
    text-align: center;
    div {
      width: 325px;
      background-color: #666666;
      cursor: pointer;
      line-height: 45px;
      text-align: center;
      &:hover {
        color: #ffffff;
      }
    }
  }
}

@media screen and (max-width: 650px) {
  .login {
    width: 100%;
    // 设置最小宽度
    min-width: 300px;
    max-width: 340px;
    height: 429px;
    padding-top: 10px;
    section {
      flex-direction: column;
      align-items: center;
      .cut {
        display: none;
      }
      .or {
        display: block !important;
        font-size: 12px;
        padding: 2px 0;
        color: rgb(172, 171, 171);
      }
      .by-other {
        padding-bottom: 0;
      }
      .by-passwod {
        padding-top: 0;
        padding-bottom: 10px;
      }
    }
    footer {
      flex-direction: column;
      align-items: center;
      div {
        height: 30px;
        line-height: 30px;
        width: 100%;
      }
    }
  }
}
</style>