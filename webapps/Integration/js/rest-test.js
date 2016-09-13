$(document).ready(function() {
    $('[data-action=do-get]').click(function() {
        var endPoint = $('[name=endpoint]').val();
        var dataType = 'json';
        var template = $('#tpl_rest_result_panel').clone();
        var $tpl = $(template.html().trim());
        $('#rest-result').prepend($tpl);
        $('.panel.open').removeClass('open');
        $tpl.addClass('open');
        $tpl.find('.js-remove').one('click', function() {
            $tpl.remove();
        });
        $tpl.find('.js-title').html(endPoint);

        $.ajax({
            type: 'get',
            url: endPoint,
            dataType: dataType,
        }).then(function(response) {
            if (response) {
                $('pre', $tpl).html(JSON.stringify(response, null, 2)).removeClass('prettyprinted');
                PR.prettyPrint();
            } else {
                $('pre', $tpl).html('empty response');
            }
        }, function(err) {
            console.log(err);
            $('pre', $tpl).html('Response status: ' + err.status);
        });
    });
});
