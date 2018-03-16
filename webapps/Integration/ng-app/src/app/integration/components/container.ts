import { Component } from '@angular/core';

@Component({
    selector: 'integration-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class IntegrationContainerComponent { }
