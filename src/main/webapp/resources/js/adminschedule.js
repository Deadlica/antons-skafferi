window.addEventListener('DOMContentLoaded', (event) => {
    function setweek(){
        today = new Date();
        let text = document.getElementById("adminscheduleweek").innerHTML;
        document.getElementById("adminscheduleweek").innerHTML = text + " " + today.getWeekOfYear();
    }
    setweek()
});