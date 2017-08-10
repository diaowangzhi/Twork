//ws处理
var socket = null;
var im = {

    init: function () {
        if ('WebSocket' in window) {
            var socketurl = 'ws://' + window.location.host + '/websocket/socketServer';
            socket = new WebSocket(socketurl);
            im.startListener();
        }
        else {
            alert("当前浏览器不支持websocket！请更换浏览器使用！");
        }
    },
    startListener: function () {
        if (socket) {
            //连接发生错误的回调方法
            socket.onerror = function () {
                console.log("连接失败");
            };
            //连接成功建立的回调方法
            socket.onopen = function () {
                console.log("连接成功");
            };
            socket.onmessage = function (msg) {
                console.log("接收到消息");
                im.handleMessage(msg);
            };
            //连接关闭的回调方法
            socket.onclose = function () {
                console.log("连接关闭");
            }
        }
    },
    handleMessage: function (msg) {
        msg = JSON.parse(msg.data);
        // console.log(msg);
        switch (msg.emit) {
            case 'chatMessage':
                layim.getMessage(msg.data);
                break;
            case 'addList':
                layim.addList(msg.data);
                break;
            case 'removeList':
                layim.removeList(msg.data);
                break;
            case 'messageBox':
                $.ajax({
                    url: 'api/message/number',
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //alert(errorcode[data.code]);
                        if (data.code == 1000 && data.number > 0) {
                            layim.msgbox(data.number);
                        }
                    },
                    error: function (data) {
                        alert("消息盒子请求失败");
                    }
                });
                break;
            default:
                break;
        }
    }
};
im.init();

layui.use('layim', function (layim) {

    window.layim = layim;

    //基础配置
    layim.config({

        //初始化接口
        init: {
            url: 'api/user/init',
            data: {}
        }

        //查看群员接口
        , members: {
            url: 'api/group/getmembers' //api/websocket/member
            , data: {}
        }

        //上传图片接口
        , uploadImage: {
            url: 'api/file/pictureupload' //（返回的数据格式见下文） api/upload?t=img
            , type: 'post' //默认post
        }

        //上传文件接口
        /*, uploadFile: {
            url: '/upload/file' //（返回的数据格式见下文）  api/upload?t=file
            , type: '' //默认post
        }*/

        /*//扩展工具栏
         ,tool: [{
         alias: 'code'
         ,title: '代码'
         ,icon: '&#xe64e;'
         }]*/

        //,brief: true //是否简约模式（若开启则不显示主面板）

        , title: 'Twork' //自定义主面板最小化时的标题
        //,right: '100px' //主面板相对浏览器右侧距离
        //,minRight: '90px' //聊天面板最小化时相对浏览器右侧距离
        , initSkin: '5.jpg' //1-5 设置初始背景*/
        //,skin: ['aaa.jpg'] //新增皮肤
        //,isfriend: false //是否开启好友
        //,isgroup: false //是否开启群组
        , min: true //是否始终最小化主面板，默认false
        //,notice: true //是否开启浏览器外消息提醒，默认false
        , voice: false //声音提醒，默认开启，声音文件为：default.wav

        , msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        //, find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        , chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html' //聊天记录页面地址，若不开启，剔除该项即可
        , copyright: 'false'
    });

    /*layim.setChatMin();*/

    //监听在线状态的切换事件
    layim.on('online', function (data) {
        console.log(data);
        var msg = JSON.stringify(data);
        socket.send(msg);
    });

    //监听签名修改
    layim.on('sign', function (value) {
        //console.log(value);
    });

    //监听自定义工具栏点击，以添加代码为例
    /*  layim.on('tool(code)', function(insert){
     layer.prompt({
     title: '插入代码'
     ,formType: 2
     ,shade: 0
     }, function(text, index){
     layer.close(index);
     insert('[pre class=layui-code]' + text + '[/pre]'); //将内容插入到编辑器
     });
     });*/

    //监听layim建立就绪
    layim.on('ready', function (res) {

        //console.log(res.mine);

        $.ajax({
            url: 'api/message/number',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                //alert(errorcode[data.code]);
                console.log(data);
                if (data.code == 1000 && data.number > 0) {
                    layim.msgbox(data.number);
                }
            },
            error: function (data) {
                alert("消息盒子请求失败");
            }
        });
    });

    //监听发送消息
    layim.on('sendMessage', function (data) {
        console.log(data);
        //var msg = JSON.stringify(data);

        socket.send(JSON.stringify({
            emit: 'chatMessage',
            data: data
        }));
    });

    //监听查看群员
    layim.on('members', function (data) {
        console.log(data);
    });

    //监听聊天窗口的切换
    layim.on('chatChange', function (data) {
        console.log(data);
    });
});
