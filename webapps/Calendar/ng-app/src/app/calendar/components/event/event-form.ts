import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    AppService, ActionService,
    NotificationService, NbModalService,
    DATE_TIME_FORMAT, AbstractFormPage, tagStylerFn
} from '@nb/core';

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
