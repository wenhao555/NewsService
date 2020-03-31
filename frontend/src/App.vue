<template>
  <div>
    <el-col :span="10" :push="7" v-if="loginFlag" style="margin-top: 100px">
      <el-card>
        <span>登录账号：</span><el-input v-model="login.account"></el-input>
        <span>登录密码：</span><el-input v-model="login.password"></el-input>
        <el-button @click="loginMethod()" style="float: right; background-color: #4db3ff; color: white; margin-top: 10px;margin-bottom: 10px;">登录</el-button>
      </el-card>
    </el-col>
    <el-col :span="20" :push="2" v-if="!loginFlag">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="用户管理" name="first">
          <el-button @click="addUser()" style="float: right; margin-bottom: 10px;margin-top: 10px;">添加用户</el-button>
          <el-table :data="userData">
            <el-table-column prop="account" label="账户" min-width="50"></el-table-column>
            <el-table-column prop="password" label="密码" min-width="50"></el-table-column>
            <el-table-column prop="name" label="姓名" min-width="50"></el-table-column>
            <el-table-column prop="sex" label="性别" min-width="50"></el-table-column>
            <el-table-column prop="birth" label="生日" min-width="60"></el-table-column>
            <el-table-column prop="image" label="头像" min-width="50">
              <template slot-scope="scope">
                <img :src="scope.row.image" style="width: 150px;">
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="80">
              <template slot-scope="scope">
                <el-button @click="editUser(scope.row)">编辑</el-button>
                <el-button @click="removeUser(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="新闻管理" name="second">
          <el-button @click="addNews()" style="float: right; margin-bottom: 10px;margin-top: 10px;">添加新闻</el-button>
          <el-table :data="newsData">
            <el-table-column prop="id" label="ID" min-width="50"></el-table-column>
            <el-table-column prop="title" label="标题" min-width="50"></el-table-column>
            <el-table-column prop="type" label="类别" min-width="50"></el-table-column>
            <el-table-column prop="content" label="内容" min-width="50"></el-table-column>
            <el-table-column prop="date" label="时间" min-width="60"></el-table-column>
            <el-table-column prop="image" label="图片" min-width="50">
              <template slot-scope="scope">
                <img :src="scope.row.image" style="width: 150px;">
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="90">
              <template slot-scope="scope">
                <el-button @click="editNews(scope.row)">编辑</el-button>
                <el-button @click="removeNews(scope.row)">删除</el-button>
                <el-button @click="addRecommond(scope.row)">推荐</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="推荐新闻管理" name="third">
          <el-table :data="recommendData">
            <el-table-column prop="id" label="ID" min-width="50"></el-table-column>
            <el-table-column prop="title" label="标题" min-width="50"></el-table-column>
            <el-table-column prop="type" label="类别" min-width="50"></el-table-column>
            <el-table-column prop="content" label="内容" min-width="50"></el-table-column>
            <el-table-column prop="date" label="时间" min-width="60"></el-table-column>
            <el-table-column prop="image" label="图片" min-width="50">
              <template slot-scope="scope">
                <img :src="scope.row.image" style="width: 150px;">
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="50">
              <template slot-scope="scope">
                <el-button @click="removeRecommend(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-col>
    <el-dialog title="用户信息" :visible.sync="userDetailFlag" v-if="userDetailFlag" :show-close="true" width="60%">
      <el-form :model="userModel" label-width="140px">
        <el-form-item label="账户" prop="account" :rules="[{required: true, trigger: 'change', message: '必填'}]">
          <el-col :span="10" :push="1">
            <el-input v-model="userModel.account"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password" :rules="[{required: true, trigger: 'change', message: '必填'}]">
          <el-col :span="10" :push="1">
            <el-input v-model="userModel.password"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="名字" prop="name" :rules="[{required: true, trigger: 'change', message: '必填'}]">
          <el-col :span="10" :push="1">
            <el-input v-model="userModel.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" prop="sex" :rules="[{required: true, trigger: 'change', message: '必填'}]">
          <el-col :span="10" :push="1">
            <el-radio-group v-model="userModel.sex">
              <el-radio label="男" value="男"></el-radio>
              <el-radio label="女" value="女"></el-radio>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-col :span="10" :push="1">
            <el-date-picker
              v-model="newsModel.birth"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="头像" prop="image">
          <el-col :span="10" :push="1">
            <el-upload
            class="avatar-uploader"
            action="no"
            :show-file-list="false"
            :on-change="changeImg"
            :on-success="handleAvatarSuccess"
            :auto-upload="false"
            :limit="1">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          </el-col>
        </el-form-item>
        <el-button @click="saveUser()">保存</el-button>
        <el-button @click="cancelUser()">返回</el-button>
      </el-form>
    </el-dialog>
    <el-dialog title="新闻信息" :visible.sync="newsDetailFlag" v-if="newsDetailFlag" :show-close="true" width="60%">
      <el-form :model="newsModel" label-width="140px">
        <el-form-item label="ID" prop="id" v-if="newsModel.id !== 0">
          {{newsModel.id}}
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-col :span="14" :push="1">
            <el-input v-model="newsModel.title"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-col :span="14" :push="1">
            <el-select v-model="newsModel.type">
            <el-option label="本地" value="本地"></el-option>
            <el-option label="国际" value="国际"></el-option>
            <el-option label="军事" value="军事"></el-option>
            <el-option label="科技" value="科技"></el-option>
            <el-option label="生活" value="生活"></el-option>
          </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-col :span="14" :push="1">
            <el-input type="textarea" :rows="5" v-model="newsModel.content"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="时间" prop="date">
          <el-col :span="14" :push="1">
            <el-date-picker
              v-model="newsModel.date"
              type="datetime"
              placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="新闻图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="no"
            :show-file-list="false"
            :on-change="changenewsImg"
            :on-success="handleAvatarSuccess"
            :auto-upload="false"
            :limit="1">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-button @click="saveNews()">保存</el-button>
        <el-button @click="cancelNews()">返回</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
    import moment from 'moment'
    export default {
        data() {
            return {
                updateFlag: false,
                newsDetailFlag: false,
                imageUrl: null,
                userModel: {
                    account: '',
                    password: '',
                    name: '',
                    sex: '男',
                    image: '',
                    birth: ''
                },
                newsModel: {
                    id: 0,
                    title: '',
                    type: '',
                    content: '',
                    date: '',
                    image: ''
                },
                loginFlag: true,
                login: {
                    account: '',
                    password: '',
                    admin: true
                },
                activeName: 'first',
                userData: [],
                newsData: [],
                recommendData: [],
                userDetailFlag: false
            }
        },
        components: {},
        mounted() {
        },
        watch: {
            loginFlag: function () {
                if (!this.loginFlag) {
                   this.getUserList()
                }
            }
        },
        methods: {
            addRecommond (row) {
                this.$axios.post("http://127.0.0.1:8000/setNewsToRecommend", row).then(response => {
                    if (response.data === '已存在') {
                        this.$message({
                            type: 'error',
                            message: '已存在'
                        })
                    } else {
                        this.$message({
                            type: 'success',
                            message: '添加推荐成功'
                        })
                    }
                })
            },
            addNews () {
                this.newsDetailFlag = true
                this. newsModel = {
                    id: 0,
                        title: '',
                        type: '',
                        content: '',
                        date: '',
                        image: ''
                }
            },
            removeRecommend (row) {
                this.$axios.post("http://127.0.0.1:8000/removeRecommend", row).then(response =>{
                    this.$message({
                        type: 'success',
                        message: '删除成功'
                    })
                    this.getRecommendList()
                })
            },
            saveNews () {
                this.newsModel.date = new moment(this.newsModel.date).format('YYYY-MM-DD HH:mm:ss')
                let url = ''
                if (this.updateFlag) {
                    url = 'http://127.0.0.1:8000/modifyNews'
                } else {
                    url = 'http://127.0.0.1:8000/setNews'
                }
                this.$axios.post(url, this.newsModel).then(response =>{
                    this.getNewsList()
                    this.$message({
                        type: 'success',
                        message: '保存成功'
                    })
                    this.newsDetailFlag = false
                    this.updateFlag = false
                })
            },
            cancelNews () {
                this.newsDetailFlag = false
                this.imageUrl = ''
                this.updateFlag = false
            },
            handleAvatarSuccess () {
            },
            editNews (row) {
                this.newsModel = JSON.parse(JSON.stringify(row))
                this.newsDetailFlag = true
                this.updateFlag = true
            },
            removeNews (row) {
                this.$axios.post("http://127.0.0.1:8000/removeNews", row).then(response =>{
                   this.getNewsList()
                    this.$message({
                        type: 'success',
                        message: '删除成功'
                    })
                })
            },
            getRecommendList () {
                this.$axios.post("http://127.0.0.1:8000/getNewsByRecommend", null).then(response =>{
                    this.recommendData = response.data
                })
            },
            getUserList () {
                this.$axios.post("http://127.0.0.1:8000/getAllUserInfo", null).then(response =>{
                    this.userData = response.data
                })
            },
            getNewsList () {
                this.$axios.post("http://127.0.0.1:8000/getNews", null).then(response =>{
                    this.newsData = response.data
                })
            },
            saveUser () {
                if (this.userModel.account === '') {
                    this.$message({
                        type: 'error',
                        message: '请输入账户'
                    })
                    return
                }
                if (this.userModel.name === '') {
                    this.$message({
                        type: 'error',
                        message: '请输入名字'
                    })
                    return
                }
                if (this.userModel.password === '') {
                    this.$message({
                        type: 'error',
                        message: '请输入密码'
                    })
                    return
                }

                this.userModel.birth = new moment(this.userModel.birth).format('YYYY-MM-DD HH:mm:ss')
                this.$axios.post("http://127.0.0.1:8000/setUserInfo", this.userModel).then(response => {
                    this.$message({
                        type: 'success',
                        message: '保存成功'
                    })
                    this.imageUrl = ''
                    this.userDetailFlag = false
                    this.getUserList()
                })
            },
            cancelUser () {
                this.imageUrl = ''
                this.userDetailFlag = false
            },
            changeImg (file, fileList) {
                this.getBase64(file.raw).then(res => {
                    this.imageUrl = res
                    this.userModel.image = res
                })
            },
            changenewsImg (file, fileList) {
                this.getBase64(file.raw).then(res => {
                    this.imageUrl = res
                    this.newsModel.image = res
                })
            },
            getBase64(file) {
                return new Promise(function (resolve, reject) {
                    let reader = new FileReader();
                    let imgResult = "";
                    reader.readAsDataURL(file);
                    reader.onload = function () {
                        imgResult = reader.result;
                    };
                    reader.onerror = function (error) {
                        reject(error);
                    };
                    reader.onloadend = function () {
                        resolve(imgResult);
                    };
                });
            },
            editUser (row) {
              this.userDetailFlag = true
              this.userModel = JSON.parse(JSON.stringify(row))
                if (this.userModel.sex === '') {
                    this.userModel.sex = '男'
                }
            },
            removeUser (row) {
                this.$axios.post("http://127.0.0.1:8000/removeUser", row).then(response => {
                  if (response) {
                      this.$message({
                          type: 'success',
                          message: '删除成功'
                      })
                      this.getUserList()
                  }
                })
            },
            loginMethod () {
                if (this.login.account === '') {
                    this.$message({
                        type: 'error',
                        message: '请输入账号'
                    })
                    return
                }
                if (this.login.password === '') {
                    this.$message({
                        type: 'error',
                        message: '请输入密码'
                    })
                    return
                }
                this.$axios.post("http://127.0.0.1:8000/adminLogin", this.login).then(response => {
                    if (response.data === '登陆成功') {
                        this.$message({
                            type: 'success',
                            message: '登录成功'
                        })
                        this.loginFlag = false
                        this.login.account = ''
                        this.login.password = ''
                    } else {
                        this.$message({
                            type: 'error',
                            message: '登录失败请确认'
                        })
                    }
                })
            },
            handleClick (tab, event) {
                if (this.activeName === 'first') {
                    this.getUserList()
                } else if (this.activeName === 'second') {
                    this.getNewsList()
                } else {
                    this.getRecommendList()
                }
            }
        }
    }
</script>

<style>

</style>
