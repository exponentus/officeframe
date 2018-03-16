import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    IApiOutcome, IEntity, IFormSchema,
    AppService, ActionService,
    AbstractFormPage,
    NotificationService,
    NbModalService
} from '@nb/core';

import { StaffService } from '../staff.service';

@Component({
    selector: 'staff-form',
    template: `
        <form class="form" *ngIf="isReady">
            <div class="content-actions">
                <nb-toolbar [actions]="actions" (action)="onAction($event)"></nb-toolbar>
            </div>
            <header class="content-header">
                <h1 class="header-title">{{title | translate}}</h1>
            </header>
            <section class="content-body">
                <schema-form
                    [(model)]="model"
                    [fsId]="fsId"
                    [errors]="errors"
                    [schema]="formSchema"
                    [payload]="payload">
                </schema-form>
            </section>
            <footer class="content-footer">
                <div class="record-author" *ngIf="model.author">
                    <span>{{'author' | translate}}</span>
                    <span>{{model.author.name}}</span>
                    <span>{{model.regDate}}</span>
                </div>
            </footer>
        </form>
    `,
    styleUrls: ['../styles/profile.css'],
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class StaffFormComponent extends AbstractFormPage<IEntity> {

    formSchema: IFormSchema[] = [];
    payload: any = {};

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        public ngxTranslate: TranslateService,
        public notifyService: NotificationService,
        public nbModalService: NbModalService,
        public appService: AppService,
        public actionService: ActionService,
        public staffService: StaffService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, staffService);
    }

    // @Override
    loadDataSuccess(data: IApiOutcome) {
        super.loadDataSuccess(data);
        this.formSchema = this.staffService.getFormSchema(this.model.kind);
        this.payload = data.payload;
        this.model.editable = this.actions.filter(it => it.customID === 'save_and_close').length > 0;
    }
}
