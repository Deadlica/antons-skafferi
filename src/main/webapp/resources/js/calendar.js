isLeapYear=(year)=>{
    return (year%4 ===0&& year % 100 !== 0 && year %400 !== 0)||(year%100 === 0 && year% 400===0)

}

getFebDays=(year)=>{

    if (isLeapYear(year))
        return 29
    else
        return 28
}

let calender= document.querySelector('.calender')

const month_names=['jan','feb','mar','apr', 'maj','jun','jul','aug','sep','oct','nov','dec']

let days=document.querySelector('.calender-days')


generateCalender=(month,year)=>{
    let currDate=new Date()
    console.log('----------'+currDate.getUTCDate())
    days.innerHTML='';
    let days_of_month =[31,getFebDays(year),31,30,31,30,31,31,30,31,30,31]
    let month_picker=document.querySelector('.month22')
    month_picker.innerHTML="<p>"+month_names[month]+"</p>"

    console.log(month_names[month])
    let first_day= new Date( year,month, 1)
    console.log(first_day.getDay()+'-----')

    firstDayStart=first_day.getDay()
    if(firstDayStart==0){
        firstDayStart=7
    }

    for (let index =0; index<days_of_month[month]+firstDayStart-1;index++){

        console.log(index+'--------'+firstDayStart)

        if (index>=firstDayStart-1){
        if ((index - firstDayStart+2)==currDate.getUTCDate()&&currDate.getMonth()==month&&currDate.getFullYear()==year){
            days.innerHTML+= "<p class ='this-day'>"+ (index - firstDayStart+2) +'</p>'
        }
        else {
            days.innerHTML+= '<p>'+ (index - firstDayStart+2) +'</p>'

        }
        }
        else {
            days.innerHTML+= '<span></span>'
        }
    }




}
let currDate=new Date()
let curr_month=currDate.getMonth()
let curr_year=currDate.getFullYear()
console.log(curr_month+'--------'+curr_year)
generateCalender(curr_month,curr_year)

let yearpicker=document.querySelector('.year-book')

document.querySelector('#prev-month').onclick=()=>{
    curr_month--
    if (curr_month>=0) {
        generateCalender(curr_month, curr_year)
    }
    else {
        curr_month=11
        curr_year--
        yearpicker.innerHTML=curr_year

        generateCalender(curr_month, curr_year)

    }
}

document.querySelector('#next-month').onclick=()=>{
    curr_month++

    if (curr_month<=11) {
        generateCalender(curr_month, curr_year)
    }
else {
        curr_month=0
        curr_year++
        yearpicker.innerHTML=curr_year

        generateCalender(curr_month, curr_year)

    }
}


