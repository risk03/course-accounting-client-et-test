function fill() {
    let row = $(this).find('td');
    $("input[name='id']").val(row[0].innerHTML);
    $("select[name='store']").val(row[1].innerHTML);
    $("select[name='user']").val(row[2].innerHTML);
    $("input[name='date']").val(row[3].innerHTML.replace(' ', 'T'));
}

$(document).ready(function () {
    $(".go").each(function () {
        $(this).click(goToTransaction)
    });
    $("input[name='date']").val(new Date().toJSON().slice(0,19));
});

function goToTransaction() {
    window.location.href = "transaction?id=" + $(this).closest('tr').find('td')[0].innerHTML;
}