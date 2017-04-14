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
    $('[data-action=delete_route_block]').click(function() {
        var checkbox = $("input[name=route_block_chbox][type=checkbox]:checked");
        checkbox.parent().parent().parent().parent().remove();
        if(checkbox.length == 0){
            alert("Нет выбранных блоков для удаления")
        }
    });
    $('[data-action=add_route_block]').click(function() {
        var $route_block_blank = $(".route_block_blank").clone();
        $route_block_blank.removeClass("route_block_blank");
        $route_block_blank.find("#approvers_controls").append($('<select name="approvers" class="span7"/>'));
        var options_type="";
        $(".options_type").each(function(indx, element){
            options_type +="<option value='"+$(this).val()+"'>"+$(this).val()+"</option>"
        });
        $route_block_blank.find("#type_controls").append($('<select name="type" class="span6">' +
            options_type+
            '</select>'));
        var fieldset = $("<fildset class='fieldset'><legend class='legend'/></fildset>");
        $(fieldset).append($route_block_blank);

        $($route_block_blank).css("visibility","visible");
        $("#route_blocks_wrapper").append(fieldset);
        var select_list=$(fieldset).find("select");
        select_list.each(function() {
            var appSelectOptions = nbApp.selectOptions && nbApp.selectOptions[this.name];
            if (appSelectOptions) {
                var $select2El = $(this).select2(nb.getSelectOptions(appSelectOptions));

                $select2El.on('select2:unselecting', function(e) {
                    $(this).data('unselecting', true);

                    if (typeof(appSelectOptions.onSelect) === 'function') {
                        appSelectOptions.onSelect(e);
                    }
                }).on('select2:opening', function(e) {
                    if ($(this).data('unselecting')) {
                        $(this).removeData('unselecting');
                        e.preventDefault();
                    }
                });

                if (typeof(appSelectOptions.onSelect) === 'function') {
                    $select2El.on('select2:select', function(e) {
                        if (appSelectOptions.onSelect) {
                            appSelectOptions.onSelect(e);
                        }
                    });
                }
            } else {
                if (nb.isMobile()) {
                    if (this.multiple) {
                        $(this).select2({
                            minimumResultsForSearch: 20
                        }).on('select2:unselecting', function() {
                            $(this).data('unselecting', true);
                        }).on('select2:opening', function(e) {
                            if ($(this).data('unselecting')) {
                                $(this).removeData('unselecting');
                                e.preventDefault();
                            }
                        });
                    }
                } else {
                    $(this).select2({
                        minimumResultsForSearch: 20
                    }).on('select2:unselecting', function() {
                        $(this).data('unselecting', true);
                    }).on('select2:opening', function(e) {
                        if ($(this).data('unselecting')) {
                            $(this).removeData('unselecting');
                            e.preventDefault();
                        }
                    });
                }
            }
        });

        // need dummy input if no select value
        $('select[name]').on('change', function() {
            if ($(this).val()) {
                $('[data-role=dummy-select][name=' + this.name + ']', $(this).parent()).remove();
            } else {
                $('<input type=hidden data-role=dummy-select name=' + this.name + ' value="">').appendTo($(this).parent());
            }
        });
    });
});
