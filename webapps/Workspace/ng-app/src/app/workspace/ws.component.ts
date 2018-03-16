import { Component } from '@angular/core';

@Component({
    selector: 'workspace-container',
    template: `
        <router-outlet></router-outlet>
    `,
    host: {
        '[class.workspace]': 'true',
        '[class.module-container]': 'true'
    }
})
export class WorkspaceComponent { }
