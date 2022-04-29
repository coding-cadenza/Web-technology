<template>
  <div class="userEditor">
    <el-card class="userCard">
      <template #header>
        <div class="card-header">
          <span>信息</span>
          <el-button class="button" type="text" @click="handleHideEdit"
            >取消</el-button
          >
        </div>
      </template>
      <el-form :model="newData" :rules="rules" ref="ruleFormRef"
      hide-required-asterisk>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="newData.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="newData.password" type="password" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="newData.email" />
        </el-form-item>
        <el-form-item prop="birthday" label="生日">
          <el-date-picker
            v-model="newData.birthday"
            class="birthdayPick"
            type="date"
            placeholder="请选择生日"
            format="YYYY-MM-DD"
            :editable="false"
          />
        </el-form-item>
        <el-form-item label="余额">
          <el-input v-model="newData.balance" />
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
</template>

<script>
import { reactive, ref,computed } from "vue";
import { formatDate } from "../utils/dateFormater";
import { ElMessage } from "element-plus";
export default {
  name: "userEditor",
  props: {
    userData: Object,
  },
  emits: ["hideEditor", "handleUpdate"],
  setup(props, { emit }) {
    let newData = computed(()=>{
      return props.userData
    })
    const ruleFormRef = ref();
    const loading = ref(false);

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
    // 更新信息
    const handleSubmit = async (ruleF) => {
      await ruleF.validate(async (valid, fields) => {
        if (valid) {
          try {
            newData.value.birthday=formatDate(newData.value.birthday)
            loading.value = true;
            await emit("handleUpdate",newData.value);
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

    // 隐藏编辑页
    const handleHideEdit = () => {
      emit("hideEditor");
    };

    return {
      newData,
      rules,
      ruleFormRef,
      handleSubmit,
      handleHideEdit,
    };
  },
};
</script>

<style lang="scss">
.userEditor {
  width: 100%;
  height: 100%;
  position: fixed;
  left: 0;
  top: 0;
  background-color: rgba($color: #000000, $alpha: 0.3);
  z-index: 999;
  .userCard {
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
</style>