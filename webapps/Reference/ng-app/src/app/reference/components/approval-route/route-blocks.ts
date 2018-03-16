import { Component, Input, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { NbModalService } from '@nb/core';

import { STAFF_URL } from '@nb/core';
import { ApprovalRoute, RouteBlock } from '../../models';

@Component({
    selector: 'ref-approval-route-blocks',
    templateUrl: './route-blocks.html',
    styleUrls: ['./route-blocks.css']
})
export class ApprovalRouteBlocksComponent {
    STAFF_URL = STAFF_URL;
    @Input() approvalRoute: ApprovalRoute;

    selectedBlockIds: string[] = [];
    approvalTypes: any[];
    TIME_LIMITS: { id: number, name: string }[];

    constructor(
        private ngxTranslate: TranslateService,
        private nbModalService: NbModalService
    ) {
        this.ngxTranslate.get(['serial', 'parallel', 'signing']).map(t => [
            { id: 'SERIAL', name: t.serial },
            { id: 'PARALLEL', name: t.parallel },
            { id: 'SIGNING', name: t.signing }
        ]).subscribe(it => this.approvalTypes = it);

        this.ngxTranslate.get(['no', 'minutes', 'hour', 'hours', 'day', 'days', 'week']).map(t => [
            { id: 0, name: t.no },
            { id: 30, name: `30 ${t.minutes}` },
            { id: 60, name: `1 ${t.hour}` },
            { id: (60 * 3), name: `3 ${t.hours}` },
            { id: (60 * 6), name: `6 ${t.hours}` },
            { id: (60 * 24 * 1), name: `1 ${t.day}` },
            { id: (60 * 24 * 3), name: `3 ${t.days}` },
            { id: (60 * 24 * 7), name: `1 ${t.week}` }
        ]).subscribe(it => this.TIME_LIMITS = it);
    }

    get editable() {
        return this.approvalRoute.editable;
    }

    onToggleSelected(block: RouteBlock) {
        let idKey = (block.isNew ? 'tid' : 'id');
        let i = this.selectedBlockIds.indexOf(block[idKey]);
        if (i === -1) {
            this.selectedBlockIds.push(block[idKey]);
        } else {
            this.selectedBlockIds.splice(i, 1);
        }
    }

    addNewBlock() {
        if (!this.approvalRoute.routeBlocks) {
            this.approvalRoute.routeBlocks = [];
        }
        this.approvalRoute.routeBlocks.push(new RouteBlock());
    }

    deleteSelectedBlocks() {
        this.approvalRoute.routeBlocks = this.approvalRoute.routeBlocks.filter(it => {
            return this.selectedBlockIds.indexOf(it.isNew ? it.tid : it.id) == -1;
        });
        this.selectedBlockIds = [];
    }
}
