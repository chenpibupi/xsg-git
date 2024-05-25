<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="110px">
      <el-form-item label="初稿文章标题" prop="articleTitle">
        <el-input v-model="queryParams.articleTitle" placeholder="请输入初稿文章标题" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="终稿上传时间" prop="finalUploadTime">
        <el-date-picker clearable
          v-model="queryParams.finalUploadTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择终稿上传时间">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['gaojian:finalArticle:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['gaojian:finalArticle:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="finalArticleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="初稿文章标题" align="center" prop="articleTitle" />
      <el-table-column label="终稿文件名称" align="center" prop="fileName" />
      <el-table-column label="终稿上传时间" align="center" prop="uploadTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-upload2" @click="handleUpload(scope.row)"
            v-hasPermi="['gaojian:article:uploadBt']">上传文件</el-button>
          <el-button size="mini" type="primary" icon="el-icon-download" @click="handleDownload(scope.row)"
            v-hasPermi="['gaojian:article:downloadBt']">下载文件</el-button>
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

    <!-- 添加或修改终稿文章信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="ID" prop="id">
          <el-input v-model="form.id" placeholder="请输入ID" />
        </el-form-item>
        <el-form-item label="初稿文章ID" prop="articleId">
          <el-input v-model="form.articleId" placeholder="请输入初稿文章ID" />
        </el-form-item>
        <el-form-item label="终稿文件" prop="finalArticleFile">
          <file-upload v-model="form.finalArticleFile" />
        </el-form-item>
        <!-- <el-form-item label="终稿上传时间" prop="finalUploadTime">
          <el-date-picker clearable v-model="form.finalUploadTime" type="date" value-format="yyyy-MM-dd"
            placeholder="请选择终稿上传时间">
          </el-date-picker>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFinalArticle, getFinalArticle, delFinalArticle, addFinalArticle, updateFinalArticle } from "@/api/gaojian/finalArticle";
import { getToken } from "@/utils/auth";
export default {
  name: "FinalArticle",
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
      // 终稿文章信息表格数据
      finalArticleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        articleTitle: null,
      },
      // 表单参数
      form: {},
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
      uploadDialogVisible: false, // 控制上传文件弹窗显示隐藏
      // 表单校验
      rules: {
        articleId: [
          { required: true, message: "初稿文章ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
      console.log('下载', row);
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
    /** 查询终稿文章信息列表 */
    getList() {
      this.loading = true;
      listFinalArticle(this.queryParams).then(response => {
        this.finalArticleList = response.rows;
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
        articleId: null,
        finalArticleFile: null,
        finalUploadTime: null
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
      this.title = "添加终稿文章信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log('row', row.id);
      this.reset();
      const id = row.id || this.ids
      getFinalArticle(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改终稿文章信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFinalArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFinalArticle(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除终稿文章信息编号为"' + ids + '"的数据项？').then(function () {
        return delFinalArticle(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gaojian/finalArticle/export', {
        ...this.queryParams
      }, `finalArticle_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
