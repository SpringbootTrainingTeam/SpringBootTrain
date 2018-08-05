$(function() {
    var zTreeObj;
    var setting = {
        view: {
            selectedMulti: false
        },
        data: {

        },
        callback: {}
    };

    zTreeNodes = [
        {"name":"网站导航", open:true, children: [
                { "name":"google", "url":"http://g.cn", "target":"_blank"},
                { "name":"baidu", "url":"http://baidu.com", "target":"_blank"},
                { "name":"sina", "url":"http://www.sina.com.cn", "target":"_blank"}
            ]
        }
    ];
    zTreeObj = $.fn.zTree.init($(".ztree"), setting, zTreeNodes);

    // $.ajax({
    //     url:"/",
    //     type:"post",
    //     success: function (data) {
    //         let zNode = data.data.nodes;
    //         $.fn.zTree.init($(".ztree"), setting, zNode);
    //     }
    // })
});