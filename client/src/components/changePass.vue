<template>
  <div class="changePass">
    <header><h2>修改密码</h2></header>
    <el-form :rules="rules" :model="passDate" ref="ruleFormRef">
      <el-form-item prop="username">
        <el-input v-model="passDate.username" placeholder="用户名">
          <template #prefix>
            <i
              class="iconfont icon-ziyuanxhdpi"
              style="font-size: 16px"
            ></i> </template></el-input
      ></el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="passDate.password"
          type="password"
          placeholder="新密码"
          ><template #prefix>
            <i
              class="iconfont icon-mima"
              style="font-size: 20px"
            ></i> </template
        ></el-input>
      </el-form-item>
      <el-form-item prop="confirmPass">
        <el-input
          v-model="passDate.confirmPass"
          type="password"
          placeholder="确认密码"
          ><template #prefix>
            <i
              class="iconfont icon-mima"
              style="font-size: 20px"
            ></i> </template
        ></el-input>
      </el-form-item>
      <div class="submit" @click="handleChange(ruleFormRef)">确认修改</div>
    </el-form>
    <footer @click="$router.push('/login')">返回登陆界面</footer>
  </div>
</template>

<script >
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import {userChangePass} from "../axios/user"
export default {
  name: "changePass",
  setup() {
    const ruleFormRef = ref();
    const router = useRouter();
    const passDate = reactive({
      username: "",
      password: "",
      confirmPass: "",
    });

    // 检查两次密码是否一致
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请确认密码"));
      } else if (value !== passDate.password) {
        callback(new Error("两次输入的密码不一致！"));
      } else {
        callback();
      }
    };
    // 表单校验规则
    const rules = reactive({
      username: [
        { required: true, message: "用户名不能为空", trigger: "blur" },
        { min: 3, max: 12, message: "用户名长度为3-12", trigger: "blur" },
      ],
      password: [
        { required: true, message: "密码不能为空", trigger: "blur" },
        { min: 6, max: 15, message: "密码长度为6-15", trigger: "blur" },
      ],
      confirmPass: [{ validator: validatePass, trigger: "blur" }],
    });

    const handleChange = async (ruleF) => {
      await ruleF.validate(async (valid, fields) => {
        if (valid) {
          try {
            await userChangePass({username:passDate.username,password:passDate.password})
            ElMessage({
              type: "success",
              message: "修改成功，快去登录吧",
            });
            router.push("/login");
          } catch (e) {
            return;
          }
        } else {
          return;
        }
      });
    };
    return {
      passDate,
      rules,
      handleChange,
      ruleFormRef,
    };
  },
};
</script>

<style lang='scss'>
.changePass {
  width: 380px;
  height: 340px;
  position: absolute;
  margin: auto;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  padding-top: 13px;
  background-color: #f2f2f2;
  border-radius: 4px;
  overflow: hidden;
  .el-form {
    margin: 0 auto;
    width: 80%;
    .el-form-item {
      margin: 0;
      .el-input {
        width: 100%;
        margin-top: 17px;
        .el-input__inner {
          height: 40px;
          font-size: 16px;
        }
        i::before {
          line-height: 37px;
          margin-left: -5px;
        }
        .el-input__icon {
          font-size: 20px !important;
        }
        .icon-ziyuanxhdpi::before {
          margin-left: -3px;
        }
      }
    }
    .submit {
      margin: 20px auto;
      width: 100%;
      padding: 0;
      height: 40px;
      line-height: 40px;
      text-align: center;
      background-color: #66b969;
      user-select: none;
      cursor: pointer;
      color: #ffffff;
      &:hover {
        box-shadow: 1px 1px 1px 1px #ccc;
      }
      border-radius: 4px;
    }
  }

  footer {
    width: 100%;
    height: 45px;
    line-height: 45px;
    background-color: #666666;
    color: #ccc;
    cursor: pointer;
    user-select: none;
    line-height: 45px;
    text-align: center;
    margin-top: 0px;
    &:hover {
      color: #ffffff;
    }
  }
}


</style>