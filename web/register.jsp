<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head></head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员注册</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">
	//自定义校验
	$.validator.addMethod(
			//规则的名称
			"checkUsername",
			//校验函数
			function(value,element,params){
				var is_Exist = false;
			    //value:输入的内容
				//element:被校验的元素对象
				//params:规则对应的参数值
				//目的:对输入的username进行ajax校验
				$.ajax({
					url:"${pageContext.request.contextPath}/checkUser",
                    async:false,//如果实现的是异步，则不知道这段代码时候会执行，就会导致flag直接没有赋值
                    type:"POST",
                    data:{"username":value},
                    success:function (date) {
                        is_Exist = date.is_exist
                    },
                    dataType:"json"
				})
                return !is_Exist;
			}
	);

	//validate插件校验
	$(function () {
		$("#myForm").validate({
			rules:{
				username:{
					required:true,
                    checkUsername:true
				},
				password:{
					required:true,
					"rangelength":[6,12]
				},
				"repassword":{
					"required":true,
					equalTo:"#password"   //与password的值进行比较
				},
				email:{
					required:true,
					email:true
				},
				sex:{
					required:true
				}


			},
			messages:{
				username:{
					required:"用户名不能为空",
                    checkUsername:"该用户名已经存在"
				},
				password:{
					required:"密码不能为空",
					rangelength:"密码长度为6-12位"
				},
				"repassword":{

					equalTo:"两次密码输入不正确"
				},
				email:{
					required:"请输入邮箱地址",
					email:"email地址错误"
				},
				sex:{
					required:"必须选择一个性别"
				}
			}
		})
	})
</script>
<script type="text/javascript">
		//获取图片结点,点击图片并更改图片
		function change() {
			$("#ChangeImg").attr("src","image/1.jpg");
            <%--"${pageContext.request.contextPath}/checkImg"--%>
		}


    //ajax异步
	$.post(
			"${pageContext.request.contextPath}/checkCode",
			function (date) {
				if(${empty date.flag})
					return
				else if(date.flag){
				//获取放入验证码的结点
				$("#checkCodeSpan").html("输入的验证码正确").css("color","blue");
				}else{
					$("#checkCodeSpan").html("输入的验证码错误").css("color","red");
				}
			},
        "json"
	)

</script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

font {
	color: #3164af;
	font-size: 18px;
	font-weight: normal;
	padding: 0 10px;
}

.error{
	color: red;
}
</style>
</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>会员注册</font>USER REGISTER
				<form id="myForm" class="form-horizontal" style="margin-top: 5px;" action="${pageContext.request.contextPath}/register" method="post">
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" name="username"
								placeholder="请输入用户名">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword3" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="password" name="password"
								placeholder="请输入密码">
						</div>
					</div>
					<div class="form-group">
						<label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="confirmpwd" name="repassword"
								placeholder="请输入确认密码">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
						<div class="col-sm-6">
							<input type="email" class="form-control" id="inputEmail3"
								placeholder="Email" name="email">
						</div>
					</div>
					<div class="form-group">
						<label for="usercaption" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="usercaption"
								placeholder="请输入姓名" name="name">
						</div>
					</div>
					<div class="form-group opt">
						<label class="col-sm-2 control-label">性别</label>
						<div class="col-sm-6">
							<label class="radio-inline"> <input type="radio"
								 id="sex1" value="male" name="sex">
								男
							</label> <label class="radio-inline"> <input type="radio"
								 id="sex2" value="female" name="sex">
								女
							</label>
							<label for="sex" class="error" style="display: none">必须选择一个性别</label>
						</div>

					</div>
					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" class="form-control" name="birthday">
						</div>
					</div>

					<div class="form-group">
						<label  class="col-sm-2 control-label">验证码</label>
						<div class="col-sm-3">
							<input type="text" class="form-control" name="checkCode">
                            <span id="checkCodeSpan"></span>
						</div>
						<div class="col-sm-2">
							<img src="${pageContext.request.contextPath}/checkImg"  id="ChangeImg" onclick="change()"/>
						</div>

					</div>

					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" width="100" value="注册" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>




