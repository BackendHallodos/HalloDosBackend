/*
 ByHds
*/

$('document').ready(function(){

var password = document.getElementById("password");

var confirmPassword =document.getElementById("confirmPassword");

function bvalidatePassword(){

    if(password.value != confirmPassword.value){
        confirmPassword.setCustemValidity("Password Don't Match")
    }else{
        confirmPassword.setCustemValiditity('');
    }
}
password.onchange=validatePassword;
confirmPassword.onkeyup=validatePassword;

});