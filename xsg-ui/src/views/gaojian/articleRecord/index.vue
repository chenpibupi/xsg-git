<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章编号" prop="articleNumber">
        <el-input v-model="queryParams.articleNumber" placeholder="请输入文章编号" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="审核人姓名" prop="createName">
        <el-input v-model="queryParams.createName" placeholder="请输入审核人姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="审核时间" prop="reviewTime">
        <el-date-picker clearable
          v-model="queryParams.reviewTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择审核时间">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['gaojian:articleRecord:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['gaojian:articleRecord:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['gaojian:articleRecord:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['gaojian:articleRecord:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="文章ID" align="center" prop="articleId" /> -->
      <!-- <el-table-column label="文章标题" align="center" prop="articleTitle" /> -->
      <el-table-column label="文章编号" align="center" prop="articleNumber" />
      <el-table-column label="审核人姓名" align="center" prop="createName" />
      <el-table-column label="审核内容" align="center" prop="reviewContent" show-overflow-tooltip />
      <el-table-column label="审核时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
       v-if="this.$store.state.user.roles[0] !== 'zuozhe'" 
       label="操作" align="center"
        class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['gaojian:articleRecord:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['gaojian:articleRecord:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改审核记录信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="文章ID" prop="articleId">
          <el-input v-model="form.articleId" placeholder="请输入文章ID" />
        </el-form-item> -->

        <el-form-item label="文章编号" prop="articleNumber">
          <el-input v-model="form.articleNumber" placeholder="请输入文章编号" />
        </el-form-item>

        <el-form-item label="审核人" prop="createName">
          <el-input v-model="form.createName" placeholder="请输入审核人姓名" />
        </el-form-item>
        <el-form-item label="审核内容">
          <el-input type="textarea" v-model="form.reviewContent" :autosize="{ minRows: 4, maxRows: 6 }"></el-input>
        </el-form-item>
        <!-- <el-form-item label="审核时间" prop="reviewTime">
          <el-date-picker clearable
            v-model="form.reviewTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择审核时间">
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
import { listArticleRecord, getArticleRecord, delArticleRecord, addArticleRecord, updateArticleRecord } from "@/api/gaojian/articleRecord";

export default {
  name: "ArticleRecord",
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
      // 审核记录信息表格数据
      articleRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        articleId: [
          { required: true, message: "文章ID不能为空", trigger: "blur" }
        ],
        reviewerName: [
          { required: true, message: "审核人姓名不能为空", trigger: "blur" }
        ],
        reviewTime: [
          { required: true, message: "审核时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询审核记录信息列表 */
    getList() {
      this.loading = true;
      listArticleRecord(this.queryParams).then(response => {
        this.articleRecordList = response.rows;
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
        reviewerName: null,
        reviewContent: null
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
      this.title = "添加审核记录信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.form = row;
      this.open = true;
      this.title = "修改审核记录信息";

      // getArticleRecord(id).then(response => {
      //   this.form = response.data;
      //   this.open = true;
      //   this.title = "修改审核记录信息";
      // });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArticleRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
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
      this.$modal.confirm('是否确认删除审核记录信息编号为"' + ids + '"的数据项？').then(function () {
        return delArticleRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/articleRecord/export', {
        ...this.queryParams
      }, `articleRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
