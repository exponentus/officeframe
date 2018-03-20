import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    IEntity,
    AppService, ActionService,
    NotificationService, NbModalService,
    AbstractFormPage
} from '@nb/core';

import { StaffService } from '../../staff.service';

@Component({
    selector: 'staff-simple-reference-form',
    templateUrl: './simple-reference-form.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class SimpleReferenceFormComponent extends AbstractFormPage<IEntity> {

    languages: any = {};

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        public ngxTranslate: TranslateService,
        public notifyService: NotificationService,
        public nbModalService: NbModalService,
        public appService: AppService,
        public actionService: ActionService,
        public calendarService: StaffService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, calendarService);
        this.languages = this.appService.languages;
    }
}
