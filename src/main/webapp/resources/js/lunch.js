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
})

function checkToday(date) {
    if (date == currentDay) {
        document.getElementById(date).innerHTML = "IDAG";
        document.getElementById(date).style.color = "#7D5A3E";
        document.getElementById(date).style.fontWeight = "bold";
    }
}