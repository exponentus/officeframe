import { Component } from '@angular/core';

@Component({
    selector: 'reference-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class ReferenceContainerComponent { }
