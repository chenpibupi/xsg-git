<template>
  <div class="app-container">
    <!-- 导航栏 -->
    <nav>
      <ul>
        <li><a href="#" @click.prevent="toggleSubmissionProcess">投稿流程</a></li>
        <li><a href="#" @click.prevent="toggleHelp">帮助信息</a></li>
        <li><router-link :to="{ path: 'system/notice' }">通知公告</router-link></li>
        <li><router-link :to="{ path: 'gaojian/article' }">我的投稿</router-link></li>
        <li><a href="#" @click.prevent="showExampleArticle">格式要求</a></li>

      </ul>
    </nav>

    <!-- 投稿流程信息区域 -->
    <div class="display-area" v-show="showSubmissionProcess">
      <h2>投稿流程</h2>
      <ol>
        <li>填写投稿表格：登录后，点击“投稿”按钮，填写相应的投稿表格，包括标题、内容等信息。</li>
        <li>上传稿件：根据表格要求上传你的稿件，支持的文件格式包括doc、docx和pdf。</li>
        <li>确认信息并提交：仔细检查填写的信息和上传的稿件，确认无误后点击“提交”按钮。</li>
      </ol>
      <button @click="hideSubmissionProcess">关闭</button>
    </div>

    <!-- 帮助信息区域 -->
    <div class="display-area" v-show="showHelp">
      <h2>帮助信息</h2>
      <ol>
        <p>如果您在使用过程中遇到任何问题或者有任何建议，欢迎联系我们的客服团队。</p>
        <p>客服邮箱：<a href="mailto:service@example.com">service@example.com</a></p>
        <p>客服电话：123-456-789</p>
      </ol>

      <button @click="hideHelp">关闭</button>
    </div>


    <!-- 文章样例信息区域 -->
    <div class="display-area" v-show="showExample">
      <h2>以下是投递稿件的要求：</h2>
      <ol>
        <li>文章标题必须明确、简洁。</li>
        <li>文章内容必须原创，不得侵犯他人版权。</li>
        <li>文章格式要求：支持的文件格式包括doc、docx和pdf。</li>
        <li>文章字数要求：500-2000字。</li>
        <li>文章内容清晰、结构完整。</li>
      </ol>

      <h3>文章格式要求：</h3>
      <p>请确保文章使用以下格式：</p>
      <ul>
        <li>标题：使用Arial字体，字号18px，粗体。</li>
        <li>一级标题：使用Arial字体，字号16px，粗体。</li>
        <li>二级标题：使用Arial字体，字号14px，粗体。</li>
        <li>正文：使用Times New Roman字体，字号12px。</li>
        <li>段落间距：1.5倍行距。</li>
        <li>图片：居中显示，宽度不超过页面宽度的80%。</li>
        <li>表格：简洁明了，使用表格工具创建。</li>
      </ul>

      <button @click="hideHelp">关闭</button>
    </div>


    <br>
    <div class="carousel">
      <!-- 轮播图 -->
      <el-carousel indicator-position="outside" type="card" style="height: 400px; width: 80%; margin: 0 auto;">
        <el-carousel-item style="position: absolute; top: 0; left: 0; right: 0; bottom: 0;">
          <img src="../assets/images/banners/banner1.png" style="height: 100%; width: 100%;" alt="">
        </el-carousel-item>
        <el-carousel-item style="position: absolute; top: 0; left: 0; right: 0; bottom: 0;">
          <img src="../assets/images/banners/banner2.png" style="height: 100%; width: 100%;" alt="">
        </el-carousel-item>
        <el-carousel-item style="position: absolute; top: 0; left: 0; right: 0; bottom: 0;">
          <img src="../assets/images/banners/banner3.png" style="height: 100%; width: 100%;" alt="">
        </el-carousel-item>
        <el-carousel-item style="position: absolute; top: 0; left: 0; right: 0; bottom: 0;">
          <img src="../assets/images/banners/banner4.png" style="height: 100%; width: 100%;" alt="">
        </el-carousel-item>
        <el-carousel-item style="position: absolute; top: 0; left: 0; right: 0; bottom: 0;">
          <img src="../assets/images/banners/banner5.png" style="height: 100%; width: 100%;" alt="">
        </el-carousel-item>
      </el-carousel>
    </div>



    <!-- 页面内容 -->
    <div class="content">

      <!-- 投稿流程 -->
      <div class="process">
        <h2>投稿流程</h2>
        <ol>
          <li>填写投稿表格：登录后，点击“投稿”按钮，填写相应的投稿表格，包括标题、内容等信息。</li>
          <li>上传稿件：根据表格要求上传你的稿件，支持的文件格式包括doc、docx和pdf。</li>
          <li>确认信息并提交：仔细检查填写的信息和上传的稿件，确认无误后点击“提交”按钮。</li>
        </ol>
      </div>

      <!-- 联系信息 -->
      <div class="contact">
        <el-card class="update-log">
          <div slot="header">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link href="http://www.xinshiguang.vip"
                target="_blank">http://www.xinshiguang.vip</el-link>
            </p>
            <p>客服邮箱：service@example.com</p>
            <p>客服电话：123-456-789</p>
          </div>
        </el-card>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      showSubmissionProcess: false, // 控制投稿流程信息区域的显示与隐藏
      showHelp: false, // 控制帮助信息区域的显示与隐藏
      showExampleDialog: false, // 控制文章样例模态框的显示与隐藏
      showExample: false
    };
  },
  methods: {
    // 点击“投稿流程”链接显示投稿流程信息
    toggleSubmissionProcess() {
      this.showSubmissionProcess = !this.showSubmissionProcess;
    },
    // 点击关闭按钮隐藏投稿流程信息
    hideSubmissionProcess() {
      this.showSubmissionProcess = false;
    },
    // 点击“帮助”链接显示帮助信息
    toggleHelp() {
      this.showHelp = !this.showHelp;
    },
    // 点击关闭按钮隐藏帮助信息
    hideHelp() {
      this.showHelp = false;
    },
    // 显示文章样例模态框
    showExampleArticle() {
      this.showExample = !this.showExample;
    },
    // 关闭文章样例模态框
    closeExampleDialog() {
      this.showExample = false;
    }
  },
};
</script>

<style scoped lang="scss">
.display-area {
  margin-top: 20px;
  padding: 20px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.display-area h2 {
  font-size: 20px;
  margin-bottom: 10px;
}

.display-area ol {
  list-style-type: decimal;
  margin-left: 20px;

  li {
    margin-bottom: 10px;
  }
}

.display-area button {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.display-area button:hover {
  background-color: #0056b3;
}

.app-container {
  font-family: "Arial", sans-serif;

  nav {
    background-color: #cccccf;
    padding: 10px 0;

    ul {
      list-style-type: none;
      margin: 0;
      padding: 0;
      text-align: center;

      li {
        display: inline-block;
        margin-right: 20px;

        a {
          color: rgb(8, 1, 1);
          text-decoration: none;

          &:hover {
            color: #dddddd;
          }
        }
      }
    }
  }

  .content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .process {
    margin-top: 40px;

    h2 {
      font-size: 24px;
      margin-bottom: 20px;
    }

    ol {
      list-style-type: decimal;
      margin-left: 20px;

      li {
        margin-bottom: 10px;
      }
    }

    p {
      margin-top: 20px;

      a {
        color: #007bff;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .contact {
    margin-top: 40px;
  }

  .update-log {
    span {
      font-size: 20px;
      font-weight: bold;
    }

    p {
      margin-top: 10px;

      i {
        margin-right: 5px;
      }

      &:first-child {
        margin-top: 0;
      }
    }
  }
}
</style>
