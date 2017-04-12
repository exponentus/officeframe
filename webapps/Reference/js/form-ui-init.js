$(function() {
    $.datepicker.setDefaults($.datepicker.regional['ru']);
    // fix fox memorize checkbox, blyat'
    $(':checkbox.all').attr('checked', false);


    $('input[type=number]').each(function() {
        $(this).attr({
            'type': 'text',
            'data-type': 'number'
        }).numericField();
    });

    $('input[type=date]').each(function() {
        $(this).attr({
            'type': 'text',
            'data-type': 'date',
            /*'readonly': 'readonly',*/
        }).datepicker({ dateFormat: nb.options.dateFormat });
    });

    // init action
    $('[data-action=save_and_close]').click(function(event) {
        event.preventDefault();
        nb.submitForm(nb.getForm(this));
    });

    $('[data-action=delete_document]').click(function(event) {
        event.preventDefault();

        var docids = nb.getSelectedEntityIDs('docid');
        if (!docids.length) {
            return;
        }
        var form_name = $("#entity").val()+ "-form";
       nb.xhrDelete(location.href + '&docid=' + docids.join('&docid=')+'&form='+ form_name).then(function() {
            location.reload();
        });
    });
    
    $('[data-action=delete_document]').click(function(event) {
        event.preventDefault();

        var docids = nb.getSelectedEntityIDs('docid');
        if (!docids.length) {
            return;
        }
        var form_name = $("#entity").val()+ "-form";
       nb.xhrDelete(location.href + '&docid=' + docids.join('&docid=')+'&form='+ form_name).then(function() {
            location.reload();
        });
    });

    $('[data-action=delete_document]').attr('disabled', true);
    $(':checkbox').bind('change', function() {
        var countChecked = $('[name=docid]:checked').length;
        $('[data-action=delete_document]').attr('disabled', countChecked === 0);
    });

    $('[name=docid]:checked').attr('checked', false);

    // disable fieldset
    $('form[data-edit=false] .fieldset').attr('disabled', true);

    // toggle theme
    $('[data-toggle-theme]').click(function() {
        var themeName = $(this).data('toggle-theme');
        if ($('body').hasClass('theme1')) {
            $('body').removeClass(themeName);
            localStorage.setItem('theme', '');
        } else {
            $('body').addClass(themeName);
            localStorage.setItem('theme', themeName);
        }
    });
    if($("#custom_category").prop("checked")){
        if($("input#custom_category_input").val().length !=0){
            $("#custom_category").val($("input#custom_category_input").val())
        }else{
            $("#custom_category").prop("checked","false")
        }
    }
    $('input#custom_category_input').focus(function() {
        $("#custom_category").prop("checked","checked")
    }).on('input',function(){
        $("#custom_category").val($(this).val());
    });
    var theme = localStorage.getItem('theme');
    if (theme) {
        $('body').addClass(theme);
    }
});
