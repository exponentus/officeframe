import { Routes } from '@angular/router';

import { ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { ReferenceContainerComponent } from './components/container';
import { ReferenceFormComponent } from './components/form';
import { ApprovalRouteFormComponent } from './components/approval-route/approval-route-form';

export const REFERENCE_ROUTES: Routes = [{
    path: 'Reference', component: ReferenceContainerComponent,
    children: [
        { path: '', redirectTo: 'countries', pathMatch: 'full' },
        { path: 'index', redirectTo: 'countries', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'approval-routes/:id', component: ApprovalRouteFormComponent },
        { path: ':viewId/:id', component: ReferenceFormComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
