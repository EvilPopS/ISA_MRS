<template>
  <div id="tabNav">
    <ul class="nav nav-tabs nav-justified">
      <li v-for="tab in tabs" :key="tab" class="nav-item">
        <a
          class="nav-link"
          :class="{ active: tab === selected }"
          @click="setTab(tab)"
          >{{ tab }}</a
        >
      </li>
    </ul>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "TabNav",
  props: {
    /* Holds all of the tabs data */
    tabs: {
      type: Array,
      required: true,
    },
    selected: {
      type: String,
      required: true,
    },
  },
  methods: {
    setTab(tab) {
      this.$emit("selected", tab);
    },
  },
};
</script>

<style scoped>
#tabNav {
  margin: 50px 0px 30px 0px;
}

a {
  color: rgba(15, 95, 72, 0.95);
}

li {
  background-color: rgba(214, 218, 216, 0.7);
}
</style>
