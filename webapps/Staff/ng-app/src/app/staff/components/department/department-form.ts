import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    AppService, ActionService,
    NotificationService, NbModalService,
    AbstractFormPage, REFERENCE_URL
} from '@nb/core';

import { STAFF_URL } from '../../staff.constants';
import { StaffService } from '../../staff.service';
import { Department } from '../../models';

@Component({
    selector: 'staff-department-form',
    templateUrl: './department-form.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class DepartmentFormComponent extends AbstractFormPage<Department> {

    REFERENCE_URL = REFERENCE_URL;
    STAFF_URL = STAFF_URL;
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
