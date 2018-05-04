import { Component, ChangeDetectionStrategy, ChangeDetectorRef, ViewEncapsulation } from '@angular/core';
import { CalendarDateFormatter, CalendarNativeDateFormatter } from 'angular-calendar';
import { Observable } from 'rxjs/Observable';
import { CalendarEvent } from 'angular-calendar';
import { map } from 'rxjs/operators/map';
import * as Moment from 'moment';

import { AppService, DATE_TIME_FORMAT } from '@nb/core';

import { Event } from '../../models';
import { CalendarService } from '../../calendar.service';

type CalendarUnitType = 'month' | 'week';

@Component({
    selector: 'calendar-widget',
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    templateUrl: './calendar.html',
    styleUrls: ['./angular-calendar.css', './calendar.css'],
    providers: [{
        provide: CalendarDateFormatter,
        useClass: CalendarNativeDateFormatter
    }]
})
export class CalendarComponent {
    view: CalendarUnitType = 'month';
    viewDate: Date = new Date();
    activeDayIsOpen: boolean = false;
    events$: Observable<Array<CalendarEvent<{ event: Event }>>>;

    event: Event;
    dateYear: number;

    get locale() {
        return this.appService.langAltCode;
    }

    constructor(
        private ref: ChangeDetectorRef,
        private appService: AppService,
        private calendarService: CalendarService
    ) { }

    ngOnInit(): void {
        this.fetchEvents();
        this.ref.markForCheck();
    }

    onChangeView(view: CalendarUnitType) {
        this.view = view;
        this.fetchEvents();
    }

    onViewDateChange(activeDayIsOpen: boolean = false) {
        this.activeDayIsOpen = activeDayIsOpen;
        this.fetchEvents();
    }

    onDayClicked({ date, events }: { date: Date; events: Array<CalendarEvent<{ event: Event }>>; }): void {
        if (date.getMonth() === this.viewDate.getMonth()) {
            if ((date.getDay() === this.viewDate.getDay() && this.activeDayIsOpen) || events.length === 0) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
                this.viewDate = date;
            }
        }
    }

    onEventClick(event: CalendarEvent) {
        this.event = event.meta;
    }

    fetchEvents(): void {
        let params = { eventStart: '', eventEnd: '', limit: 0 };
        let md = Moment(this.viewDate);
        params.eventStart = md.startOf(this.view).format(DATE_TIME_FORMAT);
        params.eventEnd = md.endOf(this.view).format(DATE_TIME_FORMAT);
        this.dateYear = md.year();

        this.events$ = this.calendarService
            .fetchCalendarEvents(params)
            .pipe(map(data => {
                return (data.payload.viewpage.result || []).map(event => {
                    //
                    if (event.relatedURL) {
                        let module = event.relatedURL.split('/')[1];
                        event.relatedURL = '/' + module + '/#' + event.relatedURL;
                    }
                    //
                    return {
                        title: event.title,
                        start: Moment(event.eventTime, DATE_TIME_FORMAT),
                        color: {
                            primary: '#e3bc08',
                            secondary: '#FDF1BA'
                        },
                        meta: event
                    };
                });
            }));
    }
}
