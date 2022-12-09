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


generateCalender=(month,year)=>{
    let currDate=new Date()

    let days_of_month =[31,getFebDays(year),31,30,31,30,31,31,30,31,30,31]

    let first_day= new Date( year,month, 1)
    let last_day= new Date( year,month+1, 1)
    firstDayStart=first_day.getDay()

    if(firstDayStart==0){
        firstDayStart=7
    }
    console.log(curr_month+"----"+month)
    for (let index =0; index<days_of_month[month]+firstDayStart;index++){
        if (index>=firstDayStart-1){
            if ((index - firstDayStart+2)==currDate.getDate()&&currDate.getMonth()==month&&currDate.getFullYear()==year)
            {
                document.getElementById("myform:j_idt110:"+(index)+":item2").classList.add("this-day")
            }
            if((index - firstDayStart+2)==currDate2 && curr_month==month+1&&currDate.getFullYear()==year) {
                document.getElementById("myform:j_idt110:"+(index)+":item2").classList.add("selected")
                prevTarget=document.getElementById("myform:j_idt19:"+(index)+":item2")
            }
        }
        else {
            document.getElementById("myform:j_idt110:"+(index)+":item2").innerHTML="";
        }
    }




}
let prevTarget=undefined

loadNewDatesListners=()=>{
    document.querySelectorAll('#days .notFull').forEach(day=>{
        day.addEventListener("click", async event => {
            if (prevTarget!=undefined){
                prevTarget.classList.remove("selected")
            }
            event.currentTarget.classList.toggle("selected");
            prevTarget=event.currentTarget
            temp2.readOnly=false

            var selectetMonth=(month_names.indexOf(month_picker.substring(0, 3)) + 1);
            var selectetday=document.getElementById(event.target.id).innerHTML;

            if (selectetMonth<10){
                var tempSelectetMonth= "0"+String(selectetMonth);
            }
            else {
                var tempSelectetMonth= String(selectetMonth);

            }
            if (selectetday.length==1){
                var tempSelectetday= "0"+selectetday;
            }
            else {
                var tempSelectetday= selectetday;

            }
            console.log(tempSelectetMonth+"!!!!!!!!"+tempSelectetday)

            temp2.value = parseInt(yearpicker) + "-" + (tempSelectetMonth) + "-" + tempSelectetday
            temp2.readOnly=true
            console.log("------"+document.getElementById(event.target.id).innerHTML)

            // document.getElementById('myform').submit();


        });
    });
}

var temp2 = document.getElementById('myform:inputText')


temp2.readOnly=true


const myArray = temp2.value.split("-", 3);

let yearpicker=document.querySelector('.year-book').innerHTML
let month_picker=document.querySelector('.month').innerHTML
console.log("---"+month_names.indexOf(month_picker.substring(0, 3)) +"-"+ parseInt(yearpicker))





let currDate2=parseInt(myArray[2])
let curr_month=parseInt(myArray[1])
var curr_year=parseInt(myArray[0])

generateCalender(month_names.indexOf(month_picker.substring(0, 3)),parseInt(yearpicker))
loadNewDatesListners();