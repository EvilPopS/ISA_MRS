<template>
    <div class="popup-overlay" @click="$emit('close')">
        <div class="popup" @click.stop>
          <div id="contentHolder">
            <slot/>
          </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'PopUp'
    };
</script>

<style scoped>
  #contentHolder{
    border: solid 3px rgb(23, 88, 3);
    border-radius: 50px;
    height: 100%;
    margin: 0 10px;
  }

  .popup-overlay {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    background-color: #000000da;
    z-index: 999;
  }

  .popup {
    background: linear-gradient(rgb(208, 236, 182), rgb(86, 240, 132));
    text-align: center;
    height: 500px;
    width: 500px;
    margin-top: 10%;
    padding: 60px 0;
    border-radius: 20px;
  }
  .close {
    margin: 10% 0 0 16px;
    cursor: pointer;
  }

</style>