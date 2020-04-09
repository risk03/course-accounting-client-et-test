function fill(){
    let row = $(this).find('td');
    $("input[name='id']").val(row[0].innerHTML);
    $("input[name='surname']").val(row[1].innerHTML);
    $("input[name='forename']").val(row[2].innerHTML);
    $("input[name='patronymic']").val(row[3].innerHTML);
    $("select[name='role']").val(row[4].innerHTML);
    $("input[name='login']").val(row[5].innerHTML);
}