import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IAction } from '@nb/core';

@Component({
    selector: 'monit-charts-page',
    template: `
        <div class="content-actions">
            <nb-toolbar [actions]="actions" (action)="handleAction($event)"></nb-toolbar>
        </div>
        <header class="content-header">
            <h1 class="header-title">{{title | translate}}</h1>
        </header>
        <section class="content-body">
            <monit-records-count-chart-cmp></monit-records-count-chart-cmp>
        </section>
    `
})
export class ChartsPage {

    title = 'count_of_records';
    actions: IAction[] = [{
        caption: 'close',
        icon: 'fa fa-chevron-left',
        className: 'btn-back',
        customID: 'close'
    }, {
        caption: 'print',
        icon: 'fa fa-print',
        customID: 'print'
    }];

    constructor(
        private route: ActivatedRoute,
        private router: Router
    ) { }

    close() {
        this.router.navigate(['../'], { relativeTo: this.route });
    }

    handleAction(action: IAction) {
        if (action.customID === 'close') {
            this.close();
        } else if (action.customID === 'print') {
            window.print();
        }
    }
}
