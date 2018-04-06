(function() {
    'use strict'

    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.getRegistrations().then(function(registrations) {
            for (var _registration of registrations) {
                _registration.unregister();
            }
        });
    } else {
        console.log('Service workers are not supported.')
    }
}());
