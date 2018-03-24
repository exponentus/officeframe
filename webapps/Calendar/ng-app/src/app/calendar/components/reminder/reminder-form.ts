import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    AppService, ActionService,
    NotificationService, NbModalService,
    AbstractFormPage, tagStylerFn
} from '@nb/core';

import { CalendarService } from '../../calendar.service';
import { Reminder } from '../../models';

@Component({
    selector: 'calendar-reminder-form',
    templateUrl: './reminder-form.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class ReminderFormComponent extends AbstractFormPage<Reminder> {

    tagStylerFn = tagStylerFn;

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        public ngxTranslate: TranslateService,
        public notifyService: NotificationService,
        public nbModalService: NbModalService,
        public appService: AppService,
        public actionService: ActionService,
        public calendarService: CalendarService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, calendarService);
    }
}
