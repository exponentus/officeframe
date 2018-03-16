import { Component } from '@angular/core';

@Component({
    selector: 'staff-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class StaffContainerComponent { }
