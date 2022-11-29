window.addEventListener('DOMContentLoaded', (event) => {
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
    function setWeek(week){
        today = new Date();
        week = today.getWeek() + week;
        if(week > 52){
            week = week - 52;
        }else if(week < week){
            week = week + 52;
        }

        if(week == today.getWeek()){
            document.getElementById("adminscheduleweek").style.color = "var(--joel_brown)";
            document.getElementById("adminscheduleweek").innerHTML = "Denna vecka (" + week + ")";
        }else{
            document.getElementById("adminscheduleweek").style.color = "black";
            document.getElementById("adminscheduleweek").innerHTML = "Vecka " + week;
        }
    }
    function setweekDates(week) {
        let curr_date = getMondayOfCurrentWeek()
        let arr = document.querySelectorAll(".admin-schedule")
        console.log(arr[0].childNodes)
        curr_date.setDate(curr_date.getDate() + 7 * week)

        for (let i = 0; i < 7; i++) {
            if(curr_date.getDate() == new Date().getDate() && i < 5){
                arr[i].firstElementChild.innerHTML = `Idag ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i].firstElementChild.style.color = "var(--joel_brown)";
                arr[i].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();
                arr[i + 5].firstElementChild.innerHTML = `Idag ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i + 5].firstElementChild.style.color = "var(--joel_brown)";
                arr[i + 5].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();

            }else if(i < 5){
                arr[i].firstElementChild.innerHTML = `${capitalizeFirstLetter(curr_date.toLocaleDateString("sv-SE", {weekday: 'long'}))} ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i].firstElementChild.style.color = "black";
                arr[i].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();
                arr[i + 5].firstElementChild.innerHTML = `${capitalizeFirstLetter(curr_date.toLocaleDateString("sv-SE", {weekday: 'long'}))} ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i + 5].firstElementChild.style.color = "black";
                arr[i + 5].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();
            }else if(curr_date.getDate() == new Date().getDate()){
                arr[i + 5].firstElementChild.innerHTML = `Idag ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i + 5].firstElementChild.style.color = "var(--joel_brown)";
                arr[i + 5].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();
            }else{
                arr[i + 5].firstElementChild.innerHTML = `${capitalizeFirstLetter(curr_date.toLocaleDateString("sv-SE", {weekday: 'long'}))} ${curr_date.getDate()} / ${curr_date.getMonth() + 1}`
                arr[i + 5].firstElementChild.style.color = "black";
                arr[i + 5].id = curr_date.getFullYear() + "-" + (curr_date.getMonth() + 1) + "-" + curr_date.getDate();
            }

            curr_date.setDate(curr_date.getDate() + 1)
        }
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
    let week = 0;
    setWeek(week)
    setweekDates(week)

    document.getElementById("increase_week").addEventListener("click", function(){
        week = week + 1;
        setweekDates(week)
        setWeek(week)
    })

    document.getElementById("decrease_week").addEventListener("click", function(){
        week = week - 1;
        setweekDates(week)
        setWeek(week)
    })
});

function getMondayOfCurrentWeek() {
    const today = new Date();
    const first = today.getDate() - today.getDay() + 1;
    return new Date(today.setDate(first));
}