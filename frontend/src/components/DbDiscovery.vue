<template>
  <div>
    <el-col :span="10" :push="7" v-if="loginFlag" style="">
      <el-card>
        <span>登录账号：</span><el-input v-model="login.account"></el-input>
        <span>登录密码：</span><el-input v-model="login.password"></el-input>
        <el-button @clic>登录</el-button>
      </el-card>
    </el-col>
  </div>
</template>

<script>
  export default {
    data() {
      return {
          loginFlag: true,
          login: {
              account: '',
              password: ''
          }
      }
    },
    components: {
    },
    mounted () {
    },
    methods: {
      save () {
        this.$axios.post("http://127.0.0.1:8000/saveDiscovery", this.form).then((response) => {
          if (response) {
            this.tableData = response.data
            this.$message({
              type: 'success',
              message: '保存成功'
            })
            this.form = {}
            this.flag = false
          }
        })
      },
      close () {
        this.flag = false
        this.form = {}
      },
      edit (obj) {
        this.flag = true
        this.form = JSON.parse(JSON.stringify(obj))
      },
      add () {
        this.flag = true
      }
    }
  }
</script>

<style>

</style>
