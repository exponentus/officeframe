import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    IAction, IApiOutcome, IEntity, IFormSchema, IFormSchemaField, IFormSchemaEvent,
    AppService, ActionService, NotificationService, NbModalService,
    AbstractFormPage
} from '@nb/core';

import { ReferenceService } from '../reference.service';

@Component({
    selector: 'reference-form',
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
                    [payload]="payload"
                    (action)="onFormAction($event)"
                    (event)="onFormEvent($event)">
                </schema-form>
            </section>
            <footer class="content-footer">
                <div class="record-author" *ngIf="model.author">
                    <span>{{'author' | translate}}</span>
                    <span>{{model.author}}</span>
                    <br/>
                    <span>{{model.lastModifiedDate}}</span>
                </div>
            </footer>
        </form>
    `,
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class ReferenceFormComponent extends AbstractFormPage<IEntity> {

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
        public referenceService: ReferenceService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, referenceService);
    }

    // @Override
    onLoadDataSuccess(data: IApiOutcome) {
        super.onLoadDataSuccess(data);
        this.formSchema = this.referenceService.getFormSchema(this.model.kind);
        this.payload = data.payload;
        this.model.editable = this.actions.filter(it => it.customID === 'save_and_close').length > 0;
    }

    onFormAction(form: { action: IAction, field: IFormSchemaField, $event: HTMLElement }) {
        if (form.action.customID === 'CHANGE_NAMED_URL') {
            this.openActionConfirmModal(form.action, () => {
                form.action.hidden = true;
                form.field.disabled = false;
                form.$event.querySelector('input').focus();
            });
        }
    }

    onFormEvent(event: IFormSchemaEvent) {
        if (event.type === 'click') {
            let action: IAction = (<IAction>event.field.onClick);
            if (action && action.customID === 'CHANGE_NAMED_URL') {
                let inputEl = (<HTMLInputElement>event.$event.target);
                if (inputEl.disabled || inputEl.readOnly) {
                    this.openActionConfirmModal(action, () => {
                        action.hidden = true;
                        event.field.disabled = false;
                        inputEl.focus();
                    });
                }
            }
        }
    }

    openActionConfirmModal(action: IAction, callback: Function) {
        this.nbModalService.confirm(action.confirm.title, action.confirm.message, {
            label: action.caption,
            className: 'btn-primary ' + action.className,
            doAction: callback
        }).show();
    }
}
