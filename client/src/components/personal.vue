<template>
  <div class="personal">
    <el-menu
      default-active="1"
      :active-text-color="bgColors.bgHeader"
      mode="horizontal"
    >
      <el-menu-item
        index="1"
        :style="`--el-menu-hover-bg-color:${bgColors.bgHover}`"
        @click="handleSelect"
        >个人资料</el-menu-item
      >
      <el-menu-item
        index="2"
        :style="`--el-menu-hover-bg-color:${bgColors.bgHover}`"
        @click="handleSelect"
        >密码修改</el-menu-item
      >
    </el-menu>

    <!-- 修改个人资料的表单 -->
    <el-form
      class="infoForm"
      v-show="isInfo"
      :rules="rules"
      ref="baseFormRef"
      :model="userInfo"
      hide-required-asterisk
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="userInfo.username" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userInfo.email" />
      </el-form-item>
      <el-form-item prop="birthday" label="生日">
        <el-date-picker
          v-model="userInfo.birthday"
          class="birthdayPick"
          type="date"
          placeholder="请选择生日"
          format="YYYY-MM-DD"
          :editable="false"
        />
      </el-form-item>
      <el-form-item label="余额">
        <el-input v-model="userInfo.balance" />
      </el-form-item>
    </el-form>

    <!-- 修改密码的表单 -->
    <el-form
      :model="passInfo"
      ref="passFormRef"
      :rules="rules"
      v-show="!isInfo"
      hide-required-asterisk
    >
      <el-form-item label="原始密码" prop="oldPass">
        <el-input v-model="passInfo.oldPass" type="password" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPass">
        <el-input v-model="passInfo.newPass" type="password" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPass">
        <el-input v-model="passInfo.confirmPass" type="password" />
      </el-form-item>
    </el-form>

    <el-button
      type="primary"
      :style="`float: left; margin-left: 10px; width: 80px;`"
      :loading="loading"
      @click="handleSubmit"
      >提交</el-button
    >
  </div>
</template>

<script>
import { useStore } from "vuex";
import { computed, reactive, ref, onMounted } from "vue";
import { getUserInfo } from "../axios/user";
import { updateUser } from "../axios/user";
import { ElMessage } from "element-plus";
import { formatDate } from '../utils/dateFormater';
export default {
  name: "personal",
  setup() {
    const store = useStore();
    const isInfo = ref(true);
    let baseFormRef = ref();
    let passFormRef = ref();
    let userInfo = reactive({}); // 用户信息
    let tempInfo = {}; // 暂时储存信息，选择不同的表单的时候要把信息复原
    let loading = ref(false);
    let passInfo = reactive({
      oldPass: "",
      newPass: "",
      confirmPass: "",
    });
    let nullPass = {
      oldPass: "",
      newPass: "",
      confirmPass: "",
    };

    let bgColors = computed(() => store.getters.bgColors);
    // 获取用户信息
    const getInfo = async () => {
      try {
        let res = await getUserInfo();
        Object.assign(userInfo, res.data.userInfo);
        Object.assign(tempInfo, res.data.userInfo);
      } catch (e) {
        return;
      }
    };
    onMounted(() => {
      getInfo();
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

    // 验证确认密码
    const checkPass = (rule, value, callback) => {
      if (value != passInfo.newPass) {
        callback(new Error("两次密码输入不一致"));
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
      oldPass: [
        { required: true, message: "密码不能为空", trigger: "blur" },
        { min: 6, max: 15, message: "密码长度为6-15", trigger: "blur" },
      ],
      newPass: [
        { required: true, message: "密码不能为空", trigger: "blur" },
        { min: 6, max: 15, message: "密码长度为6-15", trigger: "blur" },
      ],
      confirmPass: [
        { required: true, message: "密码不能为空", trigger: "blur" },
        { min: 6, max: 15, message: "密码长度为6-15", trigger: "blur" },
        { validator: checkPass, trigger: "blur" }, // 确认密码新增认证规则
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

    // 切换表单
    const handleSelect = () => {
      isInfo.value = !isInfo.value; // 控制显示和隐藏
      Object.assign(userInfo, tempInfo); // 信息重新赋值
      Object.assign(passInfo, nullPass); // 清空密码框
    };

    const handleSubmit = () => {
      if (isInfo.value) {
        changeInfo();
      } else {
        changePass();
      }
    };

    // 修改信息
    const changeInfo = async () => {
      // 表单验证
      await baseFormRef.value.validate(async (valid, fields) => {
        if (valid) {
          try {
            
            userInfo.birthday = formatDate(userInfo.birthday)
            loading.value = true;
            await updateUser(userInfo);
            ElMessage.success("修改个人信息成功");
            getInfo(); // 重新获取信息
            loading.value = false;
          } catch (e) {
            loading.value = false;
            return;
          }
        }
      });
    };

    // 修改密码
    const changePass = async () => {
      // 表单验证
      await passFormRef.value.validate(async (valid, fields) => {
        if (valid) {
          try {
            loading.value = true;
            userInfo.password = passInfo.newPass;
            await updateUser(userInfo);
            ElMessage.success("修改密码成功");
            Object.assign(passInfo, nullPass); // 清空密码框
            getInfo(); // 重新获取信息
            loading.value = false;
          } catch (e) {
            loading.value = false;
            return;
          }
        }
      });
    };

    return {
      bgColors,
      userInfo,
      isInfo,
      rules,
      baseFormRef,
      passInfo,
      handleSelect,
      passFormRef,
      handleSubmit,
      loading,
    };
  },
};
</script>

<style lang="scss">
.personal {
  .el-menu {
    .el-menu-item {
      font-size: 17px;
    }
    .is-active {
      background-color: #f2f2f2 !important;
    }
    background-color: #f2f2f2;
  }
  .el-form {
    padding: 10px 0 0 10px;
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
        width: 350px;
      }
    }
  }
}
</style>