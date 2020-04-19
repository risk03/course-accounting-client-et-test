function fill() {
    let row = $(this).find('td');
    $("select[name='product']").val(row[0].innerHTML);
    $("input[name='quantity']").val(row[1].innerHTML.replace(/,/g, '.'));
}