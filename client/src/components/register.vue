<template>
  <div class="regester">
    <header><h2>注册</h2></header>
    <el-form :rules="rules" :model="regesterDate" ref="ruleFormRef">
      <el-form-item prop="username">
        <el-input v-model="regesterDate.username" placeholder="用户名">
          <template #prefix>
            <i
              class="iconfont icon-ziyuanxhdpi"
              style="font-size: 16px"
            ></i> </template></el-input
      ></el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="regesterDate.password"
          type="password"
          placeholder="密码"
          show-password
          ><template #prefix>
            <i
              class="iconfont icon-mima"
              style="font-size: 20px"
            ></i> </template
        ></el-input>
      </el-form-item>

      <el-form-item prop="email">
        <el-input v-model="regesterDate.email" placeholder="电子邮箱"
          ><template #prefix>
            <i
              class="iconfont icon-youxiang"
              style="font-size: 20px; line-height: 41px"
            ></i> </template
        ></el-input>
      </el-form-item>

      <el-form-item prop="birthday">
        <el-date-picker
          v-model="regesterDate.birthday"
          class="birthdayPick"
          type="date"
          placeholder="请选择生日"
          format="YYYY-MM-DD"
          :editable="false"
          validate-even="true"
        />
      </el-form-item>

      <div class="submit" @click="handleRegester(ruleFormRef)">注册</div>
    </el-form>
    <footer @click="$router.push('/login')">返回登陆界面</footer>
  </div>
</template>

<script >
import { reactive, ref, toRaw } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { userRegister } from "../axios/user";
import { formatDate } from '../utils/dateFormater';
export default {
  name: "regester",
  setup() {
    const ruleFormRef = ref();
    const router = useRouter();
    const regesterDate = reactive({
      username: "",
      password: "",
      email: "",
      birthday: "",
    });

    // 验证日期
    const checkDate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请选择出生日期"));
      }
      let now = new Date().getTime();
      if (value > now) {
        callback(new Error("出生日期不合理"));
      }
      callback();
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
      email: [
        {
          required: true,
          message: "请输入邮箱",
          trigger: "blur",
        },

        {
          type: "email",
          message: "请输入正确的邮箱",
          trigger: ["blur", "change"],
        },
      ],
      birthday: [
        {
          validator: checkDate,
          trigger: "blur",
        },
      ],
    });

    // 注册
    const handleRegester = async (ruleF) => {
      await ruleF.validate(async (valid, fields) => {
        if (valid) {
          try {
            regesterDate.birthday = formatDate( regesterDate.birthday )
            await userRegister(toRaw(regesterDate));
            ElMessage({
              type: "success",
              message: "注册成功，快去登录吧",
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
      regesterDate,
      rules,
      ruleFormRef,
      handleRegester,
    };
  },
};
</script>

<style lang='scss'>
.regester {
  width: 380px;
  height: 355px;
  position: absolute;
  margin: auto;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  padding-top: 8px;
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
        margin-top: 10px;
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
      .birthdayPick {
        .el-input__prefix-inner {
          margin-left: -4px;
          .el-input__icon {
            font-size: 18px !important;
          }
        }
      }
    }
    .submit {
      margin: 15px auto;
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