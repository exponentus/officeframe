var knca = (function() {

    var wd = window.document,
        noty,
        initialized = false,
        isReady = false;

    function init() {
        if (initialized) {
            return;
        }

        if (!navigator.javaEnabled()) {
            nb.notify({
                type: 'error',
                message: nb.getText('java_disabled', 'Поддержка Java в браузере не включена! Включите или <a href=\"http://java.com/ru/download/\" target=\"blank\">установите Java</a> и вновь обратитесь к этой странице.')
            }).show(5000);

            log('java disabled');
        } else {
            insertApplet();
            initialized = true;
        }
    }

    function ready() {
        if (!initialized) {
            log('uninitialized; call knca.init()');
            return false;
        }

        if (isReady) {
            return;
        }

        isReady = true;
        nb.uiUnblock();
        noty && noty.remove();
        noty = null;

        log('is ready');
    }

    function log(msg) {
        console.log('knca > ', msg);
    }

    function insertApplet() {
        if (wd.getElementById('KncaApplet')) {
            return;
        }

        nb.uiBlock();

        noty = nb.notify({
            type: 'process',
            message: 'Подождите, идет загрузка Java-апплета...'
        }).show();

        var htm = [];
        htm.push('<applet width="1" height="1"');
        htm.push(' codebase="."');
        htm.push(' code="kz.gov.pki.knca.applet.MainApplet"');
        htm.push(' archive="knca_applet.jar"');
        htm.push(' type="application/x-java-applet"');
        htm.push(' mayscript="true"');
        htm.push(' style="display:none"');
        htm.push(' id="KncaApplet" name="KncaApplet">');
        htm.push('<param name="code" value="kz.gov.pki.knca.applet.MainApplet">');
        htm.push('<param name="archive" value="knca_applet.jar">');
        htm.push('<param name="mayscript" value="true">');
        htm.push('<param name="scriptable" value="true">');
        htm.push('<param name="language" value="ru">');
        htm.push('<param name="separate_jvm" value="true">');
        htm.push('</applet>');

        var d = wd.createElement('div');
        d.style.display = 'none;'
        d.innerHTML = htm.join('');
        wd.getElementsByTagName('body')[0].appendChild(d);
    }

    return {
        init: init,
        ready: ready
    }
})();

function AppletIsReady() {
    knca.ready();
}
