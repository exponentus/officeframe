import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { IAction, AppService } from '@nb/core';
import { IntegrationService } from '../../integration.service';
import { Service } from '../../models';

@Component({
    selector: 'integ-service-form',
    templateUrl: './service-details.html',
    host: {
        '[class.component]': 'true',
        '[class.load]': 'loading'
    }
})
export class ServiceDetailsComponent {

    loading: boolean = true;
    subs: any[] = [];

    title = 'service';
    model: Service;
    actions: IAction[] = [{
        caption: 'Close',
        icon: 'fa fa-chevron-left',
        className: 'btn-back',
        customID: 'close'
    }];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private ngxTranslate: TranslateService,
        private appService: AppService,
        private integService: IntegrationService
    ) { }

    ngOnInit() {
        this.subs.push(this.route.params.subscribe(params => {
            this.loadData(params['clazz']);
        }));
    }

    ngOnDestroy() {
        this.subs.map(s => s.unsubscribe());
    }

    loadData(serviceId: string) {
        this.loading = true;

        this.integService.fetchService(serviceId).subscribe(
            data => {
                this.appService.setWindowTitle(this.ngxTranslate.instant(data.title));

                this.model = data.payload.model;
                this.title = data.payload.contentTitle || 'service';
                this.loading = false;
            },
            error => {
                this.loading = false;
            }
        );
    }

    close() {
        this.appService.redirectToRedirectUrl();
    }

    onAction(action: IAction) {
        switch (action.customID) {
            case 'close':
                this.close();
                break;
        }
    }
}
