

document.addEventListener("DOMContentLoaded", function () {
    clicked_type_Menu("starter_food");
    document.getElementById("menu-days-list-id").querySelectorAll('li').forEach(item => {
        item.addEventListener('click', event => {
            clicked_type_Menu(item.id);
        })
    })
    document.getElementById("menu_items").querySelectorAll('.menu-item').forEach(item => {
        var disc_objekt= document.getElementById(item.id).getElementsByClassName("menu-item-description")
        var read_more= document.getElementById(item.id).getElementsByClassName("menu-item-ingr")
        disc_objekt[0].style.display="none";
        read_more[0].style.display="inline";

        item.addEventListener('click', event => {
            if(disc_objekt[0].style.display==="none")
            {
                disc_objekt[0].style.display="inline";
                read_more[0].style.display="none";
            }
            else {
                disc_objekt[0].style.display="none";
                read_more[0].style.display="inline";
            }
        })
    })
})

function clicked_type_Menu(Day) {
    document.getElementById("menu-days-list-id").querySelectorAll('li').forEach(item => {
        if (item.id == Day) {
            document.getElementById(item.id).style.color = "#7D5A3E";
            document.getElementById(item.id).style.fontWeight = "bold";
            document.getElementById(item.id).style.fontSize = "130%";
        } else {
            document.getElementById(item.id).style.color = "black";
            document.getElementById(item.id).style.fontWeight = "normal";
            document.getElementById(item.id).style.fontSize = "100%";
        }
    })
}


