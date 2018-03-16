import { Component } from '@angular/core';

@Component({
    selector: 'of-monitoring-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class MonitoringContainerComponent { }
