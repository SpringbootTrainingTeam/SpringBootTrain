$(function() {
    let setting = {
        view: {
            dblClickExpand: false,
            showLine:true,
            selectedMulti:true,
            showIcon:true,
            fontCss:{color:"white"},
            showTitle:true
        },
        data: {
            simpleData:{
                enable: true,
                idKey:"id",
                pIdKey:"pid",
                rootPId:"./uploads"
            }
        },
        callback: {}
    };

    $.ajax({
        url:"/document/list",
        type:"get",
        success: function (data) {
            $.fn.zTree.init($(".ztree"), setting, data);
        }
    })
});