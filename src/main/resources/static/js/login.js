var canGetCookie = 0;//是否支持存储Cookie 0 不支持 1 支持
var ajaxmockjax = 1;//是否启用虚拟Ajax的请求响 0 不启用  1 启用
var CodeVal = 0;

// 验证码
Code();
function Code() {
    if(canGetCookie === 1){
        createCode("AdminCode");
        let AdminCode = getCookieValue("AdminCode");
        showCheck(AdminCode);
    }else{
        showCheck(createCode(""));
    }
}

function showCheck(a) {
    CodeVal = a;
    let c = document.getElementById("myCanvas");
    let ctx = c.getContext("2d");
    ctx.clearRect(0, 0, 1000, 1000);
    ctx.font = "80px 'Hiragino Sans GB'";
    ctx.fillStyle = "#E8DFE8";
    ctx.fillText(a, 0, 100);
}

$(document).keypress(function (e) {
    // 回车键事件
    if (e.which === 13) {
        $('input[type="button"]').click();
    }
});

layui.use('layer', function () {
    //非空验证
    $('input[type="button"]').click(function () {
        let login = $('input[name="username"]').val();
        let pwd = $('input[name="password"]').val();
        let code = $('input[name="code"]').val();
        if (login === '') {
            ErrorAlert('请输入您的账号');
        } else if (pwd === '') {
            ErrorAlert('请输入密码');
        } else if (code === '' || code.length !== 4) {
            ErrorAlert('输入验证码');
        } else {
            let form = $("#login_form");
            form.submit();
            //认证中..
            fullscreen();
            $('.login').addClass('test'); //倾斜特效
            setTimeout(function () {
                $('.login').addClass('testtwo'); //平移特效
            }, 300);

            setTimeout(function () {
                $('.authentication').show().animate({ right: -320 }, {
                    easing: 'easeOutQuint',
                    duration: 600,
                    queue: false
                });
                $('.authentication').animate({ opacity: 1 }, {
                    duration: 200,
                    queue: false
                }).addClass('visible');
            }, 500);
        }
    })
});

let fullscreen = function () {
    elem = document.body;
    if (elem.webkitRequestFullScreen) {
        elem.webkitRequestFullScreen();
    } else if (elem.mozRequestFullScreen) {
        elem.mozRequestFullScreen();
    } else if (elem.requestFullScreen) {
        elem.requestFullscreen();
    } else {
        //浏览器不支持全屏API或已被禁用
    }
};
if(ajaxmockjax === 1){
    $.mockjax({
        url: 'Ajax/Login',
        status: 200,
        responseTime: 50,
        responseText: {"Status":"ok","Text":"登陆成功<br /><br />欢迎回来"}
    });
    $.mockjax({
        url: 'Ajax/LoginFalse',
        status: 200,
        responseTime: 50,
        responseText: {"Status":"Erro","Erro":"账号名或密码或验证码有误"}
    });
}
