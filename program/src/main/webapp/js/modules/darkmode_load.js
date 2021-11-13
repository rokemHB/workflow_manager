$(document).ready(function() {
    function getCookie(name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length == 2) return parts.pop().split(";").shift();
    }
    if (getCookie("darkmode") === "true") {
        // Darkmode
        DarkReader.enable({
            brightness: 100,
            contrast: 90,
            sepia: 5
        });
    } else {
        // Lightmode
        DarkReader.disable();
    }
})
