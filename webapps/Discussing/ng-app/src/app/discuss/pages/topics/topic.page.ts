import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';

import { TopicService } from '../../services';
import { Topic } from '../../models';

@Component({
    selector: 'discuss-topic-page',
    template: `
        <discuss-topic *ngIf="topic" [acl]="acl" [topic]="topic"></discuss-topic>
        <discuss-topic-comments *ngIf="topic; else loading" [topic]="topic"></discuss-topic-comments>
        <ng-template #loading>
            {{'loading' | translate}}
        </ng-template>
    `
})
export class TopicPageComponent implements OnInit {

    topic: Topic;
    acl: any = {};

    constructor(
        private route: ActivatedRoute,
        private topicService: TopicService
    ) { }

    ngOnInit(): void {
        let topicId = this.route.snapshot.params['topicId'];
        this.topicService.fetchTopic(topicId).subscribe(data => {
            this.topic = data.payload.topic;
            this.acl = data.payload.acl;
        });
    }
}
