import { Routes } from '@angular/router';

import { ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { StaffContainerComponent } from './components/container';
import { OrganizationFormComponent } from './components/organization/organization-form';
import { DepartmentFormComponent } from './components/department/department-form';
import { EmployeeFormComponent } from './components/employee/employee-form';
import { IndividualFormComponent } from './components/individual/individual-form';
import { RoleFormComponent } from './components/role/role-form';
import { SimpleReferenceFormComponent } from './components/simple-reference/simple-reference-form';

export const STAFF_ROUTES: Routes = [{
    path: 'Staff', component: StaffContainerComponent,
    children: [
        { path: '', redirectTo: 'organizations', pathMatch: 'full' },
        { path: 'index', redirectTo: 'organizations', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        //
        { path: 'organizations/:id', component: OrganizationFormComponent },
        { path: 'departments/:id', component: DepartmentFormComponent },
        { path: 'employees/:id', component: EmployeeFormComponent },
        { path: 'individuals/:id', component: IndividualFormComponent },
        { path: 'roles/:id', component: RoleFormComponent },
        //
        { path: ':viewId/:id', component: SimpleReferenceFormComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
