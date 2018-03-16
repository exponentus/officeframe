import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    IAction, IEntity,
    AppService, ActionService,
    AbstractFormPage,
    NotificationService,
    NbModalService
} from '@nb/core';

import { ReferenceService } from '../../reference.service';
import { ApprovalRoute } from '../../models';

@Component({
    selector: 'approval-route-form',
    templateUrl: './approval-route-form.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class ApprovalRouteFormComponent extends AbstractFormPage<ApprovalRoute> {

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        public ngxTranslate: TranslateService,
        public notifyService: NotificationService,
        public nbModalService: NbModalService,
        public appService: AppService,
        public actionService: ActionService,
        public referenceService: ReferenceService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, referenceService);
    }
}
