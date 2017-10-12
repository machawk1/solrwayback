Vue.component('header-container', {
    props: [],
    template: `
    <div id="header">
        <h2>SOLR Wayback page resources</h2>
    </div>    
    `,
})

Vue.component('page-resources', {
    props: ["resourceObj"],
    template: `
    <div id="pageResources">
        <h3>{{resourceObj.pageUrl}}</h3>
        <img class="preview" :src="resourceObj.pagePreviewUrl" alt="webpage preview">
        <div class="pageinfo">
            <div class="label">Crawl date</div>
            <div class="text">{{resourceObj.pageCrawlDate}}</div>
            <table id="resourcesTable">
                <thead>
                    <tr>
                        <th>Resource URL</th>
                        <th>Content type</th>
                        <th>Time diff.</th>
                        <th>See/download</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="resource in resourceObj.resources">
                        <td><a :href="resource.url">{{resource.url}}</a></td>
                        <td>{{resource.contentType}}</td>
                        <td>{{resource.timeDifference}}</td>
                        <td v-if="resource.contentType==='image'"><img :src="resource.downloadUrl"></td> 
                        <td v-else><a :href="resource.downloadUrl">Download</a></td> 
                    </tr>
                </tbody>
            </table>
        </div>
    </div>    
    `
})

var router = new VueRouter({
    mode: 'history',
    routes: []
})

var app = new Vue({
    router,
    el: '#app',
    data: {
        resourceObj : {},
        source_file_path: "",
        offset: ""
    },
    methods: {
        getQueryparams:function(){
            this.source_file_path = this.$route.query.source_file_path;
            this.offset = this.$route.query.offset;
            this.getTimestamps();
            console.log('this.source_file_path', this.source_file_path);
            console.log('this.offset', this.offset);
        },
        getTimestamps: function(){
            this.resourceUrl = "http://localhost:8080/solrwayback/services/frontend/timestampsforpage?source_file_path=" + this.source_file_path + '&offset=' + this.offset;

            this.$http.get(this.resourceUrl).then((response) => {
                this.resourceObj = response.body;
                console.log('response.body: ', response.body);
                console.log('this.resourceObj: ', this.resourceObj);
                //this.hideSpinner();
            }, (response) => {
                console.log('error: ', response);
            });
        }
        /*showSpinner: function(){
            this.spinner = true;
        },
        hideSpinner: function(){
            this.spinner = false;
        },*/
    },
    created: function() {
        this.getQueryparams()
        /*this.spinner = true;*/
    }
})
