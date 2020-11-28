
//同意实名认证协议
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

//验证手机是否符合格式，并且是否是登录手机号
function verifyPhone() {
	isCorrectPhone = -1;
	var phone = $.trim($("#phone").val());

	if ("" == phone) {
		showError("phone","手机号不能为空！");
	}else if (!/^1[1-9]\d{9}$/.test(phone)) {
		showError("phone", "手机号码格式不正确！");
	}else {
		$.ajax({
			url:"/005-p2p-web/loan/page/verifyPhone",
			type:"post",
			data:{
				phone:phone,
			},
			dataType:"json",
			success:function (data) {
				if (data.code == 1 && data.success == "SUCCESS") {
					showSuccess("phone");
					isCorrectPhone = 1;
				}else {
					showError("phone", data.message);
				}
			},
			error:function () {
				showError("phone","系统异常...请稍后再试...");
			}
		})
	}
}

//验证姓名是否是中文
function verifyRealName() {
	isCorrectRealName = -1;
	var name = $.trim($("#realName").val());
	if ("" == name) {
		showError("realName", "请输入姓名！");
	}else if (!/^[\u4e00-\u9fa5]{0,}$/.test(name)) {
		showError("realName", "请填写中文名！");
	}else {
		showSuccess("realName");
		isCorrectRealName = 1;
	}
}

//验证身份证号码是否正确
function verifyIdCard() {
	isCorrectIdCard = -1;
	var idCard = $.trim($("#idCard").val());
	if ("" == idCard) {
		showError("idCard", "请输入身份证号！");
	}else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
		showError("idCard","身份证号格式错误！");
	}else {
		showSuccess("idCard");
		isCorrectIdCard = 1;
	}
}

var isCorrectPhone = -1;
var isCorrectRealName = -1;
var isCorrectIdCard = -1;

$(function () {
	//验证手机号
	$("#phone").blur(function () {
		verifyPhone();
	})

	//验证身份证号码和姓名
	$("#realName").blur(function () {
		verifyRealName();
	})
	$("#idCard").blur(function () {
		verifyIdCard();
	})

	//点击认证按钮
	$("#btnRegist").click(function () {
		if (isCorrectPhone == 1 && isCorrectRealName == 1 && isCorrectIdCard == 1) {
			var phone = $.trim($("#phone"));
			var realName = $.trim($("#realName"));
			var idCard = $.trim($("#idCard"));

			$.ajax({
				url:"/005-p2p-web/loan/page/realNameAuthentication",
				type:"post",
				data:{
					phone: phone,
					name: realName,
					idCard: idCard,
				},
				dataType: "json",
				success:function (data) {
					if (data.code == 1 && data.success == "SUCCESS") {
						window.location.href = "/005-p2p-web/loan/page/login";
						//取消注册协议确认勾选
						$("#agree").prop("checked",false);
					}
				},
				error: function () {

				}
			})
		}
	})
})