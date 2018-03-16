import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IAction } from '@nb/core';
import { tagStylerFn } from '@nb/core';
import { REFERENCE_URL } from '@nb/core';
import { STAFF_URL } from '@nb/core';
import { DISCUSS_URL } from '../../constants';
import { Topic } from '../../models';

@Component({
    selector: 'discuss-topic-edit',
    templateUrl: './topic-edit.html',
    styleUrls: ['./topic-edit.css']
})
export class TopicEditComponent {
    REFERENCE_URL = REFERENCE_URL;
    STAFF_URL = STAFF_URL;
    tagStylerFn = tagStylerFn;
    title: string = 'topic';

    @Input() topic: Topic;
    @Input() fsId: string = '' + Date.now();
    @Input() actions: IAction[] = [];
    @Input() acl: any = {};
    @Input() errors: any = {};
    @Output() save = new EventEmitter<Topic>();

    constructor(
        private route: ActivatedRoute,
        private router: Router
    ) { }

    saveTopic() {
        this.save.emit(this.topic);
    }

    validateForm() {
        this.errors = {};
    }


    close() {
        this.router.navigate(['../'], { relativeTo: this.route });
    }

    onAction(action: IAction) {
        switch (action.customID) {
            case 'save_and_close':
                this.saveTopic();
                break;
            case 'close':
                this.close();
                break;
            default:
                // console.log(action);
                break;
        }
    }
}
