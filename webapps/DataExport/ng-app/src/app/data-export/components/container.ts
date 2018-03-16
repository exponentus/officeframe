import { Component } from '@angular/core';

@Component({
    selector: 'data-export-container',
    template: `<router-outlet></router-outlet>`,
    host: {
        '[class.module-container]': 'true'
    }
})
export class DataExportContainerComponent { }
