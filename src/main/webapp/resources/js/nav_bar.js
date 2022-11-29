document.addEventListener("DOMContentLoaded", function () {


    document.getElementById("nav_bar_objekts").querySelectorAll('li').forEach(item => {
        item.addEventListener('click', event => {
            console.log(item.id);
            if ("book_bar"==item.id) {
                document.getElementById('booking_loc').scrollIntoView({behavior: 'smooth', block: 'center'});
            }
            else if ("menu_bar"==item.id) {
                document.getElementById('menu_loc').scrollIntoView({behavior: 'smooth', block: 'center'});
            }
            else if ("grade_bar"==item.id) {
                document.getElementById('grade_loc').scrollIntoView({behavior: 'smooth', block: 'center'});
            }
            else if ("event_bar"==item.id) {
                document.getElementById('event_loc').scrollIntoView({behavior: 'smooth', block: 'center'});
            }

        })
    })


});