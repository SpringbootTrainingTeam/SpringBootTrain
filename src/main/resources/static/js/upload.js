$("#multi").on("click", function () {
    $("#files").click();
});

$("#files").change(function (){
    let multiFiles = this.files;
    let fileNames = "";
    for (let i = 0; i < multiFiles.length; i++) {
        let file = multiFiles[i];
        if (window.FileReader){
            let reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = function () {
                let name = file.name;
                fileNames += name + ";";
                $("#fileHolders").val(fileNames);
            }
        }
    }
});