function fill() {
    let row = $(this).find('td');
    $("input[name='id']").val(row[0].innerHTML);
    $("input[name='region']").val(row[1].innerHTML);
    $("input[name='city']").val(row[2].innerHTML);
    $("input[name='street']").val(row[3].innerHTML);
    $("input[name='number']").val(row[4].innerHTML);
    $("input[name='building']").val(row[5].innerHTML);
}

$(document).ready(function () {
    $(".go").each(function (index) {
        $(this).click(goToStore)
    });
});

function goToStore() {
    window.location.href = "store?id=" + $(this).closest('tr').find('td')[0].innerHTML;
}