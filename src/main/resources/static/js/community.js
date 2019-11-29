/**
 * 提交回复
 */
function post() {
    var questionId=$("#question_id").val();
    var content=$("#comment_content").val();
    comment2target(questionId,1,content);
}

function comment2target(targetId,type,content) {
    if(!content){
        alert("不能回复空内容····");
        return;
    }
    $.ajax({
        type:"post",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success:function (response) {
            if(response.code==200){
                window.location.reload();
            }else{
                if(response.code==2003){
                    var isAccepted=confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.e2ff1d2969577c18&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable",true);
                    }
                }else{
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType:"json"
    });
}
function comment(e) {
    var commentId=e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

/**
 *展开二级评论
 */
function collapseComents(e) {
    var id=e.getAttribute("data-id");
    var comments=$("#comment-"+id);
    //获取一下二级评论的展开状态
    var collapse=comments.hasClass("in");
    if(collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.classList.remove("active");
    }else{
        var subCommentContainer=$("#comment-"+id);
        if(subCommentContainer.children().length!=1){
            //展开二级评论
            comments.addClass("in");
            //logo变色
            e.classList.add("active");
        }else{
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(),function (index,comment) {

                    var media=$("<div/>",{
                        "class":"media"
                    });

                    var mediaLeft=$("<div/>",{
                        "class":"media-left"});

                    var avatar=$("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    });

                    var mediaBody=$("<div/>",{
                        "class":"media-body"});

                    var h5=$("<h5/>",{
                        "class":"media-heading"});

                    var name=$("<span/>",{
                        "text":comment.user.name});

                    var content=$("<span/>",{
                        "text":comment.content});

                    var menu=$("<div/>",{
                        "class":"menu"});

                    var gmtCreate=$("<span/>",{
                        "class":"pull-right",
                        "text":fmtDate(comment.gmtCreate)});

                    media.append(mediaLeft,mediaBody,"<hr/>");
                    mediaLeft.append(avatar);
                    mediaBody.append(h5,content,menu);
                    h5.append(name);
                    menu.append(gmtCreate);
                    subCommentContainer.prepend(media);
                });
                //展开二级评论
                comments.addClass("in");
                //logo变色
                e.classList.add("active");
            });
        }
    }
}

//format日期格式化
function fmtDate(time){
    var date=new Date();

    var data = new Date(time);
    var year = data.getFullYear();  //获取年
    var month = data.getMonth() + 1;    //获取月
    var day = data.getDate(); //获取日
    var hours = data.getHours();
    var minutes = data.getMinutes();
    if (date.getFullYear()==year&&date.getMonth() + 1==month){
        if (date.getDate()==day){
            time = "今天" + " " + hours + ":" + minutes;
            return time;
        }
        if ((parseInt(date.getDate())-1)==day){
            time = "昨天" + " " + hours + ":" + minutes;
            return time;
        }
    }

    time = year + "年" + month + "月" + day + "日" + " " + hours + ":" + minutes;
    return time;
}