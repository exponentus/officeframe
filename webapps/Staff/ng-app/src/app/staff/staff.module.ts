import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';
import { NbEntityCreationDetailsModule } from '@nb/components';

import { STAFF_ROUTES } from './staff.routes';
import { StaffService } from './staff.service';

import { StaffContainerComponent } from './components/container';
import { OrganizationFormComponent } from './components/organization/organization-form';
import { DepartmentFormComponent } from './components/department/department-form';
import { EmployeeFormComponent } from './components/employee/employee-form';
import { IndividualFormComponent } from './components/individual/individual-form';
import { RoleFormComponent } from './components/role/role-form';
import { SimpleReferenceFormComponent } from './components/simple-reference/simple-reference-form';

@NgModule({
    declarations: [
        StaffContainerComponent,
        OrganizationFormComponent,
        DepartmentFormComponent,
        EmployeeFormComponent,
        IndividualFormComponent,
        RoleFormComponent,
        SimpleReferenceFormComponent
    ],
    imports: [
        NbCommonModule,
        NbEntityCreationDetailsModule,
        RouterModule.forChild(STAFF_ROUTES)
    ],
    providers: [
        StaffService
    ]
})
export class StaffModule { }
