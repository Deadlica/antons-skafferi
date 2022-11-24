window.addEventListener('DOMContentLoaded', () => {

    setWeeks()
    setWeekDates()
});

function getMondayOfCurrentWeek() {
    const today = new Date();
    const first = today.getDate() - today.getDay() + 1;
    return new Date(today.setDate(first));
}

Date.prototype.getWeek = function () {
    let date = new Date(this.getTime());
    date.setHours(0, 0, 0, 0);
    // Thursday in current week decides the year.
    date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
    // January 4 is always in week 1.
    let week1 = new Date(date.getFullYear(), 0, 4);
    // Adjust to Thursday in week 1 and count number of weeks from date to week1.
    return 1 + Math.round(((date.getTime() - week1.getTime()) / 86400000 - 3 + (week1.getDay() + 6) % 7) / 7);
}

function setWeeks() {
    const today = new Date()
    document.getElementById("header_w1").innerHTML = `Vecka ${today.getWeek()}`
    document.getElementById("header_w2").innerHTML = `Vecka ${+today.getWeek() + 1}`
}

function setWeekDates() {
    let curr_date = getMondayOfCurrentWeek()
    let arr = document.querySelectorAll(".admin-day")
    for (let i = 0; i < 10; i++) {
        arr[i].firstElementChild.innerHTML = `${capitalizeFirstLetter(curr_date.toLocaleDateString("sv-SE", {weekday: 'long'}))} ${curr_date.getDate()} / ${curr_date.getMonth()}`
        curr_date.setDate(curr_date.getDate() + 1)
        if (i === 4) {
            curr_date.setDate(curr_date.getDate() + 2)
        }
    }
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}