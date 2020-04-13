function fill(){
    let row = $(this).find('td');
    $("input[name='id']").val(row[0].innerHTML);
    $("input[name='name']").val(row[1].innerHTML);
    $("input[name='sellingPrice']").val(row[2].innerHTML);
    $("input[name='description']").val(row[3].innerHTML);
}