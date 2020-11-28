//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});


//手机号验证：1、格式验证，2、是否已注册验证
function verifyPhone(){
	isCorrectPhone = -1;
	var phone = $.trim($("#phone").val());
	hideError("phone");
	if ("" == phone) {
		showError("phone", "手机号码不能为空！");
	}else if(!/^1[1-9]\d{9}$/.test(phone)) {
		showError("phone", "手机号码格式不正确！");
	}else {
		$.ajax({
			url:"/005-p2p-web/loan/page/checkPhone",
			type:"post",
			data:"phone="+phone,
			success:function(data) {
				if (data.code == "1" && data.success == "SUCCESS") {
					showSuccess("phone");
					isCorrectPhone = 1;
				}
				if (data.code == "-1" && data.error == "ERROR") {
					showError("phone", data.message);
				}
			},
			error:function() {
				showError("phone", "系统繁忙...请稍后再试...");
			}
		})
	}
}

//登录密码验证：是否符合规定的密码格式要求
function verifyLoginPassword() {
	var loginPassword = $.trim($("#loginPassword").val());
	hideError("loginPassword");
	if ("" == loginPassword){
		showError("loginPassword", "密码不能为空！");
	} else if (loginPassword.length<6 || loginPassword.length>20) {
		showError("loginPassword", "密码长度必须是6-20位！");
	} else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
		showError("loginPassword", "密码字符只能使用数字和大小写英文字母！不能含有其它字符！");
	} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
		showError("loginPassword", "密码必须同时包含英文和数字！");
	} else {
		showSuccess("loginPassword");
		return isCorrectLoginPassword = 1;
	}
	isCorrectLoginPassword = -1;
}

//短信验证码验证：验证码的格式验证
function verifyMessageCode() {
	var inputMessageCode = $.trim($("#messageCode").val());
	hideError("messageCode");
	if ("" == inputMessageCode) {
		showError("messageCode", "验证码不能为空！");
	}else if (inputMessageCode.length != 6) {
		showError("messageCode", "验证码长度只能是6位数！");
	}else if (!/^[0-9]+$/.test(inputMessageCode)) {
		showError("messageCode", "验证码只能输入数字，不能含其它字符！");
	}else{
		showSuccess("messageCode");
		return isCorrectMessageCode = 1;
	}
	isCorrectMessageCode = -1;
}

//发送短信验证码
function sendMessageCode(_this) {
	var phone = $.trim($("#phone").val());

	//验证码点击按钮倒计时，防止不停的发送验证码
	if (!_this.hasClass("on")) {
		if (isCorrectPhone == 1 && isCorrectLoginPassword == 1) {
			$.ajax({
				url:"/005-p2p-web/loan/page/messageCode",
				type:"post",
				data: {
					phone:phone
				},
				dataType: "json",
				success: function (data) {
					if (data.code == "1" && data.success == "SUCCESS") {
						alert(data.message);

						$.leftTime(60, function (d) {
							if (d.status) {
								_this.addClass("on");
								_this.html((d.s=="00"?"60":d.s)+"秒后重新获取");
							} else {
								_this.removeClass("on");
								_this.html("获取验证码");
							}
						})
					}
				},
				error: function () {
					showError("messageCode", "系统繁忙...请稍后再试...");
				}
			})
		}
	}
}

//定义全局变量，可以让自定义的函数也使用该变量
var isCorrectPhone = -1;
var isCorrectLoginPassword = -1;
var isCorrectMessageCode = -1;

$(function (){

	//手机号验证
	$("#phone").blur(function () {
		verifyPhone();
	})

	//登录密码验证
	$("#loginPassword").blur(function () {
		verifyLoginPassword();
	})

	//短信验证码验证
	$("#messageCode").blur(function () {
		verifyMessageCode();
	})

	//发送短信验证码
	$("#messageCodeBtn").click(function () {
		var _this = $(this);
		sendMessageCode(_this);
	})

	//点击注册按钮
	$("#btnRegist").click(function () {

		//在点击注册之后将表单发送给后台之前，必须保证前面的验证全部正确合法
		if (isCorrectPhone == 1 && isCorrectLoginPassword == 1 && isCorrectMessageCode == 1) {
			var phone = $.trim($("#phone").val());
			var loginPassword = $.md5($.trim($("#loginPassword").val()));
			var inputMessageCode = $.trim($("#messageCode").val());

			$.ajax({
				url:"/005-p2p-web/loan/page/regist",
				type:"post",
				data: {
					phone: phone,
					loginPassword: loginPassword,
					inputMessageCode:inputMessageCode
				},
				dataType:"json",
				success:function (data) {
					if (data.code == "1" && data.success == "SUCCESS") {
						alert(data.message);
						window.location.href = "/005-p2p-web/loan/page/realName";
						//取消注册协议确认勾选
						$("#agree").prop("checked",false);
					}else if (data.code == "-1" && data.error == "ERROR") {
						alert(data.message);
					}
				},
				error:function(data) {
					showError("btnRegist", "系统繁忙...请稍后再试...");
				}
			})
		}else{
			alert("注册失败");
		}
	})

})

