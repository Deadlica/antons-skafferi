window.addEventListener('DOMContentLoaded', (event) => {
    function getMondayOfCurrentWeek() {
        const today = new Date();
        const first = today.getDate() - today.getDay() + 1;

        const monday = new Date(today.setDate(first));
        return monday;
    }

    function create_lunch_week(week) {
        let curr_week
        let monday_date = getMondayOfCurrentWeek()
        let i = 0
        let cap = 5
        if (week == 1) {
            curr_week = document.getElementById("week1")
        } else {
            curr_week = document.getElementById("week2")
            monday_date.setDate(monday_date.getDate() + 7)
            i = 5
            cap = 10
        }
        curr_week.innerHTML = "";
        let curr_date = monday_date
        for (; i < cap; i++) {
            curr_date.setDate(curr_date.getDate() + 1)
            curr_week.innerHTML += "" + `
                <div class="admin-day">
                    <h3>${curr_date.getDate() + "/" + curr_date.getMonth()}</h3>
                    <hr/>
                    <div class="admin-daily-lunch">
                        <div class="admin-added-item">
                            <p>Kryddigt Renskav</p>
                            <span>&#8722;</span>
                        </div>
                    </div>
                    <form action="">
                        <div class="admin-add-lunch">
                            <label for="${"food_item" + i}">Ny rätt</label>
                            <select name="${"food_item" + i}"id="food_item">
                                <option>Kryddigt Renskav</option>
                                <option>Krämig Älgfärs</option>
                            </select>
                        </div>
                        <div class="admin-add-lunch">
                            <label for="${"price" + i}">Pris</label>
                            <input type="number" id="${"price" + i}" size='3' value="90"/>
                        </div>
                        <input type="submit" value="Spara"/>
                    </form>
                </div>
            `
        }
    }

    create_lunch_week(1)
    create_lunch_week(2)


});
