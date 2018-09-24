import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CalendarModule as ngCalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

import { NbCommonModule } from '@nb/core';
import { NbAclModule, NbEntityCreationDetailsModule } from '@nb/components';

import { CALENDAR_ROUTES } from './calendar.routes';
import { CalendarService } from './calendar.service';

import { CalendarContainerComponent } from './components/container';
import { CalendarPageComponent } from './components/calendar/calendar.page';
import { CalendarComponent } from './components/calendar/calendar';
import { EventFormComponent } from './components/event/event-form';
import { ReminderFormComponent } from './components/reminder/reminder-form';

@NgModule({
    declarations: [
        CalendarContainerComponent,
        CalendarPageComponent,
        CalendarComponent,
        EventFormComponent,
        ReminderFormComponent
    ],
    imports: [
        BrowserAnimationsModule,
        NbCommonModule,
        NbAclModule,
        NbEntityCreationDetailsModule,
        RouterModule.forChild(CALENDAR_ROUTES),
        ngCalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: adapterFactory
        })
    ],
    providers: [
        CalendarService
    ]
})
export class CalendarModule { }
