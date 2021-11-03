<template>
  <div>
      <!-- 标签使用原则:
          规则: 先定义,后使用
          语法: el-breadcrumb 找到名称为Breadcrumb组件进行展现
          定义elementUI的组件: 按需导入组件
             从elementUI中导入特定的组件
             1.import {Breadcrumb} from 'element-ui'
             2.Vue.use(Breadcrumb)  声明为全局组件.子组件可以直接调用


      -->
      <!-- 1.添加面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>活动管理</el-breadcrumb-item>
        <el-breadcrumb-item>活动列表</el-breadcrumb-item>
        <el-breadcrumb-item>活动详情</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 2.定义卡片视图  -->
      <el-card class="box-card">
        <div class="text item">
          <!-- 4.栅格: 每行24格,可以动态的缩放 -->
          <el-row :gutter="20">
            <el-col :span="8">
              <!-- 3.定义文本输入框-->
              <el-input placeholder="请输入内容" class="input-with-select">
                 <el-button slot="append" icon="el-icon-search"></el-button>
              </el-input>
            </el-col>
            <el-col :span="3">
                <el-button type="primary">主要按钮</el-button>
            </el-col>
          </el-row>
        </div>
        <hr>
        <!-- 定义表格
          :data 一般采用数组的方式 定义表格数据!!!
          label="ID" 列字段名称
          stripe: 默认为false   启用为true  斑马纹
          border: 默认为false   启用为true  边框线
        -->
        <h1>定义表格</h1>
         <el-table :data="tableData" style="width: 100%"  stripe border>
              <el-table-column
                label="编号" prop="id">
              </el-table-column>
              <el-table-column
                label="名称" prop="name">
              </el-table-column>
              <el-table-column
                label="年龄" prop="age">
              </el-table-column>
              <el-table-column
                label="性别" prop="sex">
              </el-table-column>
         </el-table>

         <!-- 分页工具
            :page-sizes 每页展现的条数信息
            :total="400" 设定总记录数
            layout 展现的数据有哪些
            :page-size  初始化时页面的条数 当作参数传递给后端
            :current-page 当前看到的页数
            @size-change: 当每页的条数发生变化时,触发事件
            @current-change:当前展现的页数发生变化时,触发事件
          -->
         <el-pagination
             @size-change="handleSizeChange"
             @current-change="handleCurrentChange"
             :current-page="100"
             :page-sizes="[10, 20, 30, 40]"
             :page-size="10"
             layout="total, sizes, prev, pager, next, jumper"
             :total="1000">
         </el-pagination>
      </el-card>

      <!--
        :visible.sync 控制对话框是否可见  true/false
      -->
      <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%">
        <span>这是一段信息</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </el-dialog>

      <hr />

      <!-- 文件上传案例
          action: 图片上传的地址,
          file-list: 需要展现的图片列表信息 数组
          on-preview="handlePreview" 点击列表触发函数
          on-remove="handleRemove"   点击删除时,触发函数
          drag   拖拽
          multiple  批量操作
       -->
      <el-upload
        class="upload-demo"
        action="https://jsonplaceholder.typicode.com/posts/"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        list-type="picture"
        drag
        multiple
        >
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>



  </div>
</template>

<script>
    //对外声明组件属性/方法等参数.要被根组件调用
    export default {
      data(){
        return {
          tableData: [
            {id:100, name:"黑熊精", age: 3000, sex:"男"},
            {id:100, name:"黑旋风", age: 3000, sex:"男"},
            {id:100, name:"黑心肠", age: 3000, sex:"男"},
            {id:100, name:"黑桃A", age: 3000, sex:"男"}
          ],
          dialogVisible: true,
          fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]

        }
      },
      methods: {
        handleSizeChange(size){
          alert("当前条数:"+size)
        },
        handleCurrentChange(current){
          alert("当前页数:"+current)
        },
        handleRemove(file, fileList) {
          console.log("点击删除按钮时,触发JS")
          console.log(file, fileList);
        },
        handlePreview(file) {
          console.log("点击url地址之后,触发!!!!!")
          console.log(file);
        }
      }
    }
</script>

<style lang="less" scoped>
</style>
