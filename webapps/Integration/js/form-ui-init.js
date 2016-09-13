$(function() {
    // fix fox memorize checkbox, blyat'
    $(':checkbox.all').attr('checked', false);

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
});
