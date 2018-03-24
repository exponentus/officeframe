import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CalendarModule as ngCalendarModule } from 'angular-calendar';

import { NbCommonModule, NbAclModule } from '@nb/core';

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
        NbCommonModule,
        NbAclModule,
        RouterModule.forChild(CALENDAR_ROUTES),
        ngCalendarModule.forRoot()
    ],
    providers: [
        CalendarService
    ]
})
export class CalendarModule { }
