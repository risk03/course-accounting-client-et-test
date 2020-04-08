$(document).ready(function () {
    $(".table-header th").each(function (index) {
            $(this).click(sortTable.bind(null, index));
        }
    );
    $(".table-content tr").each(function(index){
        $(this).click(fill)
    });
    $(".left-trigger").each(function(index){
        $(this).mouseenter(switchMenu)
    });
    $("#black").each(function (index) {
        $(this).mouseenter(switchMenu)
    })
});

function switchMenu() {
    console.log(1);
    if (switchMenu.menu_is_visible === null) {
        switchMenu.menu_is_visible = false;
    }
    let menu = document.getElementById("menu");
    if (switchMenu.menu_is_visible) {
        menu.style.display = "none";
        document.getElementById("black").style.display = "none";
        document.getElementsByName("menubutton").forEach(node => node.style.display = "none");
        switchMenu.menu_is_visible = false;
    } else {
        menu.style.display = "block";
        document.getElementById("black").style.display = "block";
        setTimeout(() => {
            document.getElementsByName("menubutton").forEach(node => node.style.display = "block");
    }, 90);
        switchMenu.menu_is_visible = true;
    }
}


function fill(){
    let row = $(this).find('td');
    $("input[name='id']").val(row[0].innerHTML);
    $("input[name='surname']").val(row[1].innerHTML);
    $("input[name='forename']").val(row[2].innerHTML);
    $("input[name='patronymic']").val(row[3].innerHTML);
    $("select[name='role']").val(row[4].innerHTML);
    $("input[name='login']").val(row[5].innerHTML);
}

function sortTable(n) {
    let table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = $(".table-content")[0];
    // table = document.getElementById("table-content");
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