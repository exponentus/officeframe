import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';

import { DISCUSS_URL } from '../../constants';
import { Topic } from '../../models';

@Component({
    selector: 'discuss-topic',
    templateUrl: './topic.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class TopicComponent {
    DISCUSS_URL = DISCUSS_URL;
    @Input() topic: Topic;
    @Input() acl: any = {};
}
