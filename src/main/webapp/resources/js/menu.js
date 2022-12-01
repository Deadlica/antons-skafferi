document.addEventListener("DOMContentLoaded", function () {
    clicked_type_Menu("starter");
    document.getElementById("menu-days-list-id").querySelectorAll('li').forEach(item => {
        item.addEventListener('click', event => {
            clicked_type_Menu(item.id);
        })
    })

    document.getElementById("all_items_menu").querySelectorAll('.menu-item').forEach(item => {
        var disc_objekt = document.getElementById(item.id).getElementsByClassName("menu-item-description")
        var read_more = document.getElementById(item.id).getElementsByClassName("menu-item-ingr")
        disc_objekt[0].style.display = "none";
        read_more[0].style.display = "inline";

        item.addEventListener('click', event => {
            if (disc_objekt[0].style.display === "none") {
                disc_objekt[0].style.display = "block";
                read_more[0].style.display = "none";
            } else {
                disc_objekt[0].style.display = "none";
                read_more[0].style.display = "block";
            }
        })
    })
})

function clicked_type_Menu(Day) {
    console.log(Day)
    document.getElementById("menu-days-list-id").querySelectorAll('li').forEach(item => {
        console.log(item.id + "----")

        if (item.id == Day) {
            document.getElementById(item.id).style.color = "#7D5A3E";
            document.getElementById(item.id).style.fontWeight = "bold";
            document.getElementById(item.id).style.fontSize = "130%";
            console.log(item.id + "_item")
            document.getElementById(item.id + "_item").style.display = "block"
        } else {
            document.getElementById(item.id).style.color = "black";
            document.getElementById(item.id).style.fontWeight = "normal";
            document.getElementById(item.id).style.fontSize = "100%";
            console.log(document.getElementById(item.id + "_item"))
            document.getElementById(item.id + "_item").style.display = "none"

        }
    })
}


