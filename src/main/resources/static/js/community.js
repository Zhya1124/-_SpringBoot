//评论的通用方法
function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();//提交成功直接刷新
            } else {
                if (response.code == 2003) {  //没登陆的跳转登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=8fbff3b33fd6876bea43&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}


//提交回复
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

//提交评论
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();//获取input的输入数据
    comment2target(commentId, 2, content);
}


//打开二级评论列表
function collapseComments(e) {
    var id = e.getAttribute("data-id");//获取this的data-id属性就是评论的id
    var comments = $("#comment-" + id);//获取id为this相同的id的标签，如果不从this里拿，你不知道id获取不到对应的标签
    //comments.toggleClass("in");//加in的class可以展开
    //获取一下collapse的状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {   //不为空取消下拉，且按钮取消持续变蓝
        comments.removeClass("in");
        e.classList.remove("active");
        e.removeAttribute("data-collapse");
    } else {
        var subCommentContainer = $("#comment-" + id);//获取大的二级评论标签
        if (subCommentContainer.children().length != 1) {//二级评论大标签初始只能有一个元素，如果不是一个元素停止append
            //展开二级评论
            comments.addClass("in");
            e.classList.add("active");
            //标记二级评论状态
            e.setAttribute("data-collapse", "in");
        } else {
            $.getJSON("/comment/" + id, function (data) {//点击才获取服务器返回来的二级评论结果（code message data三元组）

                $.each(data.data.reverse(), function (index, comment) {//把返回的二级评论data的内容拿出来

                    var mediaLeftElement = $("<div/>",{
                       "class" : "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>",{
                        "class" : "media-body"
                    }).append($("<h5/>",{
                        "class" : "media-heading",
                        "html" : comment.user.name
                    })).append($("<div/>",{
                        "html" : comment.content
                    })).append($("<div/>",{
                        "class" : "menu"
                    }).append($("<span/>",{
                        "class" : "pull-right",
                        "html" : moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>",{
                       "class" : "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);//向前拼接，有bug，反复添加
                });

                //展开二级评论
                comments.addClass("in");
                e.classList.add("active");
                //标记二级评论状态
                e.setAttribute("data-collapse", "in");
            });
        }
    }
    console.log(id);
}