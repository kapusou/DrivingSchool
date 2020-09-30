// 验证表单内容

//验证邮箱
function checkEmail(email) {
    var myreg=/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/;
    if (!myreg.test(email)) {
        return false;
    } else {
        return true;
    }
}