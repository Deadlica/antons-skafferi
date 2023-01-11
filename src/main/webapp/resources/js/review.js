





new_Comment = () => {

    document.getElementById("stars").addEventListener("click", async event => {
        console.log("!!!!" + event.target.id)
        document.getElementById("j_idt154:ranking").value = parseInt(event.target.id);
    });

}

new_Comment()
