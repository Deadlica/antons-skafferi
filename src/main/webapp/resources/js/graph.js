"use strict";

const dateFormatMap = new Map();
const dates = new Map();

const data_url = "http://89.233.229.182:8080/antons-skafferi-db-1.0-SNAPSHOT/api/orders/sales?";
let startDate = "";
let endDate = "";
let orders = [];
let maxValue = 0;

document.addEventListener("DOMContentLoaded", function () {
    initMap();
    generateDateParameters();
    generateGraph();
});

function createBar(date, amount) {
    let item = document.createElement("div");
    let bar = document.createElement("span");
    let cost = document.createElement("span");
    let title = document.createElement("span");

    const height = amount / maxValue * 100;

    bar.style = `height: ${height}%`;

    if(amount > 0) {
        cost.innerText = amount.toString() + "kr";
        cost.setAttribute("class", "cost")
    }
    title.innerText = date.split("-").at(2);
    title.setAttribute("class", "title");

    bar.append(cost);
    bar.append(title);
    item.append(bar);

    return item;
}

async function generateGraph() {
    await fetchSales();
    getDaysInMonth();
    getSalesInMonth();
    maxValue = findMapMax(dates);
    console.log(Number(maxValue));
    const graph = document.getElementById("graph");
    dates.forEach(function (value, key) {
        graph.append(createBar(key, value));
    })
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
    let date = new Date();
    let firstDay = new Date(date.getFullYear(), date.getMonth() + whichMonth, day);
    const dateInfo = firstDay.toString().split(" ");
    const year = dateInfo.at(3);
    const month = dateFormatMap.get(dateInfo.at(1));
    date = dateInfo.at(2);
    return year + "-" + month + "-" + date;
}

function initMap() {
    dateFormatMap.set("Jan", "01");
    dateFormatMap.set("Feb", "02");
    dateFormatMap.set("Mar", "03");
    dateFormatMap.set("Apr", "04");
    dateFormatMap.set("May", "05");
    dateFormatMap.set("Jun", "06");
    dateFormatMap.set("Jul", "07");
    dateFormatMap.set("Aug", "08");
    dateFormatMap.set("Sep", "09");
    dateFormatMap.set("Oct", "10");
    dateFormatMap.set("Nov", "11");
    dateFormatMap.set("Dec", "12");
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
    dateFormatMap.clear();
    dates.clear();
    orders = [];
    maxValue = 0;
}