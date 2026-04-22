window.addEventListener('DOMContentLoaded', (event) => {
    init();
});

function init() {
    const month_names = ['jan', 'feb', 'mar', 'apr', 'maj', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec'];
    const isLeapYear = y => (y % 4 === 0 && y % 100 !== 0) || y % 400 === 0;
    const getFebDays = y => isLeapYear(y) ? 29 : 28;

    const input_text = document.getElementById('myform:inputText');
    if (!input_text) return;
    input_text.readOnly = true;

    const days = () => Array.from(document.querySelectorAll('#days .week-day'));
    const selectedParts = (input_text.value || '').split('-', 3);
    const yearpicker = document.querySelector('.year-book').innerHTML;
    const month_picker = document.querySelector('.month').innerHTML;
    const month = month_names.indexOf(month_picker.substring(0, 3));
    const year = parseInt(yearpicker);
    const selectedDay = parseInt(selectedParts[2]);
    const selectedMonth = parseInt(selectedParts[1]);

    generateCalender(month, year, selectedDay, selectedMonth, days());
    loadNewDatesListeners(month, year, days(), input_text);
}

function generateCalender(month, year, selectedDay, selectedMonth, cells) {
    const currDate = new Date();
    const days_of_month = [31, (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0 ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    let firstDayStart = new Date(year, month, 1).getDay();
    if (firstDayStart === 0) firstDayStart = 7;

    for (let index = 0; index < days_of_month[month] + firstDayStart - 1; index++) {
        const cell = cells[index];
        if (!cell) continue;
        if (index >= firstDayStart - 1) {
            const day = index - firstDayStart + 2;
            if (day === currDate.getDate() && currDate.getMonth() === month && currDate.getFullYear() === year) {
                cell.classList.add("this-day");
            }
            if (day === selectedDay && selectedMonth === month + 1 && currDate.getFullYear() === year) {
                cell.classList.add("selected");
            }
        } else {
            cell.innerHTML = "";
        }
    }
}

function loadNewDatesListeners(month, year, cells, input_text) {
    document.querySelectorAll('#days .notFull').forEach(day => {
        day.addEventListener("click", event => {
            document.querySelectorAll('#days .selected').forEach(el => el.classList.remove("selected"));
            event.currentTarget.classList.add("selected");
            const selectedMonth = String(month + 1).padStart(2, '0');
            const dayText = event.currentTarget.textContent.trim();
            const selectedDay = dayText.padStart(2, '0');
            input_text.readOnly = false;
            input_text.value = year + "-" + selectedMonth + "-" + selectedDay;
            input_text.readOnly = true;
            input_text.dispatchEvent(new Event('change', { bubbles: true }));
        });
    });

    // Auto-select today or first available day if nothing is selected
    if (!input_text.value || input_text.value === "0000-00-00") {
        const today = document.querySelector('#days .this-day.notFull');
        const firstAvailable = document.querySelector('#days .notFull');
        const target = today || firstAvailable;
        if (target) target.click();
    }
}

function changeMonth() {
    init();
}
