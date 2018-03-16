import { Routes } from '@angular/router';

import { ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { StaffContainerComponent } from './components/container';
import { StaffFormComponent } from './components/form';

export const STAFF_ROUTES: Routes = [{
    path: 'Staff', component: StaffContainerComponent,
    children: [
        { path: '', redirectTo: 'organizations', pathMatch: 'full' },
        { path: 'index', redirectTo: 'organizations', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: ':viewId/:id', component: StaffFormComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
