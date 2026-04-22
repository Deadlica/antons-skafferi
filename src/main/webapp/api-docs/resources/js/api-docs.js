"use strict"


document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("entity_list").querySelectorAll("li").forEach(entity => {
        entity.addEventListener("click", event => {
            let entity_name = entity.id.substring(0, entity.id.length - 4);
            clickedEntity(entity.id);
        })
    })

    clickedEntity("employee_btn");
})

function clickedEntity(id) {
    document.getElementById("entity_list").querySelectorAll("li").forEach(entity => {
        if(entity.id == id) {
            document.getElementById(entity.id).style.color = "orange";
            document.getElementById(entity.id).style.fontWeight = "bold";
            document.getElementById(entity.id).style.opacity = "1";
            document.getElementById(entity.id).style.fontSize = "120%";
            document.getElementById(entity.id.substring(0, entity.id.length - 4)).style.display = "inline";
            window.scrollTo(0, 0);
        }
        else {
            document.getElementById(entity.id).style.color = "black";
            document.getElementById(entity.id).style.fontWeight = "normal";
            document.getElementById(entity.id).style.opacity = "0.6";
            document.getElementById(entity.id).style.fontSize = "100%";
            document.getElementById(entity.id.substring(0, entity.id.length - 4)).style.display = "none";
        }
    })
}
