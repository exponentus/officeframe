import { Routes } from '@angular/router';

import { ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { CalendarContainerComponent } from './components/container';
import { CalendarPageComponent } from './components/calendar/calendar.page';
import { EventFormComponent } from './components/event/event-form';
import { ReminderFormComponent } from './components/reminder/reminder-form';

export const CALENDAR_ROUTES: Routes = [{
    path: 'Calendar', component: CalendarContainerComponent,
    children: [
        { path: '', redirectTo: 'events', pathMatch: 'full' },
        { path: 'index', redirectTo: 'events', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'calendar', component: CalendarPageComponent },
        { path: 'events/:id', component: EventFormComponent },
        { path: 'reminders/:id', component: ReminderFormComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
