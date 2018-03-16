import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';

import { STAFF_ROUTES } from './staff.routes';
import { StaffService } from './staff.service';

import { StaffContainerComponent } from './components/container';
import { StaffFormComponent } from './components/form';

@NgModule({
    declarations: [
        StaffContainerComponent,
        StaffFormComponent
    ],
    imports: [
        NbCommonModule,
        RouterModule.forChild(STAFF_ROUTES)
    ],
    providers: [
        StaffService
    ]
})
export class StaffModule { }
