import { Component, OnDestroy } from '@angular/core';

import { IApp, AppService } from '@nb/core';

@Component({
    selector: 'nb-ws-apps',
    template: `
        <nb-sign-in *ngIf="!appService.isLogged"></nb-sign-in>
        <section class="ws">
            <section class="ws-apps animation-slide-down" *ngIf="apps.length">
                <nb-app-list [apps]="apps" itemClass="ws-app"></nb-app-list>
            </section>
            <section class="ws-apps off animation-slide-down" *ngIf="!appService.isLogged && !apps.length">
                <div class="ws-app off"></div><div class="ws-app off"></div><div class="ws-app off"></div>
            </section>
        </section>
    `
})
export class WorkspaceAppsComponent {
    constructor(
        public appService: AppService
    ) { }

    get apps(): IApp[] {
        return this.appService.apps || [];
    }
}
