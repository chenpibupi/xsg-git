<template>
  <div class="app-container" v-cloak="none">
    <el-form :model="articleData" ref="articleForm" label-width="100px">
      <el-row>
        <el-col :span="16">
          <el-form-item label="文章标题">
            <el-input v-model="articleData.articleTitle"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="文章类型">
            <el-select v-model="articleData.articleType">
              <el-option v-for="type in articleTyNames" :key="type.name" :label="type.name" :value="type.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item label="用户名">
            <span style="margin-left: 10px;">{{ articleData.authorName }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态">
            <el-tag size="small" v-if="articleData.status === 0" type="info" effect="dark">草稿</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 1" type="primary" effect="dark">待接收</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 2" type="info">审核中</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 3" type="warning">审稿中</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 4" type="success">通过未发布</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 5" type="success">已发布</el-tag>
            <el-tag size="small" v-else-if="articleData.status === 6" type="danger">未通过</el-tag>
            <el-tag size="small" v-else>未知状态</el-tag>
          </el-form-item>
        </el-col>

        <el-col :span="2">
          <el-button type="primary" style="margin-left: 16px;" @click="saveArticle" :disabled="isSavingDisabled"
            v-hasPermi="['gaojian:article:saveBt']">
            保存
          </el-button>
        </el-col>
        <el-col :span="2">
          <!-- 如果当前用户是作者，则显示投递按钮 -->
          <el-button v-if="isSubDisabled" type="success" style="margin-left: 16px;" @click="confirmSubmission">
            投递
          </el-button>
        </el-col>

        <el-col :span="3">
          <el-button @click="drawer = true" type="danger" style="margin-left: 16px;"
            v-hasPermi="['gaojian:article:checkBt']">
            审核
          </el-button>
        </el-col>
      </el-row>

      <!-- 文章封面 -->
      <el-form-item label="封面图片">
        <image-upload v-model="articleData.coverImageUrl" />
      </el-form-item>

      <el-form-item label="描述信息">
        <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 6 }" v-model="articleData.description"></el-input>
      </el-form-item>

      <el-form-item label="文章内容" label-position="top">
        <tiny-editor ref="editor" v-model="articleData.content" :disabled="disabled" :language="language" :skin="skin"
          @onClick="onClick"></tiny-editor>
      </el-form-item>
      <!-- 
      <el-form-item label="文章内容" label-position="top">
        <editor ref="editor" v-model="articleData.content" :disabled="disabled" :language="language" :skin="skin"
          @onClick="onClick"></editor>
      </el-form-item> -->

      <!-- 审批意见 -->
      <div>
        <el-row>
          <el-col :span="24">
            <h3>审批意见</h3>
            <el-table :data="approvalComments" border stripe>
              <el-table-column label="审批人" prop="createName"></el-table-column>
              <el-table-column label="审批人角色" prop="reviewerRole"></el-table-column>
              <el-table-column label="审批时间" prop="updateTime"></el-table-column>
              <el-table-column label="审批意见" prop="reviewContent"></el-table-column>
              <!-- 自定义审批结果的显示 -->
              <el-table-column label="审批结果" prop="isApproved">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.isApproved === 1 ? 'success' : 'danger'">
                    {{ scope.row.isApproved === 1 ? '通过' : '未通过' }}
                  </el-tag>
                </template>
              </el-table-column>

              <!-- 添加修改按钮 -->
              <el-table-column label="操作" v-if="this.$store.state.user.roles[0] !== 'zuozhe'">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.createUser===currentUser.id" size="mini" type="primary"
                    @click="handleUpdate(scope.row)">
                    <!-- {{ scope.row.createUser }} - {{ currentUser.id }} -->
                    修改</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>

    </el-form>

    <el-drawer title="审核" :visible.sync="drawer" :with-header="false">
      <el-form ref="auditForm" :model="auditForm" label-width="100px">
        <el-form-item label="审核状态" required>
          <el-radio-group v-model="auditForm.isApproved">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="0">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input type="textarea" v-model="auditForm.reviewContent" :autosize="{ minRows: 4, maxRows: 6 }"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAudit" :disabled="!isFormValid">提交审核</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <!-- 对话框组件 -->
    <el-dialog :visible.sync="dialogVisible" title="修改审批信息">
      <el-form ref="auditForm" :model="auditForm" label-width="100px">
        <el-form-item label="审核状态" required>
          <el-radio-group v-model="auditForm.isApproved">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="0">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input type="textarea" v-model="auditForm.reviewContent" :autosize="{ minRows: 4, maxRows: 6 }"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateAudit" :disabled="!isFormValid">修改</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  listArticleContent, getArticleContent, delArticleContent,
  addArticleContent, updateArticleContent
} from "@/api/gaojian/articleContent";
import {
  listArticleType
} from "@/api/gaojian/articleType";
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from "@/api/gaojian/article";
import {
  listArticleRecord, getArticleRecord, delArticleRecord, addArticleRecord,
  updateArticleRecord
} from "@/api/gaojian/articleRecord";


import TinyEditor from '@/components/TinyEditor.vue'
// import { Editor } from 'tinymce'
export default {
  components: {
    TinyEditor
  },
  data() {
    return {

      auditForm: {
        isApproved: '', // 审核状态
        reviewContent: '' // 审核意见
      },
      dialogVisible: false,
      drawer: false,
      loading: true,
      disabled: false,
      language: 'zh_CN',
      skin: 'oxide',
      articleData: {},
      articleTyNames: [], // 文章类型选项
      approvalComments: ['意见1', '意见2', '意见3'], // 每个角色的审批意见
      approvalModalVisible: false,
    }
  },
  created() {
    var user = this.$store.state.user;
    console.log('user', user);

    this.getLoad()
  },
  computed: {
    // 检查表单项是否都已填写
    isFormValid() {
      // 检查表单项是否都已填写
      return this.auditForm.isApproved !== '' && this.auditForm.reviewContent !== '';
    },
    // 根据文章状态和用户角色来判断是否禁用保存按钮
    isSavingDisabled() {
      // 获取当前用户的角色数组
      const userRole = this.$store.state.user.roles[0];
      // 根据文章状态和用户角色来判断是否禁用保存按钮
      const isDisabled =
        (this.articleData.status !== 0 && this.articleData.status !== 1 && this.articleData.status !== 6) && userRole === 'zuozhe';
      return isDisabled;
    },
    isSubDisabled() {
      const userRole = this.$store.state.user.roles[0];
      const isDisabled =
        (this.articleData.status == 0 || this.articleData.status == 6) && userRole === 'zuozhe';
      return isDisabled;
    },
    // 计算属性，获取当前用户信息
    currentUser() {
      return this.$store.state.user || {}; // 如果用户信息不存在，则返回空对象
    }
  },
  //对跳转目标的路由进行监听，路由监听可以在vue的watch中实现
  watch: {
    $route() {
      if (this.$route.query.id) {
        // console.log('文章标题：', this.$route.query.articleTitle);
        // this.$route.meta.title = this.$route.query.articleTitle + '_详情'
        this.getLoad()//重新调用http请求实现页面的重新渲染
      }
    }
  },
  methods: {

    // 投递确认
    confirmSubmission() {
      this.$confirm('确定要投递吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.subArticle();
      }).catch(() => {
        // 用户取消操作
      });
    },
    showApprovalModal() {
      this.approvalModalVisible = true;
    },
    // 加载文章类型列表
    getLoad() {
      // 加载文章类型列表
      this.loadArticleTyNames();
      const id = this.$route.query.id;
      console.log('id：', id);
      this.loadApprovalComments(id); // 在组件创建时加载审批意见列表

      // 使用 Promise.all 等待两个异步请求完成
      Promise.all([
        getArticleContent(id),
        getArticle(id)
      ]).then(responses => {
        // responses 是一个数组，包含两个响应对象
        // 第一个响应对象是 getArticleContent 的结果，第二个是 getArticle 的结果

        // 从 responses[1].data 中提取出字段，并赋值给 articleData
        const { id, articleTitle, articleType, authorName, coverImageUrl, description, content, status } = responses[1].data;
        this.articleData = { id, articleTitle, articleType, authorName, coverImageUrl, description, content, status };

        this.articleData.content = responses[0].data.content;
        // console.log('====', responses[0].data);
        this.articleData.description = responses[0].data.description;
        console.log('内容加载完成：', this.articleData);
        // 将文章类型赋值的逻辑放在这里
        this.setArticleTypeName();
        console.log('文章类型赋值完成：', this.articleData.articleTyName);
        // this.$route.query.articleTyName
        // console.log('文章类型：', this.$route.query.articleTyName);
        // console.log('文章内容：', this.articleData.content);
      }).catch(error => {
        console.error('加载文章信息失败');
      });
    },

    // 根据文章类型的 id 查找对应的名称并赋值给 articleData.articleTyName
    setArticleTypeName() {
      const typeId = this.articleData.articleType;
      const typeName = this.articleTyNames.find(type => type.id === typeId);
      if (typeName) {
        this.articleData.articleTyName = typeName.name;
      } else {
        console.error('无法找到对应的文章类型');
      }
    },

    // 调用后端接口或其他方法获取审批意见列表
    loadApprovalComments(id) {
      getArticleRecord(id)
        .then(response => {
          this.approvalComments = response.data; // 将获取到的审批意见列表赋值给 approvalComments
          console.log('加载审批意见列表成功');
          console.log(response.data);
        })
        .catch(error => {
          console.error('加载审批意见失败', error);
        });
    },
    // 加载文章类型列表
    loadArticleTyNames() {
      listArticleType()
        .then(response => {
          this.articleTyNames = response.rows.map(item => ({
            id: item.id,
            name: item.typeName
          }));
          console.log('文章类型列表：', this.articleTyNames);
        })
        .catch(error => {
          console.error('加载文章类型失败', error);
        });
    },

    // 提交审核
    submitAudit() {
      // 在这里编写提交审核的逻辑，可以将审核状态和审核意见发送到后端进行处理
      this.$refs.auditForm.validate(valid => {
        if (valid) {
          // 表单验证通过，执行提交操作
          // 这里可以调用提交审核的方法或触发相应的事件
          // 例如 this.$emit('submit', this.auditForm)
          console.log('提交审核');
          // 获取当前用户的角色数组
          const user = this.$store.state.user;
          this.auditForm.articleId = this.articleData.id;
          console.log('提交审核人', user.name, '_Role:' + user.roles[0]);
          console.log('文章Id', this.auditForm.articleId)
          console.log('审核状态：', this.auditForm.isApproved);
          console.log('审核意见：', this.auditForm.reviewContent);
          console.log('提交数据', this.auditForm)
          // 关闭审核抽屉
          this.drawer = false;
          // 清空审核表单
          this.$refs.auditForm.resetFields();
          addArticleRecord(this.auditForm)
            .then(response => {
              console.log('添加审核记录成功');
              // 刷新页面数据
              this.getLoad()
            }).catch(error => {
              console.error('添加审核记录失败', error);
            });
        } else {
          // 表单验证不通过，可以给出相应的提示信息
          this.$message.error('请填写完整的审核信息');
        }
      })
    },


    //处理点击修改审核按钮的方法
    handleUpdate(row) {
      // 设置对话框为可见状态，显示对话框
      this.dialogVisible = true;

      // 在这里你可以执行跳转到修改页面的逻辑，也可以弹出一个对话框进行修改操作，根据你的实际需求来实现
      // 这里只是一个示例，你需要根据你的具体业务逻辑来进行实现
      console.log('点击了修改按钮，当前行的数据为：', row);
      console.log(row.articleId)
      // 从 row 对象中获取值来设置表单的初始值
      this.auditForm.id = row.id;
      this.auditForm.isApproved = row.isApproved ? 1 : 0;
      this.auditForm.reviewContent = row.reviewContent || '';
    },
    // 修改审核
    updateAudit() {
      // 在这里编写提交审核的逻辑，可以将审核状态和审核意见发送到后端进行处理
      this.$refs.auditForm.validate(valid => {
        if (valid) {
          // 表单验证通过，执行修改操作
          // 这里可以调用提交审核的方法或触发相应的事件
          // 例如 this.$emit('submit', this.auditForm)
          console.log('修改审核');
          console.log('修改审核状态：', this.auditForm.isApproved);
          // 获取当前用户的角色数组
          const user = this.$store.state.user;
          this.auditForm.articleId = this.articleData.id;
          console.log('修改审核人', user.name, '_Role:' + user.roles[0]);
          console.log('修改审核意见：', this.auditForm);
          updateArticleRecord(this.auditForm)
            .then(response => {
              console.log('修改审核记录成功');
              // 刷新页面数据
              this.getLoad()
            }).catch(error => {
              console.error('修改审核记录失败', error);
            });
          this.dialogVisible = false;
        } else {
          // 表单验证不通过，可以给出相应的提示信息
          this.$message.error('请填写完整的审核信息');
        }
      })
    },
    onClick(e, editor) {
      console.log(editor)
    },
    // 保存
    saveArticle() {
      // 更新文章
      console.log('文章保存');
      // console.log('文章数据：', this.articleData);
      updateArticle(this.articleData)
        .then(response => {
          console.log('文章保存成功');
          // 更新文章内容
          const updatedData = {
            articleId: this.articleData.id,
            content: this.articleData.content,
            description: this.articleData.description,
            // 其他需要更新的字段...
          };
          return updateArticleContent(updatedData);
        })
        .then(response => {
          console.log('文章内容保存成功');
          this.$message({
            message: '保存成功',
            type: 'success'
          });
        })
        .catch(error => {
          console.error('保存失败', error);
          this.$message.error('保存失败');
        });
    },
    // 投递
    subArticle() {
      // this.saveArticle();
      console.log('投递');
      this.articleData.status = 1;
      updateArticle(this.articleData)
        .then(response => {
          console.log('投递文章成功');
          this.$message({
            message: '投递成功',
            type: 'success'
          });
        }).catch(error => {
          console.error('投递文章失败', error);
          this.$message.error('投递失败');
        });
    }
  }
}
</script>
