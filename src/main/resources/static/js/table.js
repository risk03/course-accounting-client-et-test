$(document).ready(function () {
    $(".table-header th").each(function (index) {
            $(this).click(sortTable.bind(null, index));
        }
    );
    $(".table-content tr").each(function (index) {
        $(this).click(fill)
    });
    $(".left-trigger").each(function (index) {
        $(this).mouseenter(switchMenu)
    });
    $("#black").each(function (index) {
        $(this).mouseenter(switchMenu)
    })
});

function switchMenu() {
    if (switchMenu.menu_is_visible === null) {
        switchMenu.menu_is_visible = false;
    }
    let menu = document.getElementById("menu");
    if (switchMenu.menu_is_visible) {
        menu.style.display = "none";
        document.getElementById("black").style.display = "none";
        Array.from(document.getElementsByName("menubutton")).forEach(node => node.style.display = "none");
        switchMenu.menu_is_visible = false;
    } else {
        menu.style.display = "block";
        document.getElementById("black").style.display = "block";
        setTimeout(() => {
            Array.from(document.getElementsByName("menubutton")).forEach(node => node.style.display = "block");
        }, 90);
        switchMenu.menu_is_visible = true;
    }
}

function sortTable(n) {
    let table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = $(".table-content")[0];
    switching = true;
    dir = "asc";
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 0; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[n];
            y = rows[i + 1].getElementsByTagName("td")[n];
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

function insertParam(key, value) {
    key = encodeURI(key);
    value = encodeURI(value);
    var kvp = document.location.search.substr(1).split('&');
    var i = kvp.length;
    var x;
    while (i--) {
        x = kvp[i].split('=');
        if (x[0] == key) {
            x[1] = value;
            kvp[i] = x.join('=');
            break;
        }
    }
    if (i < 0) {
        kvp[kvp.length] = [key, value].join('=');
    }
    document.location.search = kvp.join('&');
}