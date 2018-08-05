$(function() {
    let setting = {
        view: {
            dblClickExpand: false,
            showLine:true,
            selectedMulti:true,
            showIcon:true,
            fontCss:{color:"black"},
            showTitle:true
        },
        data: {
            simpleData:{
                idKey:"id",
                pIdKey:"pid",
                rootPId:""
            }
        },
        callback: {}
    };

    $.ajax({
        url:"/document/list",
        type:"get",
        success: function (data) {
            alert(data);
            $.fn.zTree.init($(".ztree"), setting, data);
        }
    })
});