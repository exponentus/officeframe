import { Component } from '@angular/core';

@Component({
    selector: 'calendar-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class CalendarContainerComponent { }
