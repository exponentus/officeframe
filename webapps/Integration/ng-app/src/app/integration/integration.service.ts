import { Injectable } from '@angular/core';

import { DataService } from '@nb/core';
import { INTEGRATION_URL } from './constants';
import { Service } from './models';

@Injectable()
export class IntegrationService {

    services: Service[];

    constructor(
        private dataService: DataService
    ) { }

    fetchService(serviceId: string, params: any = {}) {
        return this.dataService.apiGet(`${INTEGRATION_URL.API_SERVICES}/${serviceId}`, params);
    }
}
