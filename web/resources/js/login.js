/**
 * Created by NotePad on 19.03.2016.
 */


function onLogin() {
    var username = $("#username").val();
    var password = $("#password").val();
    if (check(username) && check(password)){
        var xhr = new XMLHttpRequest();
        xhr.open("post","/login",true);
        xhr.send(JSON.stringify({username : username, password : password}));
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == "200") {
                    if(typeof(Storage) == "undefined") {
                        console.log('localStorage is not accessible');
                        return;
                    }
                    localStorage.setItem("username", username);
                    location.replace(xhr.responseText);
                }
                else {
                    alert(xhr.responseText);
                }
            }
        };
    }
}
function onRegister() {
    var username = $("#username").val();
    var password = $("#password").val();
    if (check(username) && check(password)){
        var xhr = new XMLHttpRequest();
        xhr.open("post","/register",true);
        xhr.send(JSON.stringify({username : username, password : password}));
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == "200") {
                    alert("Successful");
                }
                else {
                    alert(xhr.responseText);
                }
            }
        };
    }
}

function check(text){
    if (text == null ||text == "")
        return false;
    return true;
}