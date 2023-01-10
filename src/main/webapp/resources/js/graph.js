"use strict";

const dateStringToInt = new Map();
const dateIntToString = new Map();
const dates = new Map();

const data_url = "http://89.233.229.182:8080/antons-skafferi-db-1.0-SNAPSHOT/api/orders/sales?";
let selectedMonth = new Date()
let startDate = "";
let endDate = "";
let orders = [];
let maxValue = 0;
let totalSales = 0;

document.addEventListener("DOMContentLoaded", function() {
    initMaps();
    setMonthTitle();
    document.getElementById("right_arrow").addEventListener("click", function() { // Load next month event
        loadNextMonth();
    })
    document.getElementById("left_arrow").addEventListener("click", function() { // Load previous month event
        loadPrevMonth();
    })
    generateDateParameters();
    generateGraph();
});

function createBar(date, amount) {
    let item = document.createElement("div");
    let bar = document.createElement("span");
    let title = document.createElement("span");

    const height = amount / maxValue * 100;

    bar.style = `height: ${height}%`;

    if(amount > 0) { //Adds price tag as long as orders were sold
        let cost = document.createElement("span");
        cost.innerText = amount.toString();
        cost.setAttribute("class", "cost")
        bar.append(cost);
    }
    title.innerText = date.split("-").at(2);
    title.setAttribute("class", "title");
    if(title.innerText === "01") { //Aligns the 1st bar next to y-axis
        item.style.paddingLeft = "0";
    }
    if(title.innerText.at(0) === "0") {
        title.innerText = title.innerText.at(1);
    }


    bar.append(title);
    item.append(bar);

    return item;
}

async function generateGraph() {
    await fetchSales();
    getDaysInMonth();
    getSalesInMonth();
    maxValue = findMapMax(dates);
    const graph = document.getElementById("graph");
    dates.forEach(function (value, key) {
        graph.append(createBar(key, value))
        totalSales += parseInt(value)
    })
    let p = document.getElementById("total_sales_text")
    p.innerText = "Total försäljning under månaden: " + totalSales.toString() + "kr";


}

function getDaysInMonth() {
    const key = startDate.split("=").at(1).substring(0, 8);
    const month = Number(startDate.split("-").at(1)) - 1; //js starts month counting at 0
    const year = Number(startDate.split("=").at(1).split("-").at(0));
    let date = new Date(year, month, 1);
    while(date.getMonth() === month) {
        if(date.getDate() < 10) {
            dates.set(key + "0" + date.getDate(), 0);
        }
        else {
            dates.set(key + date.getDate(), 0);
        }
        date.setDate(date.getDate() + 1);
    }
}

function getSalesInMonth() {
    orders.forEach(order => {
        const date = order.booking.date;
        const price = order.price;
        const oldPrice = dates.get(date);
        dates.set(date, oldPrice + price);
    })
}

function generateDateParameters() {
    startDate = "startDate=" + getDate(1);
    endDate = "endDate=" + getDate(0);
}

function getDate(day) {
    let whichMonth = 0;
    if(day === 0) {
        whichMonth = 1;
    }
    let firstDay = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + whichMonth, day);
    const dateInfo = firstDay.toString().split(" ");
    const year = dateInfo.at(3);
    const month = dateStringToInt.get(dateInfo.at(1));
    const date = dateInfo.at(2);
    return year + "-" + month + "-" + date;
}

function initMaps() {
    dateStringToInt.set("Jan", "01");
    dateStringToInt.set("Feb", "02");
    dateStringToInt.set("Mar", "03");
    dateStringToInt.set("Apr", "04");
    dateStringToInt.set("May", "05");
    dateStringToInt.set("Jun", "06");
    dateStringToInt.set("Jul", "07");
    dateStringToInt.set("Aug", "08");
    dateStringToInt.set("Sep", "09");
    dateStringToInt.set("Oct", "10");
    dateStringToInt.set("Nov", "11");
    dateStringToInt.set("Dec", "12");

    dateIntToString.set(0, "Januari");
    dateIntToString.set(1, "Februari");
    dateIntToString.set(2, "Mars");
    dateIntToString.set(3, "April");
    dateIntToString.set(4, "Maj");
    dateIntToString.set(5, "Juni");
    dateIntToString.set(6, "Juli");
    dateIntToString.set(7, "Augusti");
    dateIntToString.set(8, "September");
    dateIntToString.set(9, "Oktober");
    dateIntToString.set(10, "November");
    dateIntToString.set(11, "December");
}

function fetchSales() {
    return fetch(data_url + startDate + "&" + endDate)
        .then(response => response.json())
        .then(data => {
            data.forEach(order => {
                orders.push(order);
            })
        });
}

function findMapMax(map) {
    let max = 0;
    for(var [key, value] of map) {
        max = (!max || max < value) ? value : max;
    }
    return max;
}

function reset() {
    startDate = "";
    endDate = "";
    dates.clear();
    orders = [];
    maxValue = 0;
    totalSales = 0;
    document.getElementById("graph").innerHTML = "";
}

function setMonthTitle() {
    document.getElementById("month").innerText =
        dateIntToString.get(selectedMonth.getMonth()) + " " + selectedMonth.getFullYear();
}

function loadNextMonth() {
    selectedMonth = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() + 1);
    reset();
    generateDateParameters();
    setMonthTitle()
    generateGraph()
}

function loadPrevMonth() {
    selectedMonth = new Date(selectedMonth.getFullYear(), selectedMonth.getMonth() - 1);
    reset();
    generateDateParameters();
    setMonthTitle();
    generateGraph();
}