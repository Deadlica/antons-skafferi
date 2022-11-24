"use strict";
const currentDate = new Date();
const options = {weekday: 'long'};
const currentDay = new Intl.DateTimeFormat('en-US', options).format(currentDate);

document.addEventListener("DOMContentLoaded", function () {
    checkToday("Monday");
    checkToday("Tuesday");
    checkToday("Wednesday");
    checkToday("Thursday");
    checkToday("Friday");
    document.getElementById("lunch-days-list-id").querySelectorAll('li').forEach(item => {
        item.addEventListener('click', event => {
            clickedDay(item.id);
        })
    })

})


function checkToday(date) {
    if (date == currentDay) {
        document.getElementById(date).innerHTML = "IDAG";
        document.getElementById(date).style.color = "#7D5A3E";
        document.getElementById(date).style.fontWeight = "bold";
        document.getElementById(date).style.fontSize = "130%";
        document.getElementById("lunch-" + date).style.display = "inline"
    }
}

function clickedDay(Day) {
    document.getElementById("lunch-days-list-id").querySelectorAll('li').forEach(item => {
        if (item.id == Day) {
            document.getElementById(item.id).style.color = "#7D5A3E";
            document.getElementById(item.id).style.fontWeight = "bold";
            document.getElementById(item.id).style.fontSize = "130%";
            document.getElementById("lunch-" + item.id).style.display = "inline"
        } else {
            document.getElementById(item.id).style.color = "black";
            document.getElementById(item.id).style.fontWeight = "normal";
            document.getElementById(item.id).style.fontSize = "100%";
            document.getElementById("lunch-" + item.id).style.display = "none"
        }
    })
}