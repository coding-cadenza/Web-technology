<template>
  <div class="overview">
    <el-card class="list-card">
      <template #header>
        <div class="card-header" style="text-align: left">
          <h3 :style="{ color: bgColors.bgHeader }">用户列表</h3>
        </div>
      </template>
      <el-table
        :data="data"
        :row-class-name="tableRowClassName"
        :style="`--el-table-row-hover-bg-color:${bgColors.bgHover}`"
        class="list-table"
        border
      >
        <el-table-column
          type="index"
          label="序号"
          width="80px"
          align="center"
        />
        <el-table-column prop="username" label="姓名" />
        <el-table-column prop="articleNum" label="文章数量" />
        <el-table-column label="操作" width="150px" align="center">
          <template #default="scope">
            <el-button
              size="small"
              @click="handleManage(scope.row.id)"
              type="primary"
              >进入文章管理</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card class="chart-card">
      <template #header>
        <div class="card-header" style="text-align: left">
          <h3 :style="{ color: bgColors.bgHeader }">学生发布文章图表</h3>
        </div>
      </template>
      <div class="myChart" ref="myChart"></div>
    </el-card>
  </div>
</template>

<script>
import {
  toRefs,
  computed,
  onBeforeUpdate,
  ref,
  onMounted,
  nextTick,
  getCurrentInstance,
} from "vue";
import { useStore } from "vuex";
import * as echarts from "echarts";
// Button.vue
import bus from "vue3-eventbus";
export default {
  name: "overview",
  props: {
    data: Array,
  },
  emits: ["changeView"],
  setup(props, { emit }) {
    const instance = getCurrentInstance();
    // 获取当前背景颜色配色
    const store = useStore();

    let bgColors = computed(() => {
      nextTick(() => {
        initEcharts();
      });
      return store.getters.bgColors;
    });
    /**
     * 制作echart图表
     */
    let myChart = ref();
    // 刚开始的时候props.data还是空的，要通过计算属性利用data.value
    // setUp只执行一次，如果直接设置xdata那么不但得不到更新，而且是空的
    // 横坐标数据
    let option = computed(() => {
      let ydata = props.data.map((obj) => {
        return obj.articleNum;
      });
      let xdata = props.data.map((obj) => {
        return obj.username;
      });
      let color = bgColors.value.bgHeader;
      return {
        xAxis: {
          data: xdata,
        },
        yAxis: {},
        series: [
          {
            type: "bar", // 柱状图
            data: ydata,
            itemStyle: {
              normal: {
                color: color,
              },
            },
          },
        ],
        grid: {
          top: "5%",
          left: "5%",
          right: "5%",
          bottom: "10%",
        },
      };
    });
    // 制作柱状图
    let MyChart;
    const initEcharts = () => {
      // 生成柱状图
      MyChart = echarts.init(myChart.value);
      MyChart.setOption(option.value);
      // 屏幕大小调节图表
    };
    // 放出事件，重置表格，因为在菜单栏隐藏的时候，没有这个不行

    onBeforeUpdate(() => {
      initEcharts();
    });

    onMounted(() => {
      window.addEventListener("resize", () => {
        MyChart.resize();
      });
      bus.on("changeChart", () => {
        setTimeout(() => {
          if (myChart.value) {
            let canvas = document.getElementsByTagName("canvas")[0];
            canvas.style.width = myChart.value.offsetWidth + "px";
          }
        }, 300);
      });
    });

    const tableRowClassName = ({ row, rowIndex }) => {
      if (rowIndex % 2 !== 0) {
        return "light-row";
      } else {
        return "";
      }
    };

    // 进入文章管理
    const handleManage = (id) => {
      emit("changeView", id);
    };

    return {
      tableRowClassName,
      bgColors,
      handleManage,
      myChart,
    };
  },
};
</script>

<style lang="scss">
.overview {
  width: 100%;
  overflow: hidden;
  .el-card {
    width: 100%;
    overflow: hidden;
    .el-card__header {
      height: 40px;
      width: 100%;
      line-height: 40px;
      padding: 0;
      padding-left: 20px;
    }
  }
  .chart-card {
    margin-top: 5px;
    width: 100%;
    .myChart {
      width: 100%;
      height: 250px;
      canvas {
        transition: all 0.5s;
      }
    }
  }
  .list-table {
    overflow: hidden;
    th {
      background-color: #f2f2f2;
    }
    .light-row {
      background-color: #f2f2f2;
    }
  }
}
</style>