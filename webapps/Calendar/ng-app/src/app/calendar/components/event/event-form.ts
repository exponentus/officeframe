import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { NotificationService } from '@nb/core';
import { NbModalService } from '@nb/core';
import { AppService, ActionService } from '@nb/core';
import { DATE_TIME_FORMAT } from '@nb/core';
import { AbstractFormPage } from '@nb/core';
import { tagStylerFn } from '@nb/core';
import { REFERENCE_URL } from '@nb/core';
import { CALENDAR_URL } from '../../constants';
import { CalendarService } from '../../calendar.service';
import { Event } from '../../models';

@Component({
    selector: 'calendar-event-form',
    templateUrl: './event-form.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class EventFormComponent extends AbstractFormPage<Event> {

    DATE_TIME_FORMAT = DATE_TIME_FORMAT;
    REFERENCE_URL = REFERENCE_URL;
    CALENDAR_URL = CALENDAR_URL;
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
