/* 页面加载后执行 */

// 封面选择组件
Vue.component('cover-selector', {
    data: function () {
        return {
            coverImage: '',
            coverImageHeight: 0
        }
    },
    methods: {
        setCover: function () {
            const vue = this;
            const objFile = this.$refs.file.files[0];
            if (objFile === undefined)
                return;
            const formData = new FormData();
            formData.append('file', objFile);
            this.$http.post('https://media.ownfiles.cn/upload_image.php', formData).then(({data}) => {
                vue.$emit('input', data.data);
                vue.coverImage = data.data;
                data.code !== 0 && message.add("上传失败", data.msg);
            }, () => {
                message.add("上传失败", "网络连接发生错误");
            });
        },
        onResize: function () {
            this.coverImageHeight = this.$refs.imageSelector.offsetWidth / 16 * 9;
        }
    },
    mounted: function () {
        window.addEventListener('resize', () => this.onResize(), false);
        this.onResize();
    },
    template: ' \
        <div ref="imageSelector" @click="$refs.file.click()" class="cover-selector btn card card-hover" \
            :style="\`background-image: url(${coverImage}); height: ${coverImageHeight}px;\`"> \
            <input ref="file" type="file" @change="setCover" class="d-none" accept="image/*"> \
                <div class="bg-hide position-absolute rounded-3 top-0 start-0 end-0 bottom-0 d-flex align-items-center justify-content-center" \
                    :style="coverImage === \'\' ? \'opacity: 1\' : \'\'"> \
                    选择封面图片 \
                </div> \
        </div> \
    '
});

// 页码组件
Vue.component('pagination', {
    props: ['pages', 'nowPage', 'onChange'],
    template: ' \
        <div v-if="pages.length > 1" class="align-items-center mb-4"> \
           <nav aria-label="Page navigation"> \
               <ul class="pagination justify-content-center"> \
                   <li class="page-item"> \
                       <button class="page-link" @click="onChange(nowPage - 1)"> \
                           <i class="ai-chevron-left"></i> \
                       </button> \
                   </li> \
                   <li v-for="(item) in pages" class="page-item d-block" :class="{ active: nowPage == item }"> \
                       <button class="page-link" @click="onChange(item)">{{item}}</button> \
                   </li> \
                   <li class="page-item"> \
                       <button class="page-link" @click="onChange(nowPage + 1)"> \
                       <i class="ai-chevron-right"></i> \
                       </button> \
                   </li> \
               </ul> \
           </nav> \
        </div> \
    '
});

// 评论组件
Vue.component('comment', {
    props: ['object', 'objectId'],
    data: function () {
        return {
            count: 0,
            comment: [],
            page: 1,
            pages: [],
            maxPage: 2,
            content: ''
        }
    },
    methods: {
        post: function () {
            this.$http.post('/api/comment/add', {object: this.object, objectId: this.objectId, content: this.content}).then(({data}) => {
                if (data.code === 0) {
                    this.page = 1;
                    this.content = "";
                    this.load(true);
                } else {
                    message.add("发表失败", data.msg);
                }
            });
        },
        load: function (scroll) {
            this.$http.get('/api/comment/get', {params: {object: this.object, objectId: this.objectId, page: this.page}}).then(({data}) => {
                this.count = data.data.count;
                this.pages = data.data.pages;
                this.maxPage = data.data.maxPage;
                this.comment = data.data.comment;
                scroll && window.scroll({top: this.$refs.comment.parentNode.offsetTop, left: 0, behavior: 'smooth'});
            }, () => {

            });
        },
        changePage: function (page) {
            this.page = Math.min(this.maxPage, Math.max(1, page));
            this.load(true);
        }
    },
    mounted: function () {
        this.load(false);
    },
    template: ' \
    <div>\
        <div v-for="(item) in comment" class="card mb-4">\
            <div class="card-body pb-0">\
                <div class="mb-2">\
                    <h4 class="fs-sm mb-0" v-text="item.nickname"></h4><span class="fs-xs text-muted" v-text="item.time"></span>\
                </div>\
                <p style="white-space: pre-wrap; max-height: 200px;" class="overflow-hidden" v-text="item.content" :title="item.content"></p>\
            </div>\
        </div>\
        <pagination :pages="pages" :now-page="page" :on-change="changePage"></pagination>\
        <div>\
            <div class="input-group mb-3">\
                <span class="input-group-text">\
                <i class="ai-message-square"></i>\
                </span>\
                <textarea class="form-control" placeholder="" rows="4" v-model="content" maxlength="300"></textarea>\
            </div>\
            <button type="button" class="btn btn-primary float-end" @click="post">发表</button>\
        </div>\
    </div>\
    '
});

// 全局消息
var message = new Vue({
        el: '#message',
        data: {
            items: [],
            title: '',
            message: '',
            callback: null
        },
        methods: {
            del: function (delId) {
                this.items = this.items.filter(v => v.id !== delId);
            },
            add: function (title, content) {
                const id = new Date().getTime();
                this.items = [...this.items, {id, title, content}];
                this.items = this.items.slice(-5);
                setTimeout(() => message.del(id), 10000);
            },
            confirm: function (title, message, callback) {
                this.title = title;
                this.message = message;
                this.callback = callback;
                new bootstrap.Modal(this.$refs.modal).show();
            }
        }
    }
);