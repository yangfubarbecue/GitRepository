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

//显示手机号
function showPhone() {
	$.ajax({
		url:"/005-p2p-web/loan/page/showPhone",
		type:"get",
		success:function(data) {
			if (data.code == "1" && data.success == "SUCCESS") {
				$("#phone").val(data.message);
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
//原登录密码验证：是否符合规定的密码格式要求、是否与原密码一致
function verifyOldLoginPassword() {
	isCorrectOldLoginPassword = -1;
	var oldLoginPassword = $.trim($("#oldLoginPassword").val());
	hideError("oldLoginPassword");
	if ("" == oldLoginPassword){
		showError("oldLoginPassword", "密码不能为空！");
	} else if (oldLoginPassword.length<6 || oldLoginPassword.length>20) {
		showError("oldLoginPassword", "密码长度必须是6-20位！");
	} else if (!/^[0-9a-zA-Z]+$/.test(oldLoginPassword)) {
		showError("oldLoginPassword", "密码字符只能使用数字和大小写英文字母！不能含有其它字符！");
	} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(oldLoginPassword)) {
		showError("oldLoginPassword", "密码必须同时包含英文和数字！");
	} else {
		$.ajax({
			url:"/005-p2p-web/loan/page/verifyOldLoginPassword",
			type:"post",
			data:{password:$.md5(oldLoginPassword)},
			success:function(data) {
				if (data.code == "1" && data.success == "SUCCESS") {
					showSuccess("oldLoginPassword");
					isCorrectOldLoginPassword = 1;
				}
				if (data.code == "-1" && data.error == "ERROR") {
					showError("oldLoginPassword", data.message);
				}
			},
			error:function() {
				showError("oldLoginPassword", "系统繁忙...请稍后再试...");
			}
		})
	}
}

//新登录密码验证：是否符合规定的密码格式要求
function verifyNewLoginPassword() {
	var newLoginPassword = $.trim($("#newLoginPassword").val());
	hideError("newLoginPassword");
	if ("" == newLoginPassword){
		showError("newLoginPassword", "密码不能为空！");
	} else if (newLoginPassword.length<6 || newLoginPassword.length>20) {
		showError("newLoginPassword", "密码长度必须是6-20位！");
	} else if (!/^[0-9a-zA-Z]+$/.test(newLoginPassword)) {
		showError("newLoginPassword", "密码字符只能使用数字和大小写英文字母！不能含有其它字符！");
	} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(newLoginPassword)) {
		showError("newLoginPassword", "密码必须同时包含英文和数字！");
	} else if ($.trim($("#oldLoginPassword").val()) == newLoginPassword) {
		showError("newLoginPassword", "新密码不能和原密码相同！")
	} else {
		showSuccess("newLoginPassword");
		return isCorrectNewLoginPassword = 1;
	}
	isCorrectNewLoginPassword = -1;
}

//确认登录密码验证：是否符合规定的密码格式要求
function verifyConfirmLoginPassword() {
	var confirmLoginPassword = $.trim($("#confirmLoginPassword").val());
	hideError("confirmLoginPassword");
	if ("" == confirmLoginPassword){
		showError("confirmLoginPassword", "密码不能为空！");
	} else if (confirmLoginPassword.length<6 || confirmLoginPassword.length>20) {
		showError("confirmLoginPassword", "密码长度必须是6-20位！");
	} else if (!/^[0-9a-zA-Z]+$/.test(confirmLoginPassword)) {
		showError("confirmLoginPassword", "密码字符只能使用数字和大小写英文字母!");
	} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(confirmLoginPassword)) {
		showError("confirmLoginPassword", "密码必须同时包含英文和数字！");
	} else if (!($.trim($("#newLoginPassword").val()) == confirmLoginPassword)) {
		showError("confirmLoginPassword", "与新密码不一致！")
	} else {
		showSuccess("confirmLoginPassword");
		return isCorrectConfirmLoginPassword = 1;
	}
	isCorrectConfirmLoginPassword = -1;
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
		// showSuccess("messageCode");
		return isCorrectMessageCode = 1;
	}
	isCorrectMessageCode = -1;
}

//发送短信验证码
function sendMessageCode(_this) {
	var phone = $.trim($("#phone").val());

	//验证码点击按钮倒计时，防止不停的发送验证码
	if (!_this.hasClass("on")) {
		if (isCorrectOldLoginPassword == 1 &&
			isCorrectNewLoginPassword == 1 &&
			isCorrectConfirmLoginPassword == 1) {
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
var isCorrectOldLoginPassword = -1;
var isCorrectNewLoginPassword = -1;
var isCorrectConfirmLoginPassword = -1
var isCorrectMessageCode = -1;

$(function (){
	//显示手机号
	showPhone();

	//原登录密码验证
	$("#oldLoginPassword").blur(function () {
		verifyOldLoginPassword();
	})

	//新登录密码验证
	$("#newLoginPassword").blur(function () {
		verifyNewLoginPassword();
	})

	//确认新登录密码验证
	$("#confirmLoginPassword").blur(function () {
		verifyConfirmLoginPassword();
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

	//点击提交按钮
	$("#modifyBtn").click(function () {

		//在点击提交之后将表单发送给后台之前，必须保证前面的验证全部正确合法
		if (isCorrectOldLoginPassword == 1 &&
			isCorrectNewLoginPassword == 1 &&
			isCorrectConfirmLoginPassword == 1 &&
			isCorrectMessageCode == 1) {
			var phone = $.trim($("#phone").val());
			var loginPassword = $.md5($.trim($("#newLoginPassword").val()));
			var inputMessageCode = $.trim($("#messageCode").val());

			alert("需改前："+"手机："+phone+"密码："+loginPassword+"验证码："+inputMessageCode)
			$.ajax({
				url:"/005-p2p-web/loan/page/modifyPassword",
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
						window.location.href = "/005-p2p-web/index";
					}else if (data.code == "-1" && data.error == "ERROR") {
						alert(data.message);
					}
				},
				error:function(data) {
					showError("btnRegist", "系统繁忙...请稍后再试...");
				}
			})
		}else{
			alert("密码修改失败");
		}
	})

})

