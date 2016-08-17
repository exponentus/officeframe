$(function() {
    $.datepicker.setDefaults($.datepicker.regional['ru']);
    if(sessionStorage.getItem("organization")){
        $("select[name=organization]").html(sessionStorage.getItem("organization"));
        $("select[name=organization] option").attr("selected","true");
        $("select[name=organization] option:first").remove();
        sessionStorage.removeItem("organization");
        $("select[name=organization]").next("span").find(".select2-selection__rendered").text($("select[name=organization] option").text())
    }
    if(sessionStorage.getItem("department")){
        $("select[name=department]").html(sessionStorage.getItem("department"));
        $("select[name=department] option").attr("selected","true");
        $("select[name=department] option:first").remove();
        sessionStorage.removeItem("department");
        $("select[name=organization]").next("span").find(".select2-selection__rendered").text($("select[name=department] option").text())
    }
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

    $('input[name=bin]').on("input propertychange" ,function(){
       if($(this).val().length == 0){
           $(this).removeAttr("required").removeClass("required");
           $(this).removeClass("required");
           $(this).next("div").remove();
       }
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
        nb.xhrDelete(location.href + '&docid=' + docids.join('&docid=')).then(function() {
            location.reload();
        });
    });

    $('.delete-photo').click(function(event) {
        event.preventDefault();
        $.get("p?id=employee-form&docid="+$(this).data("docid")+"&fsid="+$(this).data("fsid")+"&avatar=0", function() {
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

    var theme = localStorage.getItem('theme');
    if (theme) {
        $('body').addClass(theme);
    }

    // toogle user registration fields
    $('#reguser').on('change', function() {
        var fieldset = $(this).parents('fieldset');
        var $regfields = $('[name=login], [name=email], [type=password]', fieldset);
        $regfields.prop('disabled', !this.checked);
    });

    $('input[name=pwd], input[name=pwd_confirm]').on('change', function() {
        var $inputs = $('input[type=password]');
        if ($('input[name=pwd]').val() != $('input[name=pwd_confirm]').val()) {
            $inputs.addClass('invalid').prop('title', 'Поля пароль и повтор пароля не совпадают');
        } else {
            $inputs.removeClass('invalid').prop('title', '');
        }
    });
});
