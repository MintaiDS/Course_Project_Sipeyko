/**
 * Created by NotePad on 27.02.2016.
 */
var textDiv;
var startButton = document.createElement("button");
startButton.setAttribute("type", "button");
startButton.setAttribute("class", "btn btn-info");
startButton.setAttribute("onclick", "getTask1()");
startButton.innerHTML = "Сгенерировать задачу";
var textField = document.createElement("p");
textField.setAttribute("class", "main-text");
var answer;
var answerField = document.createElement("input");
answerField.setAttribute("id","answer");
var answerButton = document.createElement("button");
answerButton.setAttribute("class","btn btn-info");
answerButton.setAttribute("onclick","checkAnswer()");
answerButton.innerHTML = "Ответ";

var themePath = "Greet.txt";
var theme = "";
var curTask;

function onLogout() {
    var xhr = new XMLHttpRequest();
    xhr.open("post","/logout",true);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == "200") {
                alert(xhr.responseText);
                localStorage.removeItem("username");
                location.replace(xhr.responseText);
            }
            else {
                console.log('logout error');
            }
        }
    };
}

function getTask(number) {
    var xhr = new XMLHttpRequest();
    xhr.open("get","/index"+"?task="+number,true);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == "200") {
                console.log('get message success');
                curTask = number;
                var result = $.parseJSON(xhr.response);
                answer = result.answer;
                textField.innerHTML = result.text;
                if (textDiv.lastChild != answerButton) {
                    textDiv.appendChild(answerField);
                    textDiv.appendChild(answerButton);
                }
            }
            else {
                console.log('get message error');
            }
        }
    }
}

function getStatistics() {
    var xhr = new XMLHttpRequest();
    xhr.open("get","/index"+"?task="+"-1",true);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == "200") {
                console.log('get message success');
                var result = xhr.response;
                textField.innerHTML = result;
                if (textDiv.lastChild == answerButton){
                    textDiv.removeChild(answerButton);
                    textDiv.removeChild(answerField);
                }
            }
            else {
                console.log('get message error');
            }
        }
    }
}

function checkAnswer(){
    var ans = $("#answer").val();
    if (ans != "")
    if (ans == answer){
        var xhr = new XMLHttpRequest();
        xhr.open("post","/index",true);
        xhr.send(curTask);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == "204") {
                    alert("Right answer!");
                    toTheme("Greet");
                }
                else {
                    console.log('get message error');
                }
            }
        }
    } else {
        alert("Wrong answer");
    }
}

function toTheme(text){
    var xhr = new XMLHttpRequest();
    xhr.open("get","/resources/text/"+text+".txt",true);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == "200") {
                console.log('get message success');
                textField.innerHTML = xhr.responseText;
                if (textDiv.lastChild == answerButton){
                    textDiv.removeChild(answerButton);
                    textDiv.removeChild(answerField);
                }
            }
            else {
                console.log('get message error');
            }
        }
    }
}
function toTasks(){
    document.getElementById("text").removeChild(textField);
    if (textDiv.lastChild == answerButton){
        textDiv.removeChild(answerButton);
        textDiv.removeChild(answerField);
    }
    document.getElementById("text").appendChild(startButton);
}

window.onload = function () {
    textDiv = document.getElementById("text");
    textDiv.appendChild(textField);
    //toTheme("Greet");
}