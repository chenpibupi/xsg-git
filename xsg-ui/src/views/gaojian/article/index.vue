<template>

  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章编号" prop="articleNumber">
        <el-input v-model="queryParams.articleNumber" placeholder="请输入文章编号" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="文章标题" prop="articleTitle">
        <el-input v-model="queryParams.articleTitle" placeholder="请输入文章标题" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="作者姓名" prop="authorName">
        <el-input v-model="queryParams.authorName" placeholder="请输入作者姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="文章状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择文章状态">
          <el-option v-for="dict in dict.type.article_status" :key="dict.value" :label="dict.label"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="文章类型" prop="articleType">
        <el-select v-model="queryParams.articleType" placeholder="请选择文章类型">
          <el-option v-for="type in articleTyNames" :key="type.name" :label="type.name" :value="type.id" />
        </el-select>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['gaojian:article:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['gaojian:article:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['gaojian:article:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>


    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文章编号" align="center" prop="articleNumber" />
      <el-table-column label="文章标题" align="center" prop="articleTitle" width="220">
        <template slot-scope="scope">
          <router-link :to="{
            path: '/gaojian/edit/index/',
            query: {
              id: scope.row.id,
              metaTitle: scope.row.articleTitle + '_详情'
            }
          }" class="link-type">
            <span>{{ scope.row.articleTitle }}</span>
          </router-link>

        </template>
      </el-table-column>
      <!-- <el-table-column label="文章类型" align="center" prop="articleType" /> -->
      <el-table-column label="文章类型" align="center" prop="articleTyName" width="80" />
      <el-table-column label="封面图片" align="center" prop="coverImageUrl">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImageUrl" :width="50" :height="50" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="更新时间" align="center" prop="updateTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="终稿上传时间" align="center" prop="uploadTime" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.uploadTime">{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d} {h}:{m}:{s}')
            }}</span>
          <span v-else>未上传</span>
        </template>
      </el-table-column>

      <el-table-column label="作者姓名" align="center" prop="authorName" width="120" />
      <el-table-column label="文章状态" align="center" prop="status" width="120">
        <!--文章状态: 0-草稿, 1-待接收,2-待审，3-审稿中, 4-通过未发布, 5-已发布, 6-未通过-->
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info" effect="dark">草稿</el-tag>
          <el-tag v-else-if="scope.row.status === 1" effect="dark">待接收</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">审核中</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="warning">审稿中</el-tag>
          <el-tag v-else-if="scope.row.status === 4" type="success">通过未发布</el-tag>
          <el-tag v-else-if="scope.row.status === 5" type="success">已发布</el-tag>
          <el-tag v-else-if="scope.row.status === 6" type="danger">未通过</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['gaojian:article:edit']">修改</el-button>

          <el-dropdown v-if="scope.row.status === 2 || scope.row.status === 3"
            v-hasPermi="['gaojian:article:distribution']" @command="handleCommand" trigger="click">
            <span class="el-dropdown-link">
              <el-button type="text" class="el-dropdown-link-button">
                分配专家<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="expert in experts" :key="expert.value"
                :command="{ expertId: expert.value, row: scope.row }">
                {{ expert.label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>



          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['gaojian:article:remove']">删除</el-button>
        </template>

      </el-table-column>



      <el-table-column label="终稿" align="center" width="220">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 4" size="mini" icon="el-icon-upload" @click="handleUpload(scope.row)"
            v-hasPermi="['gaojian:article:uploadBt']">上传
          </el-button>

          <el-button size="mini" icon="el-icon-download" @click="handleDownload(scope.row)"
            v-hasPermi="['gaojian:article:downloadBt']">下载</el-button>

        </template>
      </el-table-column>
    </el-table>


    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


    <!-- 上传 终稿 文件的小弹窗 -->
    <el-dialog title="上传终稿" :visible.sync="uploadDialogVisible" width="30%" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <el-form ref="uploadForm" :model="uploadForm" label-width="100px">
        <!-- <el-form-item label="选择文件">
          <file-upload v-model="form.noticeContent" />
        </el-form-item> -->
        <el-form-item label="上传稿件文件">
          <el-upload ref="upload" :limit="1" accept=".docx, .doc, .pdf" :action="upload.url" :headers="upload.headers"
            :file-list="upload.fileList" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
            :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>

            <el-button style="margin-left: 10px;" size="small" type="success" :loading="upload.isUploading"
              @click="submitUpload">上传到服务器</el-button>
            <div slot="tip" class="el-upload__tip">只能上传doc/docx/pdf文件，且不超过50MB</div>
          </el-upload>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="uploadDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 添加或修改文章信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="文章编号" prop="id">
          <el-input v-model="form.id" placeholder="请输入文章编号" />
        </el-form-item> -->
        <el-form-item label="文章标题" prop="articleTitle">
          <el-input v-model="form.articleTitle" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="封面图片路径" prop="coverImageUrl">
          <image-upload v-model="form.coverImageUrl" />
        </el-form-item>

        <el-form-item label="文章类型" prop="articleType">
          <el-select v-model="form.articleType" placeholder="请选择文章类型">
            <el-option v-for="type in articleTyNames" :key="type.name" :label="type.name" :value="type.id" />
          </el-select>
        </el-form-item>

        <el-form-item v-if="this.$store.state.user.roles[0] == 'admin'
          && (this.form.status >= 2)" label="文章状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择文章状态">
            <el-option label="审核中" value="2"></el-option>
            <el-option label="审稿中" value="3"></el-option>
            <el-option label="通过未发布" value="4"></el-option>
            <el-option label="发布" value="5"></el-option>
            <el-option label="不通过" value="6"></el-option>
          </el-select>
          <br>
          <span>文章状态: 2-审核中, 3-审稿中, 4-通过未发布, 5-发布, 6-不通过</span>
        </el-form-item>

        <!-- 控制显示/隐藏描述信息框 -->
        <el-form-item v-if="showDescriptionForm" label="描述信息" prop="description">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 6 }" v-model="form.description"></el-input>
        </el-form-item>
        <br>
        <br>
        <hr>
        <br>
        <!-- 文字提示信息 -->
        <el-descriptions title="提示信息" size="small" class="article-tips">
          <el-descriptions-item label="更多操作" :label-style="{ fontSize: '16px', color: '#333' }"
            :content-style="{ fontSize: '16px', color: '#009688' }">
            点击文章标题可以进行内容填写等更多操作。
          </el-descriptions-item>
        </el-descriptions>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<style>
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>

<script>
import { getToken } from "@/utils/auth";
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from "@/api/gaojian/article";
import { addFinalArticle, updateFinalArticle } from "@/api/gaojian/finalArticle";
import {
  listArticleType
} from "@/api/gaojian/articleType";
import { listUser } from "@/api/system/user";
export default {
  name: "Article",
  dicts: ['article_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文章信息表格数据
      articleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示描述信息框，默认显示
      showDescriptionForm: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        articleNumber: null,
        articleTitle: null,
        articleType: null,
        // coverImageUrl: null,
        createTime: null,
        updateTime: null,
        finalUploadTime: null,
        // authorAccount: null,
        authorName: null,
        status: null
      },
      uploadDialogVisible: false, // 控制上传文件弹窗显示隐藏
      articleTyNames: [],
      experts: [],
      // 上传参数
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/file/upload",
        // 上传的文件列表
        fileList: []
      },
      // 上传表单
      uploadForm: {},
      // 上传表单
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        articleTitle: [
          { required: true, message: "文章标题不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.loadArticleTyNames();
    this.loadExperts();
  },
  computed: {
    isSavingDisabled() {
      // 获取当前用户的角色数组
      const userRoles = this.$store.state.user.roles;

      // 根据文章状态和用户角色来判断是否禁用上传终稿按钮
      const isDisabled =
        !(this.articleData.status === 1 || this.articleData.status === 5) || !userRoles.includes('zuozhe');

      return isDisabled;
    },
  },
  methods: {

    // 处理下拉菜单命令
    handleCommand(command) {
      const { expertId, row } = command;
      // console.log('专家ID:', expertId);
      // console.log('行数据:', row);
      // console.log('文章id：', row.id)

      this.$confirm('确认分配给该专家吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 在 then 方法中使用存储的变量
        console.log('确认分配给专家:', expertId);
        console.log('文章id:', row.id);
        updateArticle({ id: row.id, expertId: expertId }).then(response => {
          this.$message({
            type: 'success',
            message: '分配成功!'
          });
        }).catch(error => {
          this.$message({
            type: 'danger',
            message: '分配失败!'
          });
          console.log(error);
        });
      }).catch(() => {
        console.log('取消分配');
      });
    },
    // 加载专家列表
    loadExperts() {
      const userRole = this.$store.state.user.roles[0];
      if (userRole !== 'admin' && userRole !== 'bianji') {
        return
      }
      listUser({ deptId: 204 }).then(response => {
        this.experts = response.rows.map(item => {
          return {
            label: item.nickName,
            value: item.userId
          }
        })
        console.log('sakjsass', response)
        console.log('sakj', this.experts)
      })
    },

    /* 处理上传操作-终稿 */
    // 上传-更新操作——终稿 
    handleUpload(row) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      setTimeout(() => {
        loading.close();
      }, 1000);

      // 清空上传文件列表
      this.upload.fileList = [];
      // 如果上传时间存在，表示执行更新操作
      console.log('上传：', row);
      if (row.uploadTime) {
        // 更新操作，将文件信息赋值给上传文件列表
        this.upload.fileList = [{ name: row.fileName, url: row.filePath }];
      }
      // 使用 setTimeout 延迟执行，以添加过渡效果
      setTimeout(() => {
        // 打开上传文件弹窗
        this.uploadDialogVisible = true;
      }, 1000); // 设置延迟时间为1000毫秒
      // 打印行数据，用于调试
      this.uploadForm.articleId = row.id
      this.uploadForm.articleTitle = row.articleTitle
      console.log(row);
    },
    // 文件提交处理
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
      // // 打印上传进度信息
      // console.log('上传进度信息:', event);
      // console.log('上传进度的文件信息:', file);

    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {

      console.log('上传成功的文件信息:', file);
      console.log('上传成功的响应数据:', response);
      this.upload.isUploading = false;
      this.uploadForm.filePath = response.data.url;
      this.uploadForm.fileName = file.name;
      console.log('文章id', this.uploadForm.articleId)
      console.log('文章标题', this.uploadForm.articleTitle)
      console.log(response.data);
      console.log(response);
      console.log(this.uploadForm)

      addFinalArticle(this.uploadForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg);
          this.uploadDialogVisible = false;
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      });


      this.$modal.msgSuccess(response.msg);
    },

    // 处理下载操作
    handleDownload(row) {
      // 判断文件路径是否存在
      if (!row.filePath) {
        // 如果文件路径不存在，则给出错误提示
        this.$message.error('文件不存在，无法下载，请先上传文件');
        return;
      }

      // 如果文件路径存在，则执行下载操作
      var name = row.fileName;
      var url = row.filePath;
      var suffix = url.substring(url.lastIndexOf("."), url.length);
      const a = document.createElement('a')
      a.setAttribute('download', name + suffix)
      a.setAttribute('target', '_blank')
      a.setAttribute('href', url)
      a.click()
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
    /** 查询文章信息列表 */
    getList() {
      this.loading = true;
      listArticle(this.queryParams).then(response => {
        this.articleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        articleNumber: null,
        articleTitle: null,
        articleType: null,
        articleTyName: null,
        coverImageUrl: null,
        createTime: null,
        updateTime: null,
        finalUploadTime: null,
        // authorAccount: null,
        authorName: null,
        phoneNumber: null,
        status: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文章信息";
      // 根据需要控制是否显示描述信息框
      this.showDescriptionForm = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row.id)
      this.reset();
      const id = row.id || this.ids
      getArticle(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文章信息";
        // 根据需要控制是否显示描述信息框
        this.showDescriptionForm = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();

            });
          } else {
            addArticle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除文章信息编号为"' + ids + '"的数据项？').then(function () {
        return delArticle(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gaojian/article/export', {
        ...this.queryParams
      }, `article_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
