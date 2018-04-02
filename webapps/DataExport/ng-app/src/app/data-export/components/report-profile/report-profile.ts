import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import {
    IAction, IApiOutcome, IEntity,
    AppService, ActionService,
    NotificationService, NbModalService,
    tagStylerFn, AbstractFormPage, REFERENCE_URL
} from '@nb/core';

import { DataExportService } from '../../data-export.service';
import { ReportProfile } from '../../models';

@Component({
    selector: 'de-report-profile',
    templateUrl: './report-profile.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class ReportProfileComponent extends AbstractFormPage<ReportProfile> {

    REFERENCE_URL = REFERENCE_URL;
    tagStylerFn = tagStylerFn;

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        public ngxTranslate: TranslateService,
        public notifyService: NotificationService,
        public nbModalService: NbModalService,
        public appService: AppService,
        public actionService: ActionService,
        public deService: DataExportService
    ) {
        super(route, router, ngxTranslate, notifyService, nbModalService, appService, actionService, deService);
    }

    // @Override
    loadDataSuccess(data: IApiOutcome) {
        super.loadDataSuccess(data);
        this.data = {
            entityClassNames: data.payload.entityClassNames,
            reportProfileClassNames: data.payload.reportProfileClassNames,
            exportFormatType: data.payload.exportFormatType.filter(it => it != 'UNKNOWN'),
            reportQueryType: data.payload.reportQueryType.filter(it => it != 'UNKNOWN'),
            languages: data.payload.languages
        };
    }

    // @Override
    onAction(action: IAction) {
        switch (action.customID) {
            case 'to_form':
                this.deService.generateReport(action, <ReportProfile>this.model, { fsid: this.fsId }).subscribe();
                break;
            default:
                super.onAction(action);
                break;
        }
    }
}
