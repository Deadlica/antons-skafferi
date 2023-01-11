window.addEventListener('DOMContentLoaded', (event) => {

    isLeapYear = (year) => {
        return (year % 4 === 0 && year % 100 !== 0 && year % 400 !== 0) || (year % 100 === 0 && year % 400 === 0)
    }

    getFebDays = (year) => {

        if (isLeapYear(year))
            return 29
        else
            return 28
    }

    month_names = ['jan', 'feb', 'mar', 'apr', 'maj', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec']


    generateCalender = (month, year,currDate2,curr_month) => {
        let currDate = new Date()

        let days_of_month = [31, getFebDays(year), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

        let first_day = new Date(year, month, 1)
        let last_day = new Date(year, month + 1, 1)
        firstDayStart = first_day.getDay()

        if (firstDayStart == 0) {
            firstDayStart = 7
        }
        console.log(curr_month + "----" + month)
        for (let index = 0; index < days_of_month[month] + firstDayStart; index++) {
            if (index >= firstDayStart - 1) {
                if ((index - firstDayStart + 2) == currDate.getDate() && currDate.getMonth() == month && currDate.getFullYear() == year) {
                    document.getElementById("myform:j_idt111:" + (index) + ":item2").classList.add("this-day")
                }
                if ((index - firstDayStart + 2) == currDate2 && curr_month == month + 1 && currDate.getFullYear() == year) {
                    document.getElementById("myform:j_idt111:" + (index) + ":item2").classList.add("selected")
                    prevTarget = document.getElementById("myform:j_idt110:" + (index) + ":item2")
                }
            } else {
                document.getElementById("myform:j_idt111:" + (index) + ":item2").innerHTML = "";
            }
        }
    }
    let prevTarget = undefined




    var input_text = document.getElementById('myform:inputText')


    input_text.readOnly = true


     myArray = input_text.value.split("-", 3);

    let yearpicker = document.querySelector('.year-book').innerHTML
    let month_picker = document.querySelector('.month').innerHTML
    console.log("---" + month_names.indexOf(month_picker.substring(0, 3)) + "-" + parseInt(yearpicker))


    var curr_year = parseInt(myArray[0])

    generateCalender(month_names.indexOf(month_picker.substring(0, 3)), parseInt(yearpicker),parseInt(myArray[2]),parseInt(myArray[1]))
    loadNewDatesListners(month_names.indexOf(month_picker.substring(0, 3)), parseInt(yearpicker),parseInt(myArray[2]),parseInt(myArray[1]));
})
loadNewDatesListners = (month, year) => {
    let prevTarget = undefined
    var input_text = document.getElementById('myform:inputText')
    document.querySelectorAll('#days .notFull').forEach(day => {
        day.addEventListener("click", async event => {
            document.querySelectorAll('#days .selected').forEach(Removeselected => {
                Removeselected.classList.remove("selected")
            });
            event.currentTarget.classList.toggle("selected");
            prevTarget = event.currentTarget
            input_text.readOnly = false

            var selectetMonth = month+ 1;
            var selectetday = document.getElementById(event.target.id).innerHTML;

            if (selectetMonth < 10) {
                var tempSelectetMonth = "0" + String(selectetMonth);
            } else {
                var tempSelectetMonth = String(selectetMonth);

            }
            if (selectetday.length == 1) {
                var tempSelectetday = "0" + selectetday;
            } else {
                var tempSelectetday = selectetday;

            }
            console.log(tempSelectetMonth + "!!!!!!!!" + tempSelectetday)

            input_text.value = parseInt(year) + "-" + (tempSelectetMonth) + "-" + tempSelectetday
            input_text.readOnly = true

        });
    });
}

changeMonth = () =>
{

    var input_text = document.getElementById('myform:inputText')
    input_text.readOnly = true
    myArray = input_text.value.split("-", 3);


    var curr_year = parseInt(myArray[0])
    console.log(parseInt(myArray[2])+"!!!!!!!!!!!!!"+parseInt(myArray[1]))

    let yearpicker = document.querySelector('.year-book').innerHTML
    let month_picker = document.querySelector('.month').innerHTML
    generateCalender(month_names.indexOf(month_picker.substring(0, 3)), parseInt(yearpicker),parseInt(myArray[2]),parseInt(myArray[1]))
    loadNewDatesListners(month_names.indexOf(month_picker.substring(0, 3)), parseInt(yearpicker));
}
