import { Injectable } from '@angular/core';

import { DataService } from '@nb/core';
import { MONITORING_URL } from './constants';

@Injectable()
export class MonitoringService {

    constructor(
        private dataService: DataService
    ) { }

    fetchRecordsCount() {
        return this.dataService.apiGet(`${MONITORING_URL.API_USERS_RECORDS_COUNT}`);
    }
}
