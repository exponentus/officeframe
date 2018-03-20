import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    AppService, ActionService,
    NotificationService, NbModalService,
    AbstractFormPage, REFERENCE_URL,
    Attachment
} from '@nb/core';

import { STAFF_URL } from '../../staff.constants';
import { StaffService } from '../../staff.service';
import { Employee } from '../../models';

@Component({
    selector: 'staff-employee-form',
    templateUrl: './employee-form.html',
    styleUrls: ['./employee-form.css'],
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class EmployeeFormComponent extends AbstractFormPage<Employee> {

    REFERENCE_URL = REFERENCE_URL;
    STAFF_URL = STAFF_URL;

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
    }

    computeAttachmentUrl(model: Employee, att: Attachment) {
        if (att.realFileName === 'no_avatar.png') {
            return '/Staff/img/nophoto.png';
        } else if (att.base64) {
            return att.base64;
        } else {
            return `${model.avatarURL}?t=${Date.now()}`;
        }
    }
}
