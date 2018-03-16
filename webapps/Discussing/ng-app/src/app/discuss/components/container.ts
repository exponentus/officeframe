import { Component, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'discuss-container',
    template: `
        <div class="page">
            <router-outlet></router-outlet>
        </div>
    `,
    styleUrls: ['../styles/style.css'],
    host: {
        '[class.module-container]': 'true'
    },
    encapsulation: ViewEncapsulation.None
})
export class DiscussContainerComponent { }
