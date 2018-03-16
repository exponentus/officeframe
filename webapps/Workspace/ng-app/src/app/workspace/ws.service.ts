import { Injectable } from '@angular/core';

import { AppService } from '@nb/core';

@Injectable()
export class WorkspaceService {

    constructor(
        private appService: AppService
    ) { }
}
