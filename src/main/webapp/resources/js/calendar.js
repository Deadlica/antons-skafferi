isLeapYear=(year)=>{
    return (year%4 ===0&& year % 100 !== 0 && year %400 !== 0)||(year%100 === 0 && year% 400===0)

}

getFebDays=(year)=>{

    if (isLeapYear(year))
        return 29
    else
        return 28
}

const month_names=['jan','feb','mar','apr', 'maj','jun','jul','aug','sep','oct','nov','dec']

let days=document.querySelector('.calender-days')
let yearpicker=document.querySelector('.year-book')


generateCalender=(month,year)=>{
    let currDate=new Date()
    days.innerHTML='';
    yearpicker.innerHTML=year

    let days_of_month =[31,getFebDays(year),31,30,31,30,31,31,30,31,30,31]
    let month_picker=document.querySelector('.month')
    month_picker.innerHTML="<p>"+month_names[month]+"</p>"

    console.log(month_names[month])
    let first_day= new Date( year,month, 1)

    firstDayStart=first_day.getDay()

    if(firstDayStart==0){
        firstDayStart=7
    }

    for (let index =1; index<days_of_month[month]+firstDayStart;index++){
        if (index>=firstDayStart){

        if((index - firstDayStart+1)==currDate2&&curr_month==month&&curr_year==year){
            days.innerHTML+= "<div id='"+(index- firstDayStart+1)+"' class ='selected week-day'>"+ (index - firstDayStart+1) +'</div>'
        }
        else if ((index - firstDayStart+1)==currDate.getDate()&&currDate.getMonth()==month&&currDate.getFullYear()==year){
                days.innerHTML+= "<div id='"+(index- firstDayStart+1)+"' class ='this-day week-day'>"+ (index - firstDayStart+1) +'</div>'
        }
        else {
            days.innerHTML+= "<div id='"+(index- firstDayStart+1)+"' class ='week-day'>"+ (index - firstDayStart+1)+"</div>"

        }
        }
        else {
            days.innerHTML+= '<div></div>'
        }
    }




}


document.querySelector('#prev-month').onclick=()=>{
    month--
    if (month<0) {
        month=11
        year= (year-1)
        yearpicker.innerHTML=year
    }
    generateCalender(month, year)
    loadNewDatesListners();
}

document.querySelector('#next-month').onclick=()=>{
    month++


if (month>11){
    month=0
    year= (year+1)
        yearpicker.innerHTML=year


    }
    generateCalender(month, year)
    loadNewDatesListners();
}

loadNewDatesListners=()=>{
    let prevTarget=undefined
    document.querySelectorAll('#days .week-day').forEach(day=>{
        day.addEventListener("click", async event => {
            event.currentTarget.classList.toggle("selected");

            console.log(temp2)
            temp2.readOnly=false
            temp2.value = year + "-" + (month + 1) + "-" + event.target.id
            temp2.readOnly=true

            document.getElementById('myform').submit();
            console.log(document.getElementById('myform:inputText'))


        });
    });
}

var temp2 = document.getElementById('myform:inputText')


temp2.readOnly=true


console.log(temp2.value)
const myArray = temp2.value.split("-", 3);

console.log(myArray)

let currDate2=parseInt(myArray[2])
let curr_month=parseInt(myArray[1])
var curr_year=parseInt(myArray[0])
curr_month=curr_month-1
let year=curr_year
let month=curr_month

generateCalender(curr_month,curr_year)
loadNewDatesListners();