<template>
  <div class="home">
    <h1 :style="{ color: bgColors.bgHeader }">欢迎使用本系统</h1>
  </div>
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";
export default {
  name: "home",
  setup() {
    const store = useStore();
    let bgColors = computed(() => store.getters.bgColors);
    return {
      bgColors,
    };
  },
};
</script>

<style lang = 'scss'>
.home {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

}
</style>