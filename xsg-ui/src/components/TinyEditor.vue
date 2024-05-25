<template>
  <div className="tinymce-box">
    <Editor v-model="contentValue" :init="init" :disabled="disabled" @onClick="onClick" />
  </div>
</template>

<script>
// 引入tinymce

import tinymce from 'tinymce/tinymce'

//引入tinymce编辑器
import Editor from '@tinymce/tinymce-vue'

//下面这三行解决tinymce编辑器变成只读模式
import 'tinymce/themes/silver/theme'
import 'tinymce/icons/default/icons'
import 'tinymce/skins/ui/oxide/skin.css'
import 'tinymce/skins/ui/oxide-dark/skin.css'
import '/src/components/Tinymce/plugins.js'

import 'tinymce/plugins/advlist'; //高级列表
import 'tinymce/plugins/anchor'; //锚点
import 'tinymce/plugins/autolink'; //自动链接
import 'tinymce/plugins/autoresize'; //编辑器高度自适应,注：plugins里引入此插件时，Init里设置的height将失效
import 'tinymce/plugins/autosave'; //自动存稿
import 'tinymce/plugins/charmap'; //特殊字符
import 'tinymce/plugins/code'; //编辑源码
import 'tinymce/plugins/codesample'; //代码示例
import 'tinymce/plugins/directionality'; //文字方向
import 'tinymce/plugins/emoticons'; //表情
import 'tinymce/plugins/fullscreen'; //全屏
import 'tinymce/plugins/help'; //帮助
import 'tinymce/plugins/image'; //插入编辑图片
import 'tinymce/plugins/importcss'; //引入css
import 'tinymce/plugins/insertdatetime'; //插入日期时间
import 'tinymce/plugins/link'; //超链接
import 'tinymce/plugins/lists'; //列表插件
import 'tinymce/plugins/media'; //插入编辑媒体
import 'tinymce/plugins/nonbreaking'; //插入不间断空格
import 'tinymce/plugins/pagebreak'; //插入分页符
import 'tinymce/plugins/preview'; //预览
import 'tinymce/plugins/quickbars'; //快速工具栏
import 'tinymce/plugins/save'; //保存
import 'tinymce/plugins/searchreplace'; //查找替换
import 'tinymce/plugins/table'; //表格
import 'tinymce/plugins/template'; //内容模板
import 'tinymce/plugins/visualblocks'; //显示元素范围
import 'tinymce/plugins/visualchars'; //显示不可见字符
import 'tinymce/plugins/wordcount'; //字数统计
import 'tinymce/plugins/print';
import 'tinymce/plugins/imagetools';
import 'tinymce/plugins/hr';
import 'tinymce/plugins/textpattern';


export default {
  name: 'TEditor',
  components: {
    Editor
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      init: {
        // 配置信息
        selector: "#textarea",
        height: 800,
        language_url: "/tinymce/langs/zh_CN.js", // 中文包的存放路径
        language: "zh_CN",
        toolbar_mode: "none",
        plugins: 'print preview searchreplace autolink directionality visualblocks visualchars fullscreen image imagetools  link  template code codesample table charmap hr pagebreak nonbreaking anchor insertdatetime advlist lists wordcount textpattern autosave',
        toolbar: "fullscreen undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | \\\n" +
          "                styleselect formatselect fontselect fontsizeselect | bullist numlist | blockquote subscript superscript removeformat | \\\n" +
          "                table image resize charmap hr pagebreak insertdatetime print preview | code selectall searchreplace visualblocks | indent2em lineheight formatpainter axupimgs",
        skin_url: '/tinymce/skins/ui/oxide',  //皮肤：浅色
        autosave_ask_before_unload: false, //去掉网页关闭时的弹窗
        //字体
        font_formats: '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',  //字体样式
        // image_advtab: true,
        // a11y_advanced_options: true,



        //图片上传按钮显示
        file_picker_types: 'image',
        image_caption: true,
        // 图片上传（下面两个都是图片上传方法，根据需要使用过一个即可）
        images_upload_handler: (blobInfo, success, failure) => { //此选项允许你使用自定义函数代替TinyMCE来处理上传操作。该自定义函数可接受四个参数：如果未使用此配置，TinyMCE将使用ajax每次上传一个图片，并在成功返回结果后调用成功回调函数。
          const img = 'data:image/jpeg;base64,' + blobInfo.base64()
          success(img)
          console.log(success)
          console.log(failure)
          const formData = new FormData()
          formData.append('file', blobInfo.blob())
        },
        file_picker_callback: function (cb, value, meta) {
          var input = document.createElement('input');
          input.setAttribute('type', 'file');
          input.setAttribute('accept', 'image/*');

          /*
            Note: In modern browsers input[type="file"] is functional without
            even adding it to the DOM, but that might not be the case in some older
            or quirky browsers like IE, so you might want to add it to the DOM
            just in case, and visually hide it. And do not forget do remove it
            once you do not need it anymore.
          */

          input.onchange = function () {
            var file = this.files[0];

            var reader = new FileReader();
            reader.onload = function () {
              /*
                Note: Now we need to register the blob in TinyMCEs image blob
                registry. In the next release this part hopefully won't be
                necessary, as we are looking to handle it internally.
              */
              var id = 'blobid' + (new Date()).getTime();
              var blobCache = tinymce.activeEditor.editorUpload.blobCache;
              var base64 = reader.result.split(',')[1];
              var blobInfo = blobCache.create(id, file, base64);
              blobCache.add(blobInfo);

              /* call the callback and populate the Title field with the file name */
              cb(blobInfo.blobUri(), { title: file.name });
            };
            reader.readAsDataURL(file);
          };

          input.click();
        },
        statusbar: false,//直接设置为false  去掉编辑器下方的广告
      },
      contentValue: this.value

    }
  },
  watch: {
    value(newValue) {
      this.contentValue = newValue
    },
    contentValue(newValue) {
      this.$emit('input', newValue)
    },
  },
  methods: {
    // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    onClick(e) {
      this.$emit('onClick', e, tinymce)
    },
    //清空内容
    clear() {
      this.contentValue = ''
    },
  },
}
</script>

<style lang="scss">
figure.image {
  display: inline-block;
  border: 1px solid gray;
  margin: 0 2px 0 1px;
  background: #f5f2f0;
}

figure.align-left {
  float: left;
}

figure.align-right {
  float: right;
}

figure.image img {
  margin: 8px 8px 0 8px;
}

figure.image figcaption {
  margin: 6px 8px 6px 8px;
  text-align: center;
}
</style>